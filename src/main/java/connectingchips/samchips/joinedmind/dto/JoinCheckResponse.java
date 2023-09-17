package connectingchips.samchips.joinedmind.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class JoinCheckResponse {

    private final Boolean isDoneToday;

    public static JoinCheckResponse of(boolean isJoining){
        return new JoinCheckResponse(isJoining);
    }
}
