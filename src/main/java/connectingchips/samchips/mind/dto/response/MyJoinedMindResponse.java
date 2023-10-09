package connectingchips.samchips.mind.dto.response;

import connectingchips.samchips.joinedmind.entity.JoinedMind;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import static connectingchips.samchips.joinedmind.service.JoinedMindService.NOT_JOIN;
import static connectingchips.samchips.mind.service.MindService.CAN_JOIN;

@Getter
@RequiredArgsConstructor
public class MyJoinedMindResponse {
    private final Long mindId;
    private final String name;
    private final Integer canJoin;
    private final Integer boardCount;

    public static MyJoinedMindResponse of(JoinedMind joinedMind, Integer boardCount) {
        return new MyJoinedMindResponse(
                joinedMind.getMind().getMindId(),
                joinedMind.getMind().getName(),
                checkCanJoin(joinedMind),
                boardCount
        );
    }
    private static Integer checkCanJoin(JoinedMind joinedMind){
        if(joinedMind.getIsJoining() == NOT_JOIN) return CAN_JOIN;
        else return NOT_JOIN;
    }
}
