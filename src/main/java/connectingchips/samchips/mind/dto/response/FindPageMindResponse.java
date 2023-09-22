package connectingchips.samchips.mind.dto.response;

import connectingchips.samchips.mind.entity.Mind;
import lombok.Getter;
import lombok.RequiredArgsConstructor;



@Getter
@RequiredArgsConstructor
public class FindPageMindResponse {
    private final Long mindId;
    private final String mindTypeName;
    private final String name;
    private final Integer userCount;
    private final String introduce;
    private final String writeFormat;
    private final Boolean isDoneToday;
    private final Integer count;
    public static FindPageMindResponse of(final Mind mind,boolean isDoneToday,Integer count,Integer joinMindSize){
        return new FindPageMindResponse(
                mind.getMindId(),
                mind.getMindType().getName(),
                mind.getName(),
                joinMindSize,
                mind.getIntroduce(),
                mind.getWriteFormat(),
                isDoneToday,
                count
        );
    }


}
