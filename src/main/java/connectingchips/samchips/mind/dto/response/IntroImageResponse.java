package connectingchips.samchips.mind.dto.response;

import connectingchips.samchips.mind.entity.Mind;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class IntroImageResponse {


    private final String introImage;

    public static IntroImageResponse of(Mind mind){
        return new IntroImageResponse(mind.getIntroImage());
    }
}
