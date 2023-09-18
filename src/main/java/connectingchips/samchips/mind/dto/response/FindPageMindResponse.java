package connectingchips.samchips.mind.dto.response;

import connectingchips.samchips.mind.entity.Mind;
import connectingchips.samchips.user.domain.User;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class FindPageMindResponse {
    private final Long mindId;
    private final String mindTypeName;
    private final String name;
    private final Integer userCount;
    private final String writeFormat;
    private final String pageImage;
    private final Boolean isDoneToday;
    private final Integer count;
    public static FindPageMindResponse of(final Mind mind,boolean isDoneToday,Integer count){
        return new FindPageMindResponse(
                mind.getMindId(),
                mind.getMindType().getName(),
                mind.getName(),
                mind.getJoinedMinds().size(),
                mind.getWriteFormat(),
                mind.getPageImage(),
                isDoneToday,
                count
        );
    }

}
