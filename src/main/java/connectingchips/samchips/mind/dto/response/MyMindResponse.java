package connectingchips.samchips.mind.dto.response;


import connectingchips.samchips.mind.entity.Mind;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class MyMindResponse {

    private final Long mindId;
    private final String mindTypeName;
    private final String name;
    private final Integer count;
    private final Integer boardCount;
    private final String myListImage;
    private final Boolean isDoneToday;
    private final Boolean keepJoin;

    public static MyMindResponse of(Mind mind, Integer count, Integer boardCount,Boolean isDoneToday,Boolean keepJoin) {
        return new MyMindResponse(
                mind.getMindId(),
                mind.getMindType().getName(),
                mind.getName(),
                count,
                boardCount,
                mind.getMyListImage(),
                isDoneToday,
                keepJoin
        );
    }
}
