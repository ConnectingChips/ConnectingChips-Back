package connectingchips.samchips.mind.service;

import connectingchips.samchips.board.entity.Board;
import connectingchips.samchips.board.repository.BoardRepository;
import connectingchips.samchips.joinedmind.dto.JoinCheckResponse;
import connectingchips.samchips.joinedmind.entity.JoinedMind;
import connectingchips.samchips.joinedmind.repository.JoinedMindRepository;
import connectingchips.samchips.mind.dto.controller.CreateMindInput;
import connectingchips.samchips.mind.dto.service.CheckAllMindOutput;
import connectingchips.samchips.mind.dto.service.FindMindOutput;
import connectingchips.samchips.mind.entity.Mind;
import connectingchips.samchips.mind.repository.MindRepository;
import connectingchips.samchips.user.domain.User;
import connectingchips.samchips.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MindService {

    private final MindRepository mindRepository;
    private final JoinedMindRepository joinedMindRepository;
    private final UserRepository userRepository;
    private final BoardRepository boardRepository;


    public FindMindOutput findMind(long mindId) {
        return FindMindOutput
                .of(findVerifiedMind(mindId), joinedMindRepository.countJoinedMindUser(mindId));
        //한번할때마다 너무 큰 연산이 필요한거 같음 Mind에 카운트 수를 두는게 나아보임
    }

    public List<FindMindOutput> findMinds() {
        return mindRepository.findAll()
                .stream()
                .map(a -> FindMindOutput.of(a, joinedMindRepository.countJoinedMindUser(a.getMindId())))
                .toList();
    }


    public Mind findVerifiedMind(Long mindId) {
        Optional<Mind> findMindById = mindRepository.findById(mindId);
        return findMindById.orElseThrow(() ->
                new RuntimeException("존재하지 않는 작심 번호입니다."));
    }

//    public List<CheckAllMindOutput> checkTodayAll(Long userId) {
//        List<Board> userBoard = boardRepository.findAllByCreatedAtAndUserId(Timestamp.valueOf(LocalDateTime.now()), userId);
//        userBoard.stream().
//
//        //해당 유저-> 참여한 작심-> 작심 id랑
//                //board -> 작심 id 비교
//    }


    private User findVerifiedUser(Long userId) {
        Optional<User> findUserById = userRepository.findById(userId);
        return findUserById.orElseThrow(() ->
                new RuntimeException("존재하지 않는 유저 번호입니다."));
    }
    private Board findVerifiedBoard(Long boardId) {
        Optional<Board> byId = boardRepository.findById(boardId);
        return byId.orElseThrow(() ->
                new RuntimeException("존재하지 않는 게시판 번호입니다."));
    }

    public JoinCheckResponse checkToday(Long joinedMindId) {
        return JoinCheckResponse.of(findVerifiedJoinedMind(joinedMindId).getTodayWrite());
    }

    public List<CheckAllMindOutput> checkTodayAll(Long userId) {
        return findVerifiedUser(userId)
                .getJoinedMinds().stream()
                .map(joinedMind ->
                        CheckAllMindOutput.builder()
                                .joinedMindId(joinedMind.getJoinedMindId())
                                .isDoneToday(joinedMind.getTodayWrite()).build()).toList();
    }

    private JoinedMind findVerifiedJoinedMind(Long joinedMindId) {
        Optional<JoinedMind> findJoinedMindById = joinedMindRepository.findById(joinedMindId);
        return findJoinedMindById.orElseThrow(() ->
                new RuntimeException("존재하지 않는 참여한 작심 번호입니다."));
    }

    public void createMind(CreateMindInput createMindInput) {
        mindRepository.save(Mind.builder()
                .name(createMindInput.getName())
                .introduce(createMindInput.getIntroduce())
                .backgroundImage(createMindInput.getBackgroundImage())
                .writeFormat(createMindInput.getWriteFormat())
                .exampleImage(createMindInput.getExampleImage())
                .build());
    }

    public void deleteMind(Long mindId) {
        mindRepository.delete(findVerifiedMind(mindId));
    }

}
