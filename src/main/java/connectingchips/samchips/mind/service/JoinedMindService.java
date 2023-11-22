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
import java.util.function.Predicate;

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

    /**
     *
     * beforeJoinedCheck는 이전에 작심을 참여한 적이 있는지 확인하는 여부입니다
     *
     * 만약에 이전에 작심에 참여한 적 있다면 유저 내부에 해당 JoinedMind가 존재하기 때문에
     * 새로 객체를 만들어줄 필요없이 참여여부(isJoining)을 true로 변경만 해주면되고 (reMindRelation)
     *
     * 없을 경우에는 새로 JoinedMind객체를 생성해 유저 데이터에 저장해주기위해서 만들었어여
     * 그래서 해당 유저 내부에 JoinedMind의 mindId중 현재 가입하려는 mindId와 동일한 값이 있는지 확인하는 메서드입니다
     */

    @Transactional
    public void makeMindRelation(Long mindId, User user) {
        Mind mind = findVerifiedMind(mindId); //작심을 찾아옴

        if(isFirstJoinedMind(mindId, user)) { //첫번째 작심이면
            checkAlreadyJoined(mindId, user);
            checkJoinMindCountMax(user);
            createJoinedMind(mind, user);
        }

        reMindRelation(first.get().getJoinedMindId(), user); //다시 연결맺기
    }

    public JoinedMind createJoinedMind(Mind mind, User user) {
        JoinedMind joinedMind = JoinedMind.builder().mind(mind).user(user).build();
        return joinedMindRepository.save(joinedMind);
    }

    //첫번째 작심인지 체크하는 메서드
    private boolean isFirstJoinedMind(Long mindId, User user) {
        return user.getJoinedMinds().stream()
                .noneMatch(joinedMind -> joinedMind.getJoinedMindId().equals(mindId));
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
