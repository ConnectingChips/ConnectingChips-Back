package connectingchips.samchips.mind.dto.response;

import connectingchips.samchips.mind.entity.Mind;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class PageImageResponse {

    private final String pageImage;

    public static PageImageResponse of(Mind mind){
        return new PageImageResponse(mind.getPageImage());
    }
}
