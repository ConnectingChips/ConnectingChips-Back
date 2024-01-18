package connectingchips.samchips.mind.service;

import connectingchips.samchips.board.S3Uploader;
import connectingchips.samchips.board.repository.BoardRepository;
import connectingchips.samchips.global.exception.BadRequestException;
import connectingchips.samchips.mind.entity.JoinedMind;
import connectingchips.samchips.mind.repository.JoinedMindRepository;

import connectingchips.samchips.mind.dto.request.CreateMindRequest;
import connectingchips.samchips.mind.dto.request.UpdateMindRequest;
import connectingchips.samchips.mind.dto.response.*;
import connectingchips.samchips.mind.entity.Mind;
import connectingchips.samchips.mind.repository.MindRepository;
import connectingchips.samchips.mind.entity.MindType;
import connectingchips.samchips.mind.repository.MindTypeRepository;
import connectingchips.samchips.user.domain.User;
import connectingchips.samchips.user.repository.UserRepository;
import connectingchips.samchips.global.utils.CustomBeanUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import static connectingchips.samchips.global.exception.CommonErrorCode.*;
import static connectingchips.samchips.mind.controller.MindController.ANONYMOUS_USER;

@Service
@RequiredArgsConstructor
@Slf4j
public class MindService {

    public static final int NOT_MEMBER = -1;
    public static final int NOT_JOIN = 0;
    public static final int CAN_JOIN = 1;
    public static final int FIRST_IMAGE_NUM = 0;
    public static final int SECOND_IMAGE_NUM = 1;
    public static final int THIRD_IMAGE_NUM = 2;
    public static final int FOURTH_IMAGE_NUM = 3;

    private final MindRepository mindRepository;
    private final MindTypeRepository mindTypeRepository;
    private final JoinedMindRepository joinedMindRepository;
    private final UserRepository userRepository;
    private final BoardRepository boardRepository;
    private final CustomBeanUtils<Mind> beanUtils;
    private final S3Uploader s3Uploader;

    /* auth.getPrincipal().toString()은 토큰이 존재할경우
    connectingchips.samchips.user.domain.UserAdapter
    [Username=test1234,
    Password=[PROTECTED],
    Enabled=true,
    AccountNonExpired=true,
    credentialsNonExpired=true,
    AccountNonLocked=true,
    Granted Authorities=[ROLE_USER]]
    위와 같은 String 형식의 데이터를 전달하고 토큰이 없을경우 anonymousUser라는 String 데이터를 반환합니다.
    아래의 메서드는 auth.getPrincipal().toString() 데이터에서 Username부분(위의 예시에서는 test1234)를
    사용하기위해 다른 String부분을 잘라주는 메서드입니다. 이거는 확인하시고
    Authentication에서 회원id를 추출해서 반환한다로 변경해주셔도 될거같습니다. */

    public String getUsernameByAuthentication(Authentication auth) {
        String principal = auth.getPrincipal().toString();
        if(principal.equals(ANONYMOUS_USER)) return principal;
        return principal.substring(principal.indexOf("=")+1, principal.indexOf(","));
    }

    @Transactional
    public FindIntroMindResponse findIntroMindNotUsername(Long mindId) {
        Mind verifiedMind = findVerifiedMind(mindId);
        int size = makeJoinMindSize(verifiedMind);
        return FindIntroMindResponse
                .of(findVerifiedMind(mindId), NOT_MEMBER,size);
    }
    @Transactional
    public MindExampleImageResponse getExampleImage(Long mindId) {
        return MindExampleImageResponse.of(findVerifiedMind(mindId));
    }

    private static int makeJoinMindSize(Mind verifiedMind) {
        return verifiedMind.getJoinedMinds().stream().filter(jm -> jm.getIsJoining() == CAN_JOIN).toList().size();
    }

    @Transactional
    public FindIntroMindResponse findIntroMindByUsername(Long mindId, String username) {
        User user = findVerifiedUserByUsername(username);

        return FindIntroMindResponse
                .of(findVerifiedMind(mindId),checkCanJoin(mindId, user));
    }
    @Transactional
    public FindPageMindResponse findPageMindByUser(Long mindId, String username) {
        User user = findVerifiedUserByUsername(username);
        Mind verifiedMind = findVerifiedMind(mindId);
        return user.getJoinedMinds()
                .stream()
                .filter(joinedMind -> Objects.equals(joinedMind.getMind().getMindId(), mindId)).findFirst()
                .map(joinedMind -> FindPageMindResponse.of(verifiedMind, joinedMind.getTodayWrite(), joinedMind.getCount(),makeJoinMindSize(verifiedMind)))
                .orElse(FindPageMindResponse.of(verifiedMind, false, NOT_JOIN,makeJoinMindSize(verifiedMind)));
    }
    @Transactional
    public FindPageMindResponse findPageMindByAnonymous(Long mindId) {
        Mind verifiedMind = findVerifiedMind(mindId);
        return FindPageMindResponse.of(verifiedMind, false, NOT_JOIN,makeJoinMindSize(verifiedMind));
    }

