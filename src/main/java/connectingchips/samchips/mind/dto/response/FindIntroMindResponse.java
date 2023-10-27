package connectingchips.samchips.mind.dto.response;

import connectingchips.samchips.mind.entity.Mind;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class FindIntroMindResponse {

    public static final int NOT_LOGIN = -1;
    private final Long mindId;
    private final String mindTypeName;
    private final String name;
    private final String introduce;
    private final Integer userCount;
    private final String writeFormat;
    private final Integer canJoin;

    public static FindIntroMindResponse of(final Mind mind, Integer canJoin,Integer joinMindSize){
        return new FindIntroMindResponse(
                mind.getMindId(),
                mind.getMindType().getName(),
                mind.getName(),
                mind.getIntroduce(),
                joinMindSize,
                mind.getWriteFormat(),
                canJoin
                );
    }

    public static FindIntroMindResponse of(final Mind mind,Integer joinMindSize){
        return new FindIntroMindResponse(
                mind.getMindId(),
                mind.getMindType().getName(),
                mind.getName(),
                mind.getIntroduce(),
                joinMindSize,
                mind.getWriteFormat(),
                NOT_LOGIN
                );
    }
}
