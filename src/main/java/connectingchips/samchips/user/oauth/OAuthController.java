package connectingchips.samchips.user.oauth;

import connectingchips.samchips.global.commons.dto.BasicResponse;
import connectingchips.samchips.global.commons.dto.DataResponse;
import connectingchips.samchips.user.domain.SocialType;
import connectingchips.samchips.user.dto.AuthResponseDto;
import connectingchips.samchips.user.oauth.client.TestApiClient;
import connectingchips.samchips.user.oauth.dto.OAuthRequestDto;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@Validated
@RequiredArgsConstructor
@RequestMapping("/oauth")
public class OAuthController {

    private final OAuthService oAuthService;
    private final TestApiClient testApiClient;

    /* 각 소셜 타입의 AuthCode 요청 Url Redirect */
    @GetMapping("/{socialType}")
    public BasicResponse redirectAuthCodeRequestUrl(@PathVariable @NotNull SocialType socialType, HttpServletResponse response) throws IOException {
        String redirectUrl = oAuthService.getAuthCodeRequestUrl(socialType);
        response.sendRedirect(redirectUrl);

        return BasicResponse.of(HttpStatus.OK);
    }

    /* 소셜 로그인 */
    @PostMapping("/login")
    public DataResponse<AuthResponseDto.AccessToken> socialLogin(@RequestBody @Valid OAuthRequestDto.Login loginDto, HttpServletResponse response){

        AuthResponseDto.Token token = oAuthService.socialLogin(loginDto);
        AuthResponseDto.AccessToken accessToken = new AuthResponseDto.AccessToken(token.getAccessToken());

        Cookie cookie = new Cookie("refreshToken", token.getRefreshToken());
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setPath("/");
        cookie.setMaxAge(24 * 60 * 60);

        response.addCookie(cookie);

        return DataResponse.of(accessToken);
    }

    @GetMapping("/test400")
    public void test400(){
        testApiClient.test400();
    }

    @GetMapping("/test500")
    public void test500(){
        testApiClient.test500();
    }
}
