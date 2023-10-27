package connectingchips.samchips.global.email.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class EmailRequestDto {

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Authentication{

        @NotBlank(message = "수신자 이메일은 필수 입력 값입니다.")
        private String toEmail;
    }
}
