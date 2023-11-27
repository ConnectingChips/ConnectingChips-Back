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

        //재작심인 경우 -> 유효성 확인 후 재 연결
        JoinedMind beforeJoinedMind = findVerifiedJoinedMindByIsJoining(mindId, joinedMinds, NOT_JOIN); //예전 참여했던 작심 가져오기
        makeReJoinedMind(joinedMinds, beforeJoinedMind, user);
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
    public void makeReJoinedMind(List<JoinedMind> joinedMinds, JoinedMind beforeJoinedMind, User user) {
        //재작심하기 위해 해줘야하는 것
        //(1) Joining 변경 (2) 해당 User의 JoinedMind로 변경
        beforeJoinedMind.updateIsJoining(JOIN);
        joinedMinds.add(beforeJoinedMind);
        user.updateJoinedMinds(joinedMinds);
        userRepository.save(user);
    }

    private JoinedMind findVerifiedJoinedMindByIsJoining(Long mindId, List<JoinedMind> joinedMinds, int joining) {
        return joinedMinds.stream()
                .filter(joinedMind -> joinedMind.getJoinedMindId().equals(mindId) && isJoining(joinedMind, joining))
                .findFirst()
                .orElseThrow(() -> new BadRequestException(ALREADY_JOIN_MIND));
    }

    private boolean isJoining(JoinedMind joinedMind, int joining) {
        return joinedMind.getIsJoining().equals(joining);
    }

    //현재 참여하고 있는지 확인
    private JoinedMind checkUserHaveJoinedMind(Long mindId, User user) {
        return user.getJoinedMinds()
                .stream()
                .filter(jm -> Objects.equals(jm.getMind().getMindId(), mindId) && jm.getIsJoining() == JOIN)
                .findFirst()
                .orElseThrow(() -> new BadRequestException(NOT_JOIN_MIND));
    }

    public JoinedMind createJoinedMind(Long mindId, User user) {
        Mind mind = findVerifiedMind(mindId);
        JoinedMind joinedMind = JoinedMind.builder().mind(mind).user(user).build();
        return joinedMindRepository.save(joinedMind);
    }

    @Transactional
    public void exitMindRelation(Long mindId, User user) {
        List<JoinedMind> joinedMinds = getJoinedMinds(user);
        JoinedMind joinedMind = findVerifiedJoinedMindByIsJoining(mindId, joinedMinds, JOIN); //참여 중인 작심 가져옴

        //(1) joinedMind 변경 (참여 중이지 않은 작심으로 변경)
        joinedMind.updateIsJoining(NOT_JOIN);

        //(2) NOT_JOIN으로 바꾼 작심을 새로 저장하고, JOIN 으로 되어있던 작심은 새로 변경 후 JoinedMind 에 새로운 데이터 저장
        joinedMinds.add(joinedMind);
        joinedMinds.remove(joinedMind); //기존에 있던 작심 제외
        joinedMindRepository.save(joinedMind);

        //(3) 변경된 JoinedMinds 를 User Entity 에 새로 업데이트 후 저장
        user.updateJoinedMinds(joinedMinds);
        userRepository.save(user);
    }



    private Mind findVerifiedMind(Long mindId) {
        return mindRepository.findById(mindId)
                .orElseThrow(() -> new BadRequestException(NOT_FOUND_MIND_ID));
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
