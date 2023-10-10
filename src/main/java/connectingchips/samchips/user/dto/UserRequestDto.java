package connectingchips.samchips.user.dto;

import connectingchips.samchips.user.domain.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class UserRequestDto {

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Signup{

        @NotBlank(message = "아이디는 필수 입력 값입니다.")
        private String accountId;

        @NotBlank(message = "비밀번호는 필수 입력 값입니다.")
        private String password;

        @Email(message = "이메일 형식에 맞지 않습니다.")
        @NotBlank(message = "이메일은 필수 입력 값입니다.")
        private String email;

        @NotBlank(message = "닉네임은 필수 입력 값입니다.")
        private String nickname;

        public User toEntity(){
            return User.builder()
                    .accountId(accountId)
                    .password(password)
                    .email(email)
                    .nickname(nickname)
                    .build();
        }
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Login{

        @NotBlank(message = "아이디는 필수 입력 값입니다.")
        private String accountId;

        @NotBlank(message = "비밀번호는 필수 입력 값입니다.")
        private String password;
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Update{

        @NotBlank(message = "닉네임은 필수 입력 값입니다.")
        private String nickname;
    }
}
