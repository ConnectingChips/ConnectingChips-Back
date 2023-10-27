package connectingchips.samchips.mind.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CheckMindResponse {

    private boolean isDoneToday;

    @Builder
    public CheckMindResponse(boolean isDoneToday) {
        this.isDoneToday = isDoneToday;
    }
}
