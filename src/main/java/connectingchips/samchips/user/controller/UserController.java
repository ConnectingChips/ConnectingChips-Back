package connectingchips.samchips.user.controller;

import connectingchips.samchips.global.commons.dto.BasicResponse;
import connectingchips.samchips.global.commons.dto.DataResponse;
import connectingchips.samchips.global.email.dto.EmailRequestDto;
import connectingchips.samchips.user.domain.LoginUser;
import connectingchips.samchips.user.domain.User;
import connectingchips.samchips.user.dto.AuthResponseDto;
import connectingchips.samchips.user.dto.UserRequestDto;
import connectingchips.samchips.user.dto.UserResponseDto;
import connectingchips.samchips.user.service.AuthService;
import connectingchips.samchips.user.service.UserService;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@RestController
@Validated
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private final AuthService authService;

    /* 유저 회원가입 */
    @PostMapping
    public ResponseEntity<BasicResponse> createUser(@RequestBody @Valid UserRequestDto.Signup signupDto){
        userService.signup(signupDto);

        return new ResponseEntity<>(BasicResponse.of(HttpStatus.CREATED) ,HttpStatus.CREATED);
    }

    /* 자체 로그인 */
    @PostMapping("/login")
    public DataResponse<AuthResponseDto.AccessToken> login(@RequestBody @Valid UserRequestDto.Login loginDto, HttpServletResponse response){
        AuthResponseDto.Token token = authService.login(loginDto);

        AuthResponseDto.AccessToken accessToken = new AuthResponseDto.AccessToken(token.getAccessToken());

        Cookie cookie = new Cookie("refreshToken", token.getRefreshToken());
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setPath("/");
        cookie.setMaxAge(24 * 60 * 60);

        response.addCookie(cookie);

        return DataResponse.of(accessToken);
    }

    /* 아이디 사용 가능 여부 반환 */
    @GetMapping("/check-id")
    public DataResponse<UserResponseDto.CheckId> checkAccountId(@RequestParam @NotBlank String accountId){
        boolean isUsable = !userService.checkAccountId(accountId);
        UserResponseDto.CheckId checkIdDto = new UserResponseDto.CheckId(isUsable);

        return DataResponse.of(checkIdDto);
    }

    /* 현재 로그인 여부 반환 */
    @GetMapping("/check-login")
    public DataResponse<UserResponseDto.CheckLogin> checkLogin(@LoginUser User loginUser){
        boolean isLogin = false;
        if(loginUser != null){
            isLogin = true;
        }
        UserResponseDto.CheckLogin checkLoginDto = new UserResponseDto.CheckLogin(isLogin);

        return DataResponse.of(checkLoginDto);
    }

    // 인증 이메일 보내기
    @PostMapping("/authentication-email")
    public BasicResponse sendAuthenticationEmail(@RequestBody @Valid EmailRequestDto.Authentication authenticationDto) throws MessagingException {
        authService.sendAuthenticationEmail(authenticationDto);

        return BasicResponse.of(HttpStatus.OK);
    }

    // 이메일 인증하기 버튼
    @GetMapping("/authentication-email")
    public ModelAndView authenticationEmail(@RequestParam @NotBlank String email, @RequestParam @NotBlank String authCode){
        boolean isAuthenticated = authService.authenticationEmail(email, authCode);

        if(isAuthenticated)
            return new ModelAndView("authSuccess");
        else
            return new ModelAndView("authFail");
    }

    /* 현재 로그인한 유저 정보 반환 */
    @GetMapping
    @PreAuthorize("hasRole('USER')")
    public DataResponse<UserResponseDto.Info> getLoginUser(@LoginUser User loginUser){
        UserResponseDto.Info info = UserResponseDto.Info.builder()
                .userId(loginUser.getId())
                .nickname(loginUser.getNickname())
                .profileImage(loginUser.getProfileImage())
                .roles(loginUser.getRoles())
                .build();

        return DataResponse.of(info);
    }

    /* accessToken 재발급 */
    @GetMapping("/reissue")
    public DataResponse<AuthResponseDto.AccessToken> reissueAccessToken(@CookieValue("refreshToken") String refreshToken){
        AuthResponseDto.AccessToken accessToken = authService.reissueAccessToken(refreshToken);

        return DataResponse.of(accessToken);
    }

    /* 로그아웃 */
    @PutMapping("/logout")
    @PreAuthorize("hasRole('USER')")
    public BasicResponse logout(@LoginUser User loginUser, HttpServletRequest request
            , HttpServletResponse response){
        authService.logout(loginUser.getId(), request);

        // 로그아웃 시에 refreshToken을 가지고 있는 cookie 제거
        Cookie cookie = new Cookie("refreshToken", null);
        cookie.setPath("/");
        cookie.setMaxAge(0);

        response.addCookie(cookie);

        return BasicResponse.of(HttpStatus.OK);
    }

    /* 유저 정보 수정 */
    @PutMapping
    @PreAuthorize("hasRole('USER')")
    public BasicResponse updateInfo(@RequestBody @Valid UserRequestDto.Update updateDto, @LoginUser User loginUser){
        userService.updateInfo(loginUser.getId(), updateDto);

        return BasicResponse.of(HttpStatus.OK);
    }

    /* 유저 탈퇴 */
    @DeleteMapping
    @PreAuthorize("hasRole('USER')")
    public BasicResponse deleteUser(@LoginUser User loginUser){
        userService.deleteByUserId(loginUser.getId());

        return BasicResponse.of(HttpStatus.OK);
    }
}
