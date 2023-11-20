package connectingchips.samchips.board.stub;

import connectingchips.samchips.user.domain.User;
import connectingchips.samchips.user.dto.UserRequestDto;

public class UserStubData {

    public User createUser(){
        return createSignupDto().toEntity();
    }

    public UserRequestDto.Signup createSignupDto(){
        return UserRequestDto.Signup.builder()
                .accountId("test")
                .password("test")
                .email("test@test.com")
                .nickname("test")
                .build();
    }

    public UserRequestDto.Update createUpdateDto(){
        return new UserRequestDto.Update("update test");
    }
}
