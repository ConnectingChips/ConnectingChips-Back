package connectingchips.samchips.mind.dto.service;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CheckAllMindOutput {

    private Long joinedMindId;
    private boolean isDoneToday;

    @Builder
    public CheckAllMindOutput(Long joinedMindId, boolean isDoneToday) {
        this.joinedMindId = joinedMindId;
        this.isDoneToday = isDoneToday;
    }
}
