package connectingchips.samchips.mindtype.dto;

import connectingchips.samchips.mind.entity.Mind;
import connectingchips.samchips.mindtype.entity.MindType;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.util.List;


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
