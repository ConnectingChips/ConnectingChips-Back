package connectingchips.samchips.mind.dto.response;

import connectingchips.samchips.mind.entity.Mind;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class MindExampleImageResponse {

    private final String exampleImage;

    public static MindExampleImageResponse of(Mind mind){
        return new MindExampleImageResponse(mind.getExampleImage());
    }

}
