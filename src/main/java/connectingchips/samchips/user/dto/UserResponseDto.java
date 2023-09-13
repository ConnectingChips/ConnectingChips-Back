package connectingchips.samchips.user.dto;

import connectingchips.samchips.user.domain.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

public class UserResponseDto {

    @Getter
    @Builder
    @AllArgsConstructor
    public static class Info{

        private Long userId;
        private String nickname;
        private String profileImage;
        private Role roles;
    }
}
