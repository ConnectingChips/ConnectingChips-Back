package connectingchips.samchips.mind.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CheckAllMindResponse {

    private Long joinedMindId;
    private boolean isDoneToday;

    @Builder
    public CheckAllMindResponse(Long joinedMindId, boolean isDoneToday) {
        this.joinedMindId = joinedMindId;
        this.isDoneToday = isDoneToday;
    }
}
