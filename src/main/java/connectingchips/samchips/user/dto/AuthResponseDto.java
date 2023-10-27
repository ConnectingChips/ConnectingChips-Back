package connectingchips.samchips.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

public class AuthResponseDto {

    @Getter
    @AllArgsConstructor
    public static class Token{

        private String accessToken;
        private String refreshToken;
    }

    @Getter
    @AllArgsConstructor
    public static class AccessToken{

        private String accessToken;
    }
}
