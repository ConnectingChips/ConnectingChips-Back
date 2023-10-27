package connectingchips.samchips.mind.mindtype.dto;

import connectingchips.samchips.mind.mindtype.entity.MindType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;


@Getter
@RequiredArgsConstructor
public class MindTypeResponse {

    private final Long mindTypeId;
    private final String name;



    public static MindTypeResponse of(MindType mindType) {
        return new MindTypeResponse(mindType.getMindTypeId(),
                mindType.getName());
    }
}
