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
import java.util.Optional;

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
        Optional<JoinedMind> first = beforeJoinedCheck(mindId, user); //이전에 참여한 적이 있는지 확인

        if(first.isPresent()) { //이전에 참여한 전적이 있으면
            reMindRelation(first.get().getJoinedMindId(), user); //다시 연결맺기
            return;
        }

        checkAlreadyJoined(mindId, user);
        checkJoinMindCountMax(user);
        createJoinedMind(mind, user);
    }

    public JoinedMind createJoinedMind(Mind mind, User user) {
        JoinedMind joinedMind = JoinedMind.builder().mind(mind).user(user).build();
        return joinedMindRepository.save(joinedMind);
    }

    //user로 들어온 user가
    private Optional<JoinedMind> beforeJoinedCheck(Long mindId, User user) {
        Optional<JoinedMind> first = user.getJoinedMinds()
                .stream()
                .filter(joinedMind -> Objects.equals(joinedMind.getMind().getMindId(), mindId) && joinedMind.getIsJoining().equals(NOT_JOIN))
                .findFirst();
        return first;
    }

    private void checkJoinMindCountMax(User user) {
        // 권한에 따라 참여 가능한 작심 개수 조정
        int maxCount = FULL_COUNT;
        if(user.getRoles().contains("ROLE_ADMIN"))
            maxCount = ADMIN_FULL_COUNT;

        if(user.getJoinedMinds().
                stream()
                .filter(joinedMind -> joinedMind.getIsJoining() == JOIN)
                .toList().size() >= maxCount){
            throw new BadRequestException(INVALID_REQUEST);
        }
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

    private void checkAlreadyJoined(Long mindId, User user) {
        if(user.getJoinedMinds().isEmpty()) return;
        Optional<JoinedMind> first = user.getJoinedMinds().stream()
                .filter(jm -> jm.getMind().getMindId().equals(mindId) && jm.getIsJoining().equals(JOIN))
                .findFirst();
        if(first.isPresent()) throw new BadRequestException(ALREADY_JOIN_MIND);
    }
    private Mind findVerifiedMind(Long mindId) {
        return mindRepository.findById(mindId)
                .orElseThrow(() -> new BadRequestException(NOT_FOUND_MIND_ID));
    }
    private JoinedMind findVerifiedJoinedMind(Long joinedMindId) {
        return joinedMindRepository.findById(joinedMindId)
                .orElseThrow(() -> new BadRequestException(NOT_FOUND_JOINED_MIND_ID));
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
