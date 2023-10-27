package connectingchips.samchips.user.oauth.kakao.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import connectingchips.samchips.user.domain.SocialType;
import connectingchips.samchips.user.domain.User;

import java.util.UUID;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record KakaoUserResponse(
        Long id,
        KakaoAccount kakaoAccount
) {
    public User toEntity(){
        String randomName = UUID.randomUUID().toString();
        String nickName = "chips_" + randomName.substring(12);

        return User.builder()
                .accountId(id.toString())
                .password("kakao" + id)
                .nickname(nickName)
                .email(kakaoAccount.email)
                .gender(kakaoAccount.gender)
                .ageRange(kakaoAccount.ageRange)
                .socialType(SocialType.KAKAO)
                .build();
    }

    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public record KakaoAccount(
//            boolean emailNeedsAgreement,    // 사용자 동의 시 카카오계정 대표 이메일 제공 가능
//            boolean isEmailValid,           // 이메일 유효 여부
//            boolean isEmailVerified,        // 이메일 인증 여부
            String email,                   // 카카오계정 대표 이메일
//            boolean ageRangeNeedsAgreement, // 사용자 동의 시 연령대 제공 가능
            String ageRange,                // 연령대
//            boolean genderNeedsAgreement,   // 사용자 동의 시 성별 제공 가능
            String gender                   // 성별
    ){
    }
}
