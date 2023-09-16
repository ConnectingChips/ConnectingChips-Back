package connectingchips.samchips.user.oauth.kakao.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record KakaoUserResponse(
        Long id,
        KakaoAccount kakaoAccount
) {

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
