package connectingchips.samchips.mind.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class JoinCheckResponse {

    private final Boolean isJoining;

    public static JoinCheckResponse of(boolean isJoining){
        return new JoinCheckResponse(isJoining);
    }
}
