package connectingchips.samchips.mind.service;

import connectingchips.samchips.exception.BadRequestException;
import connectingchips.samchips.exception.ExceptionCode;
import connectingchips.samchips.joinedmind.dto.JoinCheckResponse;
import connectingchips.samchips.joinedmind.entity.JoinedMind;
import connectingchips.samchips.joinedmind.repository.JoinedMindRepository;
import connectingchips.samchips.mind.dto.request.CreateMindRequest;
import connectingchips.samchips.mind.dto.response.CheckAllMindResponse;
import connectingchips.samchips.mind.dto.response.FindMindResponse;
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

import static connectingchips.samchips.exception.ExceptionCode.*;

@Service
@RequiredArgsConstructor
public class MindService {

    private final MindRepository mindRepository;
    private final JoinedMindRepository joinedMindRepository;
    private final UserRepository userRepository;

    @Transactional
    public FindMindResponse findMind(long mindId) {
        return FindMindResponse
                .of(findVerifiedMind(mindId), joinedMindRepository.countJoinedMindUser(mindId));
    }
    @Transactional
    public List<FindMindResponse> findMinds() {
        return mindRepository.findAll()
                .stream()
                .map(a -> FindMindResponse.of(a, joinedMindRepository.countJoinedMindUser(a.getMindId())))
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
                new BadRequestException(NOT_FOUND_USER_ID));
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

    public void deleteMind(Long mindId) {
        mindRepository.delete(findVerifiedMind(mindId));
    }

    public List<FindMindResponse> findAllMindExceptMe(User loginUser) {
        List<Long> list = loginUser.getJoinedMinds().stream().map(user -> user.getMind().getMindId()).toList();
        return mindRepository.findAll()
                .stream()
                .filter(mind -> !list.contains(mind.getMindId()))
                .map(mind -> FindMindResponse.of(mind,joinedMindRepository.countJoinedMindUser(mind.getMindId())))
                .collect(Collectors.toList());

    }
}
