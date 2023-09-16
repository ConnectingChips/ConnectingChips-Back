package connectingchips.samchips.joinedmind.service;

import connectingchips.samchips.exception.BadRequestException;
import connectingchips.samchips.exception.ExceptionCode;
import connectingchips.samchips.joinedmind.dto.JoinCheckResponse;
import connectingchips.samchips.joinedmind.entity.JoinedMind;
import connectingchips.samchips.joinedmind.repository.JoinedMindRepository;
import connectingchips.samchips.mind.entity.Mind;
import connectingchips.samchips.mind.repository.MindRepository;
import connectingchips.samchips.user.domain.User;
import connectingchips.samchips.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static connectingchips.samchips.exception.ExceptionCode.*;

@Service
@RequiredArgsConstructor
public class JoinedMindService {

    public static final int JOIN = 1;
    public static final int NOT_JOIN = 0;
    private final JoinedMindRepository joinedMindRepository;
    private final UserRepository userRepository;
    private final MindRepository mindRepository;

    @Transactional
    public JoinCheckResponse JoinCheck(Long joinedMindId) {

        return JoinCheckResponse.of(findVerifiedJoinedMind(joinedMindId).getIsJoining() == JOIN);
    }
    @Transactional
    public void makeMindRelation(Long mindId, User user) {
        Mind mind = findVerifiedMind(mindId);
        checkAlreadyJoined(mindId, user);
        JoinedMind joinedMind = JoinedMind.builder()
                .mind(mind)
                .user(user)
                .build();
        joinedMindRepository.save(joinedMind);
    }
    @Transactional
    public void reMindRelation(Long joinedMindId, Long userId) {
        User user = findVerifiedUser(userId);
        List<JoinedMind> joinedMinds = user.getJoinedMinds();
        JoinedMind verifiedJoinedMind = findVerifiedJoinedMind(joinedMindId);
        verifiedJoinedMind.setIsJoining(JOIN);
        joinedMinds.add(verifiedJoinedMind);
        user.editJoinedMinds(joinedMinds);
        userRepository.save(user);
    }
    @Transactional
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
        Optional<JoinedMind> first = user.getJoinedMinds().stream()
                .filter(jm -> jm.getMind().getMindId().equals(mindId) && jm.getIsJoining().equals(JOIN))
                .findFirst();
        if(first.isPresent()) throw new BadRequestException(ALREADY_JOIN_MIND);
    }
    private Mind findVerifiedMind(Long mindId) {
        Optional<Mind> findMindById = mindRepository.findById(mindId);
        return findMindById.orElseThrow(() ->
                new BadRequestException(NOT_FOUND_MIND_ID));
    }

    private User findVerifiedUser(Long userId) {
        Optional<User> findUserById = userRepository.findById(userId);
        return findUserById.orElseThrow(() ->
                new BadRequestException(NOT_FOUND_USER_ID));
    }

    private JoinedMind findVerifiedJoinedMind(Long joinedMindId) {
        Optional<JoinedMind> findJoinedMindById = joinedMindRepository.findById(joinedMindId);
        return findJoinedMindById.orElseThrow(() ->
                new BadRequestException(NOT_FOUND_JOINED_MIND_ID));
    }

}