    private User findVerifiedUserByUsername(String username) {
        Optional<User> byAccountId = userRepository.findByAccountId(username);
        return byAccountId.orElseThrow(() -> new BadRequestException(NOT_FOUND_USER));
    }

    private static Integer checkCanJoin(Long mindId, User user) {
        if(user.getJoinedMinds().stream()
                .anyMatch(joinedMind -> Objects.equals(joinedMind.getMind().getMindId(), mindId) && joinedMind.getIsJoining() == NOT_JOIN))
            return CAN_JOIN;
        return NOT_JOIN;
    }

    @Transactional
    public Mind findVerifiedMind(Long mindId) {
        Optional<Mind> findMindById = mindRepository.findById(mindId);
        return findMindById.orElseThrow(() ->
                new BadRequestException(NOT_FOUND_MIND_ID));
    }

    private User findVerifiedUser(Long userId) {
        Optional<User> findUserById = userRepository.findById(userId);
        return findUserById.orElseThrow(() ->
                new BadRequestException(NOT_FOUND_USER));
    }

    @Transactional
    public List<CheckAllMindResponse> checkTodayAll(Long userId) {
        return findVerifiedUser(userId)
                .getJoinedMinds().stream()
                .map(joinedMind ->
                        CheckAllMindResponse.builder()
                                .joinedMindId(joinedMind.getJoinedMindId())
                                .isDoneToday(joinedMind.getTodayWrite()).build()).toList();
    }

    @Transactional
    public List<FindTotalMindResponse> findAllMindExceptMe(String accountId) {
        User loginUser = findVerifiedUserByUsername(accountId);
        List<Long> list = loginUser.getJoinedMinds().stream()
                .filter(joinedMind -> joinedMind.getIsJoining() == CAN_JOIN)
                .map(user -> user.getMind().getMindId()).toList();
        return mindRepository.findAll()
                .stream()
                .filter(mind -> !list.contains(mind.getMindId()))
                .map(mind -> FindTotalMindResponse.of(mind,checkCanJoin(mind.getMindId(),loginUser),makeJoinMindSize(mind)))
                .collect(Collectors.toList());
    }

    @Transactional
    public List<FindTotalMindResponse> findAllMindExceptMe() {
        return mindRepository.findAll()
                .stream()
                .map(mind -> FindTotalMindResponse.of(mind, NOT_MEMBER,makeJoinMindSize(mind)))
                .toList();
    }
    @Transactional
    public List<FindTotalMindResponse> findAllMindExceptMeByMindType(String accountId,Long mindTypeId) {
        User loginUser = findVerifiedUserByUsername(accountId);
        List<Long> list = loginUser.getJoinedMinds().stream()
                .filter(joinedMind -> joinedMind.getIsJoining() == CAN_JOIN)
                .map(jm -> jm.getMind().getMindId()).toList();

        return mindRepository.findAll()
                .stream()
                .filter(mind -> Objects.equals(mind.getMindType().getMindTypeId(), mindTypeId) &&!list.contains(mind.getMindId()))
                .map(mind -> FindTotalMindResponse.of(mind,checkCanJoin(mind.getMindId(),loginUser),makeJoinMindSize(mind)))
                .toList();

    }

    @Transactional
    public List<FindTotalMindResponse> findAllMindExceptMeByMindType(Long mindTypeId) {
        return mindRepository.findAll()
                .stream()
                .filter(mind -> Objects.equals(mind.getMindType().getMindTypeId(), mindTypeId))
                .map(mind -> FindTotalMindResponse.of(mind, NOT_MEMBER,makeJoinMindSize(mind)))
                .toList();
    }

    public MyMindResponse findMyJoinMind(User loginUser, Long mindId) {
        JoinedMind jm = getJoinedMind(loginUser, mindId);
        return MyMindResponse.of(jm.getMind(),jm.getCount(),boardRepository.findBoardCountByUserAndMind(loginUser,jm.getMind()),
                jm.getTodayWrite(),jm.getKeepJoin());
    }

    private static JoinedMind getJoinedMind(User loginUser, Long mindId) {
        JoinedMind jm = loginUser.getJoinedMinds()
                .stream()
                .filter(joinedMind -> Objects.equals(joinedMind.getMind().getMindId(), mindId))
                .findFirst().orElseThrow(() -> new BadRequestException(NOT_JOIN_MIND));
        return jm;
    }

