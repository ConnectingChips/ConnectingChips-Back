package connectingchips.samchips.mind.service;

import connectingchips.samchips.board.S3Uploader;
import connectingchips.samchips.board.repository.BoardRepository;
import connectingchips.samchips.exception.BadRequestException;
import connectingchips.samchips.joinedmind.dto.JoinCheckResponse;
import connectingchips.samchips.joinedmind.entity.JoinedMind;
import connectingchips.samchips.joinedmind.repository.JoinedMindRepository;

import connectingchips.samchips.mind.dto.request.CreateMindRequest;
import connectingchips.samchips.mind.dto.request.UpdateMindRequest;
import connectingchips.samchips.mind.dto.response.*;
import connectingchips.samchips.mind.entity.Mind;
import connectingchips.samchips.mind.repository.MindRepository;
import connectingchips.samchips.mindtype.entity.MindType;
import connectingchips.samchips.mindtype.repository.MindTypeRepository;
import connectingchips.samchips.user.domain.User;
import connectingchips.samchips.user.repository.UserRepository;
import connectingchips.samchips.utils.CustomBeanUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import static connectingchips.samchips.exception.CommonErrorCode.*;

@Service
@RequiredArgsConstructor
public class MindService {

    public static final int ANONYMOUS_USER = -1;
    public static final int NOT_JOIN = 0;
    public static final int CAN_JOIN = 1;
    public static final int FIRST_IMAGE_NUM = 0;
    public static final int SECOND_IMAGE_NUM = 1;
    public static final int THIRD_IMAGE_NUM = 2;
    public static final int FOURTH_IMAGE_NUM = 3;
    public static final int FULL_COUNT = 3;
    private final MindRepository mindRepository;
    private final MindTypeRepository mindTypeRepository;
    private final JoinedMindRepository joinedMindRepository;
    private final UserRepository userRepository;
    private final BoardRepository boardRepository;
    private final CustomBeanUtils<Mind> beanUtils;
    private final S3Uploader s3Uploader;

    @Transactional
    public FindIntroMindResponse findIntroMindNotAccountId(Long mindId) {
        return FindIntroMindResponse
                .of(findVerifiedMind(mindId),ANONYMOUS_USER);
    }
    @Transactional
    public FindIntroMindResponse findIntroMindByAccountId(Long mindId, String accountId) {
        User user = findVerifiedUserByAccount(accountId);

        return FindIntroMindResponse
                .of(findVerifiedMind(mindId),checkCanJoin(mindId, user));
    }
    @Transactional
    public FindPageMindResponse findPageMindByUser(Long mindId, String accountId) {
        User user = findVerifiedUserByAccount(accountId);
        Mind verifiedMind = findVerifiedMind(mindId);
        return user.getJoinedMinds()
                .stream()
                .filter(joinedMind -> Objects.equals(joinedMind.getMind().getMindId(), mindId)).findFirst()
                .map(joinedMind -> FindPageMindResponse.of(verifiedMind, joinedMind.getTodayWrite(), joinedMind.getCount()))
                .orElse(FindPageMindResponse.of(verifiedMind, false, NOT_JOIN));
    }
    @Transactional
    public FindPageMindResponse findPageMindByAnonymous(Long mindId) {
        Mind verifiedMind = findVerifiedMind(mindId);
        return FindPageMindResponse.of(verifiedMind, false, NOT_JOIN);
    }

    private User findVerifiedUserByAccount(String accountId) {
        Optional<User> byAccountId = userRepository.findByAccountId(accountId);
        User user = byAccountId.orElseThrow(() -> new BadRequestException(NOT_FOUND_USER));
        return user;
    }



