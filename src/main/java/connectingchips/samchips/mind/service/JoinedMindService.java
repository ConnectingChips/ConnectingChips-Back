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

    private final JoinedMindRepository joinedMindRepository;
    private final JoinedMindRepositoryImpl joinedMindRepositoryImpl;
    private final UserRepository userRepository;
    private final MindRepository mindRepository;
    private final ValidateJoinedMind validateJoinedMind;

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
        List<JoinedMind> joinedMinds = getJoinedMinds(user);

        //첫 작심인 경우 -> 새로 작심 연결
        if(isFirstJoinedMind(mindId, joinedMinds)) {
            createJoinedMind(mindId, user);
            return;
        }

        JoinedMind beforeJoinedMind = findJoinedMindByIsJoining(mindId, joinedMinds, NOT_JOIN); //예전 참여했던 작심 가져오기
        beforeJoinedMind.updateIsJoining(JOIN);
        //makeJoinedMindRelation(beforeJoinedMind);
    }

    @Transactional
    public void exitMindRelation(Long mindId, User user) {
        List<JoinedMind> joinedMinds = getJoinedMinds(user);
        JoinedMind nowJoinedMind = findJoinedMindByIsJoining(mindId, joinedMinds, JOIN); //참여 중인 작심 가져옴
        //(1) joinedMind 변경 (참여 중이지 않은 작심으로 변경)
        nowJoinedMind.updateIsJoining(NOT_JOIN);
        //(3) 변경된 JoinedMinds 를 User Entity 에 새로 업데이트 후 저장
        //user.updateJoinedMinds(joinedMinds);
    }

    //user 엔티티에 있는 joinedMinds 를 유효성 검사 후 반환
    private List<JoinedMind> getJoinedMinds(User user) {
        List<JoinedMind> joinedMinds = user.getJoinedMinds();
        validateJoinedMind.validateJoinedMinds(joinedMinds, user);
        return joinedMinds;
    }

    private boolean isFirstJoinedMind(Long mindId, List<JoinedMind> joinedMinds) {
        return joinedMinds.stream().noneMatch(joinedMind -> joinedMind.getJoinedMindId().equals(mindId));
    }

    @Transactional
    public void makeJoinedMindRelation(JoinedMind beforeJoinedMind) {
        //(1) Joining 변경 (2) 해당 User의 JoinedMind로 변경
        beforeJoinedMind.updateIsJoining(JOIN);
        //user.updateJoinedMinds(joinedMinds);
    }

    private JoinedMind findJoinedMindByIsJoining(Long mindId, List<JoinedMind> joinedMinds, int joining) {
        return joinedMinds.stream()
                .filter(joinedMind -> joinedMind.getJoinedMindId().equals(mindId) && isJoining(joinedMind, joining))
                .findFirst()
                .orElseThrow(() -> new BadRequestException(ALREADY_JOIN_MIND));
    }

    private boolean isJoining(JoinedMind joinedMind, int joining) {
        return joinedMind.getIsJoining().equals(joining);
    }

    public JoinedMind createJoinedMind(Long mindId, User user) {
        Mind mind = findVerifiedMind(mindId);
        JoinedMind joinedMind = JoinedMind.builder().mind(mind).user(user).build();
        return joinedMindRepository.save(joinedMind);
    }

    private Mind findVerifiedMind(Long mindId) {
        return mindRepository.findById(mindId)
                .orElseThrow(() -> new BadRequestException(NOT_FOUND_MIND_ID));
    }

    public void updateKeepJoin(Long mindId, User user) {
        List<JoinedMind> joinedMinds = getJoinedMinds(user);
        JoinedMind joinedMind = findJoinedMindByIsJoining(mindId, joinedMinds, JOIN);

        if(!joinedMind.getKeepJoin()) throw new BadRequestException(INVALID_REQUEST);

        joinedMind.updateKeepJoin(false);
    }
    public void resetCountAndUpdateKeepJoin(){
        joinedMindRepositoryImpl.resetCountAndUpdateKeepJoin();
    }
}
