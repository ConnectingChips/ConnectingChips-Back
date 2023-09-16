package connectingchips.samchips.user.domain;

import java.util.Locale;

public enum SocialType {
    SAMCHIPS,
    KAKAO;

    public static SocialType fromName(String type){
        return SocialType.valueOf(type.toUpperCase(Locale.ENGLISH));
    }
}
