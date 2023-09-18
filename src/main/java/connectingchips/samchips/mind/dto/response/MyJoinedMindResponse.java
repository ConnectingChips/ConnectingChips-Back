package connectingchips.samchips.mind.dto.response;

import connectingchips.samchips.mind.entity.Mind;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class MyJoinedMindResponse {
    private final Long mindId;
    private final String name;
    private final Integer canJoin;

    public static MyJoinedMindResponse of(Mind mind, Integer canJoin) {
        return new MyJoinedMindResponse(
                mind.getMindId(),
                mind.getMindType().getName(),
                canJoin
        );
    }
}
