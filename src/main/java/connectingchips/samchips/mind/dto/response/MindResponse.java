package connectingchips.samchips.mind.dto.response;

import connectingchips.samchips.mind.entity.Mind;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class MindResponse {

    private final Long mindId;
    private final String name;
    private final String introduce;
    private final String writeFormat;
    private final String introImage;
    private final String pageImage;
    private final String totalListImage;
    private final String myListImage;

    public static MindResponse of(Mind mind){
        return new MindResponse(
                mind.getMindId(),
                mind.getName(),
                mind.getIntroduce(),
                mind.getWriteFormat(),
                mind.getIntroImage(),
                mind.getPageImage(),
                mind.getTotalListImage(),
                mind.getMyListImage()
        );
    }
}
