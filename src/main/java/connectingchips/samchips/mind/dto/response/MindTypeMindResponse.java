package connectingchips.samchips.mind.dto.response;

import connectingchips.samchips.mind.entity.Mind;
import lombok.Getter;
import lombok.RequiredArgsConstructor;


@Getter
@RequiredArgsConstructor
public class MindTypeMindResponse {

    private final Long mindId;
    private final String name;

    public static MindTypeMindResponse of(Mind mind) {
        return new MindTypeMindResponse(
                mind.getMindId(),
                mind.getName());
    }
}
