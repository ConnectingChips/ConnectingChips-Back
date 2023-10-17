package connectingchips.samchips.mind.mindtype.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateMindTypeRequest {

    private Long mindTypeId;
    private String name;

}
