package connectingchips.samchips.joinedmind.service;

import connectingchips.samchips.joinedmind.dto.service.JoinCheckOutPut;
import connectingchips.samchips.joinedmind.entity.JoinedMind;
import connectingchips.samchips.joinedmind.repository.JoinedMindRepository;
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
public class JoinedMindService {

    public static final int JOIN = 1;
    public static final int NOT_JOIN = 0;
    private final JoinedMindRepository joinedMindRepository;
    private final UserRepository userRepository;
    private final MindRepository mindRepository;


    public JoinCheckOutPut JoinCheck(Long joinedMindId) {

        return JoinCheckOutPut.of(findVerifiedJoinedMind(joinedMindId).getIsJoining());
    }

    public void makeMindRelation(Long mindId, Long userId) {
        Mind mind = findVerifiedMind(mindId);
        User user = findVerifiedUser(userId);
        checkAlreadyJoined(mindId, user);
        JoinedMind joinedMind = JoinedMind.builder()
                .mind(mind)
                .user(user)
                .build();
        joinedMindRepository.save(joinedMind);
    }

    public void reMindRelation(Long joinedMindId, Long userId) {
        User user = findVerifiedUser(userId);
        List<JoinedMind> joinedMinds = user.getJoinedMinds();
        JoinedMind verifiedJoinedMind = findVerifiedJoinedMind(joinedMindId);
        verifiedJoinedMind.setIsJoining(JOIN);
        joinedMinds.add(verifiedJoinedMind);
        user.editJoinedMinds(joinedMinds);
        userRepository.save(user);
    }

    public void exitMindRelation(Long joinedMindId, Long userId) {
        JoinedMind joinedMind = findVerifiedJoinedMind(joinedMindId);
        User user = findVerifiedUser(userId);
        List<JoinedMind> joinedMinds = user.getJoinedMinds();
        joinedMinds.remove(joinedMind);
        joinedMind.setIsJoining(NOT_JOIN);
        user.editJoinedMinds(joinedMinds);
        joinedMindRepository.save(joinedMind);
        userRepository.save(user);
    }

    private static void checkAlreadyJoined(Long mindId, User user) {
        Optional<JoinedMind> first = user.getJoinedMinds().stream().filter(jm -> jm.getMind().getMindId().equals(mindId))
                .findFirst();
        if(first.isPresent()) throw new RuntimeException("이미 가입한 작심입니다.");
    }
    private Mind findVerifiedMind(Long mindId) {
        Optional<Mind> findMindById = mindRepository.findById(mindId);
        return findMindById.orElseThrow(() ->
                new RuntimeException("존재하지 않는 작심 번호입니다."));
    }

    private User findVerifiedUser(Long userId) {
        Optional<User> findUserById = userRepository.findById(userId);
        return findUserById.orElseThrow(() ->
                new RuntimeException("존재하지 않는 유저 번호입니다."));
    }

    private JoinedMind findVerifiedJoinedMind(Long joinedMindId) {
        Optional<JoinedMind> findJoinedMindById = joinedMindRepository.findById(joinedMindId);
        return findJoinedMindById.orElseThrow(() ->
                new RuntimeException("존재하지 않는 참여한 작심 번호입니다."));
    }

}
