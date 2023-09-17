package connectingchips.samchips.user.oauth;

import connectingchips.samchips.commons.dto.BasicResponse;
import connectingchips.samchips.commons.dto.DataResponse;
import connectingchips.samchips.user.domain.SocialType;
import connectingchips.samchips.user.dto.AuthResponseDto;
import connectingchips.samchips.user.oauth.dto.OAuthRequestDto;
import jakarta.persistence.Basic;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.hibernate.exception.DataException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/oauth")
public class OAuthController {

    private final OAuthService oAuthService;

    @GetMapping("/{socialType}")
    public BasicResponse redirectAuthCodeRequestUrl(@PathVariable SocialType socialType, HttpServletResponse response) throws IOException {
        String redirectUrl = oAuthService.getAuthCodeRequestUrl(socialType);
        response.sendRedirect(redirectUrl);

        return BasicResponse.of(HttpStatus.OK);
    }

    @PostMapping("/login")
    public DataResponse<AuthResponseDto.AccessToken> socialLogin(@RequestBody OAuthRequestDto.Login loginDto, HttpServletResponse response){
        System.out.println("socialLogin");
        AuthResponseDto.Token token = oAuthService.socialLogin(loginDto);
        AuthResponseDto.AccessToken accessToken = new AuthResponseDto.AccessToken(token.getAccessToken());

        Cookie cookie = new Cookie("refreshToken", token.getRefreshToken());
        cookie.setHttpOnly(true);
//        cookie.setSecure(true);
        cookie.setPath("/");
        cookie.setMaxAge(24 * 60 * 60);

        response.addCookie(cookie);

        return DataResponse.of(accessToken);
    }
}
