package connectingchips.samchips.mind.dto;

import connectingchips.samchips.mind.dto.response.MindTypeMindResponse;
import connectingchips.samchips.mind.entity.MindType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@RequiredArgsConstructor
public class AddMindResponse {
    private final Long mindTypeId;
    private final String name;
    private final List<MindTypeMindResponse> mindInfo;

    public static AddMindResponse of(MindType mindType) {
        return new AddMindResponse(
                mindType.getMindTypeId(),
                mindType.getName(),
                mindType.getMinds().stream()
                        .map(MindTypeMindResponse::of)
                        .toList());
    }
}
