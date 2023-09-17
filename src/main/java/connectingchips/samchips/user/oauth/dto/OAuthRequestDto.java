package connectingchips.samchips.user.oauth.dto;

import connectingchips.samchips.user.domain.SocialType;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class OAuthRequestDto {

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Login{

        @NotBlank(message = "SocialType은 필수 입력 값입니다.")
        private String socialType;

        @NotBlank(message = "AuthCode는 필수 입력 값입니다.")
        private String authCode;
    }
}