    @Transactional
    public List<MyMindResponse> findMyJoinMindList(User loginUser) {
        //항상 로그인 유저는 무조건 존재한다고 가정, 캐시데이터에는 JoinedMind에 대한 값이 저장되어 있지 않기 때문에
        //UserRepository에서 실제 User의 전체값을 가져와 사용
        User finalLoginUser = userRepository.findById(loginUser.getId()).get();
        return loginUser.getJoinedMinds()
                .stream()
                .filter(joinedMind -> joinedMind.getIsJoining() == CAN_JOIN)
                .map(joinedMind ->
                        MyMindResponse.of(joinedMind.getMind(),
                                joinedMind.getCount(),
                                boardRepository.findBoardCountByUserAndMind(finalLoginUser,joinedMind.getMind()),
                                joinedMind.getTodayWrite(),
                                joinedMind.getKeepJoin()))
                .toList();
    }

    @Transactional
    public List<MyJoinedMindResponse> findMyJoinedMindList(User loginUser) {
        //항상 로그인 유저는 무조건 존재한다고 가정, 캐시데이터에는 JoinedMind에 대한 값이 저장되어 있지 않기 때문에
        //UserRepository에서 실제 User의 전체값을 가져와 사용
        User finalLoginUser = userRepository.findById(loginUser.getId()).get();
        return loginUser.getJoinedMinds()
                .stream()
                .filter(joinedMind -> joinedMind.getIsJoining() == NOT_JOIN)
                .map(joinedMind -> MyJoinedMindResponse.of(joinedMind,boardRepository.findBoardCountByUserAndMind(finalLoginUser,joinedMind.getMind())))
                .toList();
    }


    @Transactional
    public IntroImageResponse findIntroMindImage(Long mindId) {
        return IntroImageResponse.of(findVerifiedMind(mindId));
    }

    @Transactional
    public PageImageResponse findPageMindImage(Long mindId) {
        return PageImageResponse.of(findVerifiedMind(mindId));
    }

    private MindType findVerifiedMindType(Long mindTypeId) {
        Optional<MindType> byId = mindTypeRepository.findById(mindTypeId);
        return byId.orElseThrow(() ->
                new BadRequestException(NOT_FOUND_MIND_TYPE_ID));
    }

    public CheckReMindResponse checkReMindAvailability(User loginUser,Long mindId){
        Optional<JoinedMind> first = loginUser.getJoinedMinds().stream()
                .filter(joinedMind -> Objects.equals(joinedMind.getMind().getMindId(), mindId))
                .findFirst();
        return first.map(CheckReMindResponse::of).orElseGet(CheckReMindResponse::of);
    }

    public CheckReMindResponse changeIsDoneToday(Long mindId, User user) {
        JoinedMind joinedMind = getJoinedMind(user, mindId);
        joinedMind.updateTodayWrite(false);
        joinedMindRepository.save(joinedMind);
        return CheckReMindResponse.of(joinedMind);
    }

    @Transactional
    public MindResponse createMind(CreateMindRequest createMindRequest,
                                   List<MultipartFile> images) throws IOException {
        List<String> upload = s3Uploader.upload(images);

        return MindResponse.of(mindRepository.save(Mind.builder()
                .name(createMindRequest.getName())
                .introduce(createMindRequest.getIntroduce())
                .introImage(upload.get(FIRST_IMAGE_NUM))
                .writeFormat(createMindRequest.getWriteFormat())
                .pageImage(upload.get(SECOND_IMAGE_NUM))
                .myListImage(upload.get(THIRD_IMAGE_NUM))
                .totalListImage(upload.get(FOURTH_IMAGE_NUM))
                .mindType(findVerifiedMindType(createMindRequest.getMindTypeId()))
                .build()));
    }

    @Transactional
    public MindResponse updateMind(Long mindId,
                                   UpdateMindRequest updateMindRequest,
                                   List<MultipartFile> images) throws IOException {
        MindType mindType= new MindType();
        if(updateMindRequest == null) updateMindRequest = new UpdateMindRequest();
        if(updateMindRequest.getMindTypeId() != null) mindType = findVerifiedMindType(updateMindRequest.getMindTypeId());

        Mind verifiedMind = findVerifiedMind(mindId);
        List<String> upload = s3Uploader.upload(images);
        return MindResponse.of(beanUtils.copyNonNullProperties(
                Mind.builder()
                        .name(updateMindRequest.getName())
                        .introduce(updateMindRequest.getIntroduce())
                        .writeFormat(updateMindRequest.getWriteFormat())
                        .introImage(upload.get(FIRST_IMAGE_NUM))
                        .pageImage(upload.get(SECOND_IMAGE_NUM))
                        .totalListImage(upload.get(THIRD_IMAGE_NUM))
                        .myListImage(upload.get(FOURTH_IMAGE_NUM))
                        .mindType(mindType)
                        .build(), verifiedMind));
    }

    @Transactional
    public void deleteMind(Long mindId) {
        mindRepository.delete(findVerifiedMind(mindId));
    }
}
