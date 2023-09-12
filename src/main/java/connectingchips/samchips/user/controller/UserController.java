package connectingchips.samchips.user.controller;

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
    public ResponseEntity<Void> signup(@RequestBody UserRequestDto.Signup signupDto){
        userService.signup(signupDto);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDto.Token> login(@RequestBody UserRequestDto.Login loginDto){
        AuthResponseDto.Token token = authService.login(loginDto);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(JwtFilter.AUTHORIZATION_HEADER, "Bearer " + token.getToken());

        return new ResponseEntity<>(token, httpHeaders, HttpStatus.OK);
    }

    @GetMapping("/{userId}")
    @PreAuthorize("hasAnyRole('USER')")
    public ResponseEntity<UserResponseDto.Info> findByUserId(@PathVariable Long userId){
        UserResponseDto.Info result = userService.findByUserId(userId);

        return ResponseEntity.ok(result);
    }

    @PutMapping("/{userId}")
    public ResponseEntity<Void> deleteByUserId(@PathVariable Long userId, @RequestBody UserRequestDto.Edit editDto){
        userService.editInfo(userId, editDto);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deleteByUserId(@PathVariable Long userId){
        userService.deleteByUserId(userId);

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
