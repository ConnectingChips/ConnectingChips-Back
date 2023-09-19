package connectingchips.samchips.mind.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CreateMindRequest {

    private String name;
    private String introduce;
    private String writeFormat;
    private Long mindTypeId;


}
