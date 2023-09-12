package connectingchips.samchips.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

public class AuthResponseDto {

    @Getter
    @AllArgsConstructor
    public static class Token{

        private String token;
    }
}
