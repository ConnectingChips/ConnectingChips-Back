package connectingchips.samchips.mindtype.dto;

import connectingchips.samchips.mind.entity.Mind;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;


@Getter
@NoArgsConstructor
public class MindTypeResponse {

    private Long mindTypeId;
    private String name;

    private List<Mind> minds;


    @Builder
    public MindTypeResponse(Long mindTypeId, String name, List<Mind> minds) {
        this.mindTypeId = mindTypeId;
        this.name = name;
        this.minds = minds;
    }
}