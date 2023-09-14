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
import jakarta.servlet.http.HttpServletRequest;
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
    public DataResponse<AuthResponseDto.Token> login(@RequestBody UserRequestDto.Login loginDto){
        AuthResponseDto.Token token = authService.login(loginDto);

        return DataResponse.of(token);
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
    public DataResponse<AuthResponseDto.Token> reissue(HttpServletRequest request){
        AuthResponseDto.Token token = authService.reissueAccessToken(request);

        return DataResponse.of(token);
    }

    @PutMapping("/logout")
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
