package connectingchips.samchips.user.controller;

import connectingchips.samchips.commons.dto.BasicResponse;
import connectingchips.samchips.commons.dto.DataResponse;
import connectingchips.samchips.user.dto.AuthResponseDto;
import connectingchips.samchips.user.dto.UserRequestDto;
import connectingchips.samchips.user.dto.UserResponseDto;
import connectingchips.samchips.user.jwt.JwtFilter;
import connectingchips.samchips.user.service.AuthService;
import connectingchips.samchips.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
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

    @GetMapping("/{userId}")
    @PreAuthorize("hasAnyRole('USER')")
    public DataResponse<UserResponseDto.Info> findByUserId(@PathVariable Long userId){
        UserResponseDto.Info result = userService.findByUserId(userId);

        return DataResponse.of(result);
    }

    @PutMapping("/{userId}")
    public BasicResponse deleteByUserId(@PathVariable Long userId, @RequestBody UserRequestDto.Edit editDto){
        userService.editInfo(userId, editDto);

        return BasicResponse.of(HttpStatus.OK);
    }

    @DeleteMapping("/{userId}")
    public BasicResponse deleteByUserId(@PathVariable Long userId){
        userService.deleteByUserId(userId);

        return BasicResponse.of(HttpStatus.OK);
    }
}
