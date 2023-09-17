package connectingchips.samchips.mind.dto.response;

import connectingchips.samchips.mind.entity.Mind;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class FindMindResponse {

    public static final int NOT_LOGIN = -1;
    private final Long mindId;
    private final String mindType;
    private final String name;
    private final String introduce;
    private final Integer userCount;
    private final String writeFormat;
    private final Integer canJoin;
    private final String introImage;

    public static FindMindResponse of(final Mind mind,Integer canJoin){
        return new FindMindResponse(
                mind.getMindId(),
                mind.getMindType().getName(),
                mind.getName(),
                mind.getIntroduce(),
                mind.getJoinedMinds().size(),
                mind.getWriteFormat(),
                canJoin,
                mind.getIntroImage()
                );
    }
    public static FindMindResponse of(final Mind mind){
        return new FindMindResponse(
                mind.getMindId(),
                mind.getMindType().getName(),
                mind.getName(),
                mind.getIntroduce(),
                mind.getJoinedMinds().size(),
                mind.getWriteFormat(),
                NOT_LOGIN,
                mind.getIntroImage()
                );
    }
}
