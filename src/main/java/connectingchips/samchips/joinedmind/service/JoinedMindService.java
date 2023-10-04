package connectingchips.samchips.joinedmind.service;

import connectingchips.samchips.exception.BadRequestException;
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
import java.util.Objects;
import java.util.Optional;

import static connectingchips.samchips.exception.CommonErrorCode.*;

@Service
@RequiredArgsConstructor
public class JoinedMindService {

    public static final int JOIN = 1;
    public static final int NOT_JOIN = 0;
    public static final int FULL_COUNT = 3;
    public static final int ZERO_COUNT = 0;
    private final JoinedMindRepository joinedMindRepository;
    private final UserRepository userRepository;
    private final MindRepository mindRepository;

    @Transactional
    public JoinCheckResponse JoinCheck(Long mindId,User loginUser) {
        return JoinCheckResponse.of(checkJoinedMind(mindId, loginUser));
    }

    private static boolean checkJoinedMind(Long mindId, User loginUser) {
        return loginUser.getJoinedMinds()
                .stream()
                .anyMatch(joinedMind -> Objects.equals(joinedMind.getMind().getMindId(), mindId) && joinedMind.getIsJoining() == JOIN);
    }

    @Transactional
    public void makeMindRelation(Long mindId, User user) {
        Mind mind = findVerifiedMind(mindId);
        Optional<JoinedMind> first = beforeJoinedCheck(mindId, user);
        if(first.isPresent()) {
            reMindRelation(first.get().getJoinedMindId(),user);
            return;
        }
        checkAlreadyJoined(mindId, user);
        checkJoinMindCountMax(user);
        JoinedMind joinedMind = new JoinedMind();
        joinedMind.setUser(user);
        joinedMind.setMind(mind);
        joinedMindRepository.save(joinedMind);
    }

    private Optional<JoinedMind> beforeJoinedCheck(Long mindId, User user) {
        Optional<JoinedMind> first = user.getJoinedMinds()
                .stream()
                .filter(jm -> Objects.equals(jm.getMind().getMindId(), mindId) && jm.getIsJoining().equals(NOT_JOIN))
                .findFirst();
        return first;
    }

    private void checkJoinMindCountMax(User user) {
        if(user.getJoinedMinds().
                stream()
                .filter(joinedMind -> joinedMind.getIsJoining() == JOIN)
                .toList().size() >= FULL_COUNT){
            throw new BadRequestException(INVALID_REQUEST);
        }
    }

    @Transactional
    public void reMindRelation(Long joinedMindId, User user) {
        List<JoinedMind> joinedMinds = user.getJoinedMinds();
        JoinedMind verifiedJoinedMind = findVerifiedJoinedMind(joinedMindId);
        verifiedJoinedMind.setIsJoining(JOIN);
        joinedMinds.add(verifiedJoinedMind);
        user.editJoinedMinds(joinedMinds);
        userRepository.save(user);
    }
    @Transactional
    public void exitMindRelation(Long mindId,User user) {
        JoinedMind joinedMind = checkUserHaveJoinedMind(mindId, user);
        List<JoinedMind> joinedMinds = user.getJoinedMinds();
        joinedMinds.remove(joinedMind);
        joinedMind.setIsJoining(NOT_JOIN);
        joinedMinds.add(joinedMind);
        user.editJoinedMinds(joinedMinds);
        joinedMindRepository.save(joinedMind);
        userRepository.save(user);
    }

    private JoinedMind checkUserHaveJoinedMind(Long mindId, User user) {
        return user.getJoinedMinds()
                .stream()
                .filter(jm -> Objects.equals(jm.getMind().getMindId(), mindId) && jm.getIsJoining() == JOIN)
                .findFirst()
                .orElseThrow(() -> new BadRequestException(NOT_JOIN_MIND));
    }

    private void checkAlreadyJoined(Long mindId, User user) {
        if(user.getJoinedMinds().isEmpty()) return;
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
    private JoinedMind findVerifiedJoinedMind(Long joinedMindId) {
        Optional<JoinedMind> findJoinedMindById = joinedMindRepository.findById(joinedMindId);
        return findJoinedMindById.orElseThrow(() ->
                new BadRequestException(NOT_FOUND_JOINED_MIND_ID));
    }

    public void reMind(Long mindId, User user) {
        JoinedMind joinedMind = checkUserHaveJoinedMind(mindId, user);
        if(!joinedMind.getKeepJoin()) throw new BadRequestException(INVALID_REQUEST);
        joinedMind.setKeepJoin(false);
        joinedMindRepository.save(joinedMind);
    }
}
