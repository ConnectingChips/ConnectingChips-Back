package connectingchips.samchips.utils;

import connectingchips.samchips.user.domain.User;
import connectingchips.samchips.user.domain.UserAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityUtils {

    /* 현재 사용자 정보 반환 */
    public static User getLoginUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserAdapter userAdapter = (UserAdapter) authentication.getPrincipal();

        // 로그인 상태면 User 반환, 비로그인 상태면 null 반환
        return userAdapter == null ? null : userAdapter.getUser();
    }

    /* 현재 사용자의 UserName 반환 */
    public static String getLoginUserName(){
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        // 로그인 상태면 User의 accountId 반환
        if(principal instanceof UserAdapter){
            return ((UserAdapter) principal).getUsername();
        }
        // 비로그인 상태면 anonymousUser 반환
        else{
            return principal.toString();
        }
    }
}
