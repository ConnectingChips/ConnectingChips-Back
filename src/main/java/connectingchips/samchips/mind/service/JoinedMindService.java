package connectingchips.samchips.mind.service;

import connectingchips.samchips.global.exception.BadRequestException;
import connectingchips.samchips.mind.dto.JoinCheckResponse;
import connectingchips.samchips.mind.entity.JoinedMind;
import connectingchips.samchips.mind.repository.JoinedMindRepository;
import connectingchips.samchips.mind.repository.JoinedMindRepositoryImpl;
import connectingchips.samchips.mind.entity.Mind;
import connectingchips.samchips.mind.repository.MindRepository;
import connectingchips.samchips.user.domain.User;
import connectingchips.samchips.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

import static connectingchips.samchips.global.exception.CommonErrorCode.*;

@Service
@RequiredArgsConstructor
public class JoinedMindService {

    public static final int JOIN = 1;
    public static final int NOT_JOIN = 0;
    public static final int ADMIN_FULL_COUNT = Integer.MAX_VALUE;
    public static final int FULL_COUNT = 3;

    private final JoinedMindRepository joinedMindRepository;
    private final JoinedMindRepositoryImpl joinedMindRepositoryImpl;
    private final UserRepository userRepository;
    private final MindRepository mindRepository;

    @Transactional
    public JoinCheckResponse JoinCheck(Long mindId,User loginUser) { //작심에 참여하고 있는지 확인
        return JoinCheckResponse.of(checkJoinedMind(mindId, loginUser));
    }

    private static boolean checkJoinedMind(Long mindId, User loginUser) {
        return loginUser.getJoinedMinds()
                .stream()
                .anyMatch(joinedMind -> Objects.equals(joinedMind.getMind().getMindId(), mindId) && joinedMind.getIsJoining() == JOIN);
    }

    @Transactional
    public void makeMindRelation(Long mindId, User user) {
        Mind mind = findVerifiedMind(mindId); //작심을 찾아옴
        List<JoinedMind> joinedMinds = user.getJoinedMinds();
        if(isFirstJoinedMind(mindId, joinedMinds)) {
            createJoinedMind(mind, user);
            return;
        }

        JoinedMind beforeJoinedMind = findJoinedMindByJoinedMinds(mindId, joinedMinds); //예전 참여했던 작심 가져오기
        validateIsJoining(beforeJoinedMind); //이미 참여중이면, 에러 발생

        //작심 개수가 초과하는지 확인
        validateJoinedMindCount(joinedMinds, getCountMaxByRole(user));

    }

    private JoinedMind findJoinedMindByJoinedMinds(Long mindId, List<JoinedMind> joinedMinds) {
        return joinedMinds.stream()
                .filter(joinedMind -> joinedMind.getJoinedMindId().equals(mindId))
                .findFirst()
                .orElseThrow(() -> new BadRequestException(INVALID_REQUEST));
    }

    private Mind findAlreadyMindByJoinedMinds(Long mindId, List<JoinedMind> joinedMinds) {
        return joinedMinds.stream()
                .filter(joinedMind -> joinedMind.getJoinedMindId().equals(mindId))
                .findFirst()
                .map(JoinedMind::getMind)
                .orElseThrow(() -> new BadRequestException(INVALID_REQUEST));
    }

    public JoinedMind createJoinedMind(Mind mind, User user) {
        JoinedMind joinedMind = JoinedMind.builder().mind(mind).user(user).build();
        return joinedMindRepository.save(joinedMind);
    }

    private boolean isFirstJoinedMind(Long mindId, List<JoinedMind> joinedMinds) {
        return joinedMinds.stream()
                .noneMatch(joinedMind -> joinedMind.getJoinedMindId().equals(mindId));
    }

    private void validateIsJoining(JoinedMind joinedMind) {
        if(isJoined(joinedMind)) throw new BadRequestException(ALREADY_JOIN_MIND);
    }

    private boolean isJoined(JoinedMind joinedMind) {
        return joinedMind.getIsJoining().equals(JOIN);
    }

    private int getCountMaxByRole(User user) {
        // 권한에 따라 참여 가능한 작심 개수 조정
        if(user.getRoles().contains("ROLE_ADMIN"))
            return ADMIN_FULL_COUNT;
        return FULL_COUNT;
    }

    //작심참여 갯수를 초과했는지 판단
    private void validateJoinedMindCount(List<JoinedMind> joinedMinds, int maxCount) {
        if(isJoinedMindCountMax(joinedMinds, maxCount)) throw new BadRequestException(INVALID_REQUEST);
    }

    private boolean isJoinedMindCountMax(List<JoinedMind> joinedMinds, int maxCount) {
        return joinedMinds.stream()
                .filter(this::isJoined)
                .count() >= maxCount;
    }

    @Transactional
    public void reMindRelation(Long joinedMindId, User user) {
        List<JoinedMind> joinedMinds = user.getJoinedMinds();
        JoinedMind verifiedJoinedMind = findVerifiedJoinedMind(joinedMindId);
        verifiedJoinedMind.updateIsJoining(JOIN);
        joinedMinds.add(verifiedJoinedMind);
        user.editJoinedMinds(joinedMinds);
        userRepository.save(user);
    }
    @Transactional
    public void exitMindRelation(Long mindId,User user) {
        JoinedMind joinedMind = checkUserHaveJoinedMind(mindId, user);
        List<JoinedMind> joinedMinds = user.getJoinedMinds();
        joinedMinds.remove(joinedMind);
        joinedMind.updateIsJoining(NOT_JOIN);
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

    private Mind findVerifiedMind(Long mindId) {
        return mindRepository.findById(mindId)
                .orElseThrow(() -> new BadRequestException(NOT_FOUND_MIND_ID));
    }
    private JoinedMind findVerifiedJoinedMind(Long joinedMindId) {
        return joinedMindRepository.findById(joinedMindId)
                .orElseThrow(() -> new BadRequestException(NOT_FOUND_JOINED_MIND_ID));
    }

    private JoinedMind findVerifiedJoinedMind(Long mindId, User user) {
        return user.getJoinedMinds()
                .stream()
                .filter(joinedMind -> Objects.equals(joinedMind.getMind().getMindId(), mindId)
                        && joinedMind.getIsJoining().equals(NOT_JOIN))
                .findFirst().orElseThrow(() -> new BadRequestException(NOT_FOUND_MIND_ID));
    }

    public void updateKeepJoin(Long mindId, User user) {
        JoinedMind joinedMind = checkUserHaveJoinedMind(mindId, user);

        if(!joinedMind.getKeepJoin()) throw new BadRequestException(INVALID_REQUEST);

        joinedMind.setKeepJoin(false);
        joinedMindRepository.save(joinedMind);
    }
    public void resetCountAndUpdateKeepJoin(){
        joinedMindRepositoryImpl.resetCountAndUpdateKeepJoin();
    }
}
