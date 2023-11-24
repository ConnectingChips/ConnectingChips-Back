package connectingchips.samchips.mind.service;

import connectingchips.samchips.global.exception.BadRequestException;
import connectingchips.samchips.mind.entity.JoinedMind;
import connectingchips.samchips.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

import static connectingchips.samchips.global.exception.CommonErrorCode.ALREADY_JOIN_COUNT_FULL;

@Service
@RequiredArgsConstructor
public class ValidateJoinedMind {
    public static final int JOIN = 1;
    public static final int NOT_JOIN = 0;

    public static final int ADMIN_FULL_COUNT = Integer.MAX_VALUE;
    public static final int FULL_COUNT = 3;

    public void validateJoinedMinds(List<JoinedMind> joinedMinds, User user) {
        int countMax = getCountMaxByRole(user);
        validateJoinedMindCount(joinedMinds, countMax);
    }

    //작심 개수가 초과하는지 확인
    private void validateJoinedMindCount(List<JoinedMind> joinedMinds, int maxCount) {
        if(isJoinedMindCountMax(joinedMinds, maxCount)) throw new BadRequestException(ALREADY_JOIN_COUNT_FULL);
    }

    //작심참여 갯수를 초과 했는지 판단
    private boolean isJoinedMindCountMax(List<JoinedMind> joinedMinds, int maxCount) {
        return joinedMinds.stream().filter(this::isJoined).count() >= maxCount;
    }

    private int getCountMaxByRole(User user) {
        if(user.getRoles().contains("ROLE_ADMIN")){ // 권한에 따라 참여 가능한 작심 개수 조정
            return ADMIN_FULL_COUNT;
        }
        return FULL_COUNT;
    }

    private boolean isJoined(JoinedMind joinedMind) {
        return joinedMind.getIsJoining().equals(JOIN);
    }
}
