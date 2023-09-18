package connectingchips.samchips.mind.service;

import connectingchips.samchips.board.repository.BoardRepository;
import connectingchips.samchips.exception.BadRequestException;
import connectingchips.samchips.joinedmind.dto.JoinCheckResponse;
import connectingchips.samchips.joinedmind.entity.JoinedMind;
import connectingchips.samchips.joinedmind.repository.JoinedMindRepository;
import connectingchips.samchips.mind.dto.request.CreateMindRequest;
import connectingchips.samchips.mind.dto.response.CheckAllMindResponse;
import connectingchips.samchips.mind.dto.response.FindMindResponse;
import connectingchips.samchips.mind.dto.response.MyMindResponse;
import connectingchips.samchips.mind.entity.Mind;
import connectingchips.samchips.mind.repository.MindRepository;
import connectingchips.samchips.user.domain.User;
import connectingchips.samchips.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static connectingchips.samchips.exception.CommonErrorCode.*;

@Service
@RequiredArgsConstructor
public class MindService {

    public static final int CAN_NOT_JOIN = 0;
    public static final int CAN_JOIN = 1;
    private final MindRepository mindRepository;
    private final JoinedMindRepository joinedMindRepository;
    private final UserRepository userRepository;
    private final BoardRepository boardRepository;

    @Transactional
    public FindMindResponse findMind(Long mindId,User user) {
        Integer canJoin = checkCanJoin(mindId, user);
        return FindMindResponse
                .of(findVerifiedMind(mindId),canJoin);
    }

    private static Integer checkCanJoin(Long mindId, User user) {
        Integer canJoin = CAN_NOT_JOIN;
        if(user.getJoinedMinds().stream()
                .anyMatch(joinedMind -> joinedMind.getMind().getMindId() == mindId && joinedMind.getIsJoining() == 0))
            canJoin = CAN_JOIN;
        return canJoin;
    }

    @Transactional
    public List<FindMindResponse> findMinds(User user) {
        return mindRepository.findAll()
                .stream()
                .map(mind -> FindMindResponse.of(mind,checkCanJoin(mind.getMindId(),user)))
                .toList();
    }

    @Transactional
    public List<FindMindResponse> findAllMinds(){
        return mindRepository.findAll()
                .stream()
                .map(FindMindResponse::of)
                .toList();
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
    public void createMind(CreateMindRequest createMindRequest) {
        mindRepository.save(Mind.builder()
                .name(createMindRequest.getName())
                .introduce(createMindRequest.getIntroduce())
                .backgroundImage(createMindRequest.getBackgroundImage())
                .writeFormat(createMindRequest.getWriteFormat())
                .exampleImage(createMindRequest.getExampleImage())
                .build());
    }

    @Transactional
    public void deleteMind(Long mindId) {
        mindRepository.delete(findVerifiedMind(mindId));
    }

    @Transactional
    public List<FindMindResponse> findAllMindExceptMe(User loginUser) {
        List<Long> list = loginUser.getJoinedMinds().stream().map(user -> user.getMind().getMindId()).toList();
        return mindRepository.findAll()
                .stream()
                .filter(mind -> !list.contains(mind.getMindId()))
                .map(mind -> FindMindResponse.of(mind,checkCanJoin(mind.getMindId(),loginUser)))
                .collect(Collectors.toList());

    }

    public List<MyMindResponse> findMyJoinedMindList(User loginUser) {
        return loginUser.getJoinedMinds()
                .stream()
                .map(joinedMind -> MyMindResponse.builder()
                        .mindId(joinedMind.getMind().getMindId())
                        .mindTypeName(joinedMind.getMind().getMindType().getName())
                        .name(joinedMind.getMind().getName())
                        .isDoneToday(joinedMind.getTodayWrite())
                        .image(joinedMind.getMind().getBackgroundImage())
                        .boardCount(boardRepository.findBoardCountByUserAndMind(loginUser, joinedMind.getMind()))
                        .count(joinedMind.getCount())
                        .build())
                .toList();
    }
}
