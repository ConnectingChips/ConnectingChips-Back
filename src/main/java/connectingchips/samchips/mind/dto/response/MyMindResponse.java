package connectingchips.samchips.mind.dto.response;

import connectingchips.samchips.mindtype.entity.MindType;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MyMindResponse {

    private Long mindId;

    private String mindTypeName;

    private String name;
    private Integer count;
    private Integer boardCount;
    private String image;
    private Boolean isDoneToday;

    @Builder
    public MyMindResponse(Long mindId, String mindTypeName, String name, Integer count, Integer boardCount, String image, Boolean isDoneToday) {
        this.mindId = mindId;
        this.mindTypeName = mindTypeName;
        this.name = name;
        this.count = count;
        this.boardCount = boardCount;
        this.image = image;
        this.isDoneToday = isDoneToday;
    }
}
