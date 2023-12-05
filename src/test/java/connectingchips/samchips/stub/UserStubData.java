package connectingchips.samchips.stub;

import connectingchips.samchips.global.email.dto.EmailRequestDto;
import connectingchips.samchips.user.domain.User;
import connectingchips.samchips.user.domain.UserAdapter;
import connectingchips.samchips.user.dto.UserRequestDto;

public class UserStubData {

    public User createUser1(){
        User user = User.builder()
                .accountId("test")
                .password("test")
                .email("test@test.com")
                .nickname("test")
                .build();
        user.setId(1l);

        return user;
    }

    public User createUser2(){
        User user = User.builder()
                .accountId("test2")
                .password("test2")
                .email("test2@test.com")
                .nickname("test2")
                .build();
        user.setId(2l);

        return user;
    }

    public UserAdapter createUserAdapter(){
        return new UserAdapter(createUser1());
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

    public EmailRequestDto.Authentication createEmailAuthenticationDto(){
        return new EmailRequestDto.Authentication("test@test.com");
    }
}
