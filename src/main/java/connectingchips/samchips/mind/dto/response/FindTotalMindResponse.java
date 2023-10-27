package connectingchips.samchips.mind.dto.response;

import connectingchips.samchips.mind.entity.Mind;
import lombok.Getter;
import lombok.RequiredArgsConstructor;


@Getter
@RequiredArgsConstructor
public class FindTotalMindResponse {

    private final Long mindId;
    private final String mindTypeName;
    private final String name;
    private final String introduce;
    private final Integer userCount;
    private final String writeFormat;
    private final Integer canJoin;
    private final String totalListImage;


    public static FindTotalMindResponse of(Mind mind, Integer canJoin,Integer joinMindSize){
        return new FindTotalMindResponse(
                mind.getMindId(),
                mind.getMindType().getName(),
                mind.getName(),
                mind.getIntroduce(),
                joinMindSize,
                mind.getWriteFormat(),
                canJoin,
                mind.getTotalListImage()
        );
    }
}
