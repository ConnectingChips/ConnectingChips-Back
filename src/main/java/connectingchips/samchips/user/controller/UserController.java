package connectingchips.samchips.user.controller;

import connectingchips.samchips.commons.dto.BasicResponse;
import connectingchips.samchips.commons.dto.DataResponse;
import connectingchips.samchips.user.domain.LoginUser;
import connectingchips.samchips.user.domain.User;
import connectingchips.samchips.user.dto.AuthResponseDto;
import connectingchips.samchips.user.dto.UserRequestDto;
import connectingchips.samchips.user.dto.UserResponseDto;
import connectingchips.samchips.user.service.AuthService;
import connectingchips.samchips.user.service.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@Validated
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private final AuthService authService;

    @PostMapping
    public ResponseEntity<BasicResponse> signup(@RequestBody UserRequestDto.Signup signupDto){
        userService.signup(signupDto);

        return new ResponseEntity<>(BasicResponse.of(HttpStatus.CREATED) ,HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public DataResponse<AuthResponseDto.AccessToken> login(@RequestBody UserRequestDto.Login loginDto, HttpServletResponse response){
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

    @GetMapping("/check-id")
    public DataResponse<UserResponseDto.CheckId> checkAccountId(@RequestParam String accountId){
        boolean isUsable = !userService.checkAccountId(accountId);
        UserResponseDto.CheckId checkIdDto = new UserResponseDto.CheckId(isUsable);

        return DataResponse.of(checkIdDto);
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('USER')")
    public DataResponse<UserResponseDto.Info> getLoginUser(@LoginUser User loginUser){
        UserResponseDto.Info info = UserResponseDto.Info.builder()
                .userId(loginUser.getId())
                .nickname(loginUser.getNickname())
                .profileImage(loginUser.getProfileImage())
                .roles(loginUser.getRoles())
                .build();

        return DataResponse.of(info);
    }

    @GetMapping("/reissue")
    public DataResponse<AuthResponseDto.AccessToken> reissue(@CookieValue("refreshToken") String refreshToken){
        AuthResponseDto.AccessToken accessToken = authService.reissueAccessToken(refreshToken);

        return DataResponse.of(accessToken);
    }

    @PutMapping("/logout")
    @PreAuthorize("hasAnyRole('USER')")
    public BasicResponse logout(@LoginUser User loginUser){
        authService.logout(loginUser.getId());

        return BasicResponse.of(HttpStatus.OK);
    }

    @PutMapping
    @PreAuthorize("hasAnyRole('USER')")
    public BasicResponse editUserInfo(@RequestBody UserRequestDto.Edit editDto, @LoginUser User loginUser){
        userService.editInfo(loginUser.getId(), editDto);

        return BasicResponse.of(HttpStatus.OK);
    }

    @DeleteMapping
    @PreAuthorize("hasAnyRole('USER')")
    public BasicResponse deleteUser(@LoginUser User loginUser){
        userService.deleteByUserId(loginUser.getId());

        return BasicResponse.of(HttpStatus.OK);
    }
}
