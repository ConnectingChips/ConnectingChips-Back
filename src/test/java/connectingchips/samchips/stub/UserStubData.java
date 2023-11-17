package connectingchips.samchips.stub;

import connectingchips.samchips.user.domain.SocialType;
import connectingchips.samchips.user.domain.User;

public class UserStubData {
    public User createUser(){
        return  User.builder()
                .email("test@test.com")
                .password("test123456")
                .nickname("테스트닉네임")
                .accountId("test12345")
                .ageRange("17")
                .gender("남")
                .socialType(SocialType.SAMCHIPS)
                .profileImage("test.png")
                .build();
    }
}
