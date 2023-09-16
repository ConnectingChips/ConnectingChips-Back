package connectingchips.samchips.user.domain;

import java.util.Locale;

public enum SocialType {
    KAKAO;

    public static SocialType fronName(String type){
        return SocialType.valueOf(type.toUpperCase(Locale.ENGLISH));
    }
}