    private static Integer checkCanJoin(Long mindId, User user) {
        Integer canJoin = NOT_JOIN;
        if(user.getJoinedMinds().stream()
                .anyMatch(joinedMind -> Objects.equals(joinedMind.getMind().getMindId(), mindId) && joinedMind.getIsJoining() == NOT_JOIN))
            canJoin = CAN_JOIN;
        return canJoin;
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
    public JoinCheckResponse checkToday(Long joinedMindId) {
        return JoinCheckResponse.of(findVerifiedJoinedMind(joinedMindId).getTodayWrite());
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

    private JoinedMind findVerifiedJoinedMind(Long joinedMindId) {
        Optional<JoinedMind> findJoinedMindById = joinedMindRepository.findById(joinedMindId);
        return findJoinedMindById.orElseThrow(() ->
                new BadRequestException(NOT_FOUND_JOINED_MIND_ID));
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
    @Transactional
    public List<FindTotalMindResponse> findAllMindExceptMe(String accountId) {
        User loginUser = findVerifiedUserByAccount(accountId);
        List<Long> list = loginUser.getJoinedMinds().stream().map(user -> user.getMind().getMindId()).toList();
        return mindRepository.findAll()
                .stream()
                .filter(mind -> !list.contains(mind.getMindId()))
                .map(mind -> FindTotalMindResponse.of(mind,checkCanJoin(mind.getMindId(),loginUser)))
                .collect(Collectors.toList());
    }

    @Transactional
    public List<FindTotalMindResponse> findAllMindExceptMe() {
        return mindRepository.findAll()
                .stream()
                .map(mind -> FindTotalMindResponse.of(mind, ANONYMOUS_USER))
                .toList();
    }
    @Transactional
    public List<FindTotalMindResponse> findAllMindExceptMeByMindType(String accountId,Long mindTypeId) {
        User loginUser = findVerifiedUserByAccount(accountId);
        List<Long> list = loginUser.getJoinedMinds().stream().map(user -> user.getMind().getMindId()).toList();

        return mindRepository.findAll()
                .stream()
                .filter(mind -> Objects.equals(mind.getMindType().getMindTypeId(), mindTypeId) &&!list.contains(mind.getMindId()))
                .map(mind -> FindTotalMindResponse.of(mind,checkCanJoin(mind.getMindId(),loginUser)))
                .toList();

    }

    @Transactional
    public List<FindTotalMindResponse> findAllMindExceptMeByMindType(Long mindTypeId) {
        return mindRepository.findAll()
                .stream()
                .filter(mind -> Objects.equals(mind.getMindType().getMindTypeId(), mindTypeId))
                .map(mind -> FindTotalMindResponse.of(mind, ANONYMOUS_USER))
                .toList();
    }

    @Transactional
    public MyMindResponse findMyJoinMind(User loginUser, Long mindId) {
        JoinedMind jm = loginUser.getJoinedMinds()
                .stream()
                .filter(joinedMind -> Objects.equals(joinedMind.getMind().getMindId(), mindId))
                .findFirst().orElseThrow(() -> new BadRequestException(NOT_JOIN_MIND));
        return MyMindResponse.of(jm.getMind(),jm.getCount(),boardRepository.findBoardCountByUserAndMind(loginUser,jm.getMind()),
                jm.getTodayWrite());
    }

    @Transactional
    public List<MyMindResponse> findMyJoinMindList(User loginUser) {
        return loginUser.getJoinedMinds()
                .stream()
                .filter(joinedMind -> joinedMind.getIsJoining() == CAN_JOIN)
                .map(joinedMind ->
                        MyMindResponse.of(joinedMind.getMind(),
                                joinedMind.getCount(),
                                boardRepository.findBoardCountByUserAndMind(loginUser,joinedMind.getMind()),
                                joinedMind.getTodayWrite()))
                .toList();
    }

    @Transactional
    public List<MyJoinedMindResponse> findMyJoinedMindList(User loginUser) {
        return loginUser.getJoinedMinds()
                .stream()
                .filter(joinedMind -> joinedMind.getIsJoining() == NOT_JOIN)
                .map(joinedMind -> MyJoinedMindResponse.of(joinedMind,boardRepository.findBoardCountByUserAndMind(loginUser,joinedMind.getMind())))
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
                .filter(joinedMind -> Objects.equals(joinedMind.getMind().getMindId(), mindId) &&joinedMind.getIsJoining() == NOT_JOIN)
                .findFirst();
        return first.map(CheckReMindResponse::of).orElseGet(CheckReMindResponse::of);
    }

}
