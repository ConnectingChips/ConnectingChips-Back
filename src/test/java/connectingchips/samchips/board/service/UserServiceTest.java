package connectingchips.samchips.board.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import connectingchips.samchips.board.S3Uploader;
import connectingchips.samchips.stub.UserStubData;
import connectingchips.samchips.global.exception.BadRequestException;
import connectingchips.samchips.global.exception.RestApiException;
import connectingchips.samchips.user.domain.User;
import connectingchips.samchips.user.dto.UserRequestDto;
import connectingchips.samchips.user.repository.UserRepository;
import connectingchips.samchips.user.service.AuthService;
import connectingchips.samchips.user.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static connectingchips.samchips.global.exception.AuthErrorCode.*;
import static connectingchips.samchips.global.exception.CommonErrorCode.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    private final long USER_ID = 1L;

    private UserStubData userStubData;

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;
    @Mock
    private AuthService authService;
    @Mock
    private BCryptPasswordEncoder passwordEncoder;
    @Mock
    private S3Uploader s3Uploader;

    @BeforeEach
    void init() {
        userStubData = new UserStubData();
    }

//    @Test
//    public void 사용자_회원가입() {
//        // given
//        UserRequestDto.Signup signupDto = userStubData.createSignupDto();
//        given(userRepository.existsByAccountId(Mockito.anyString())).willReturn(false);
//        given(authService.verificationEmail(Mockito.anyString())).willReturn(true);
//        given(passwordEncoder.encode(Mockito.anyString())).willReturn("");
//        given(s3Uploader.find(Mockito.anyString())).willReturn(Collections.emptyList());
//        given(userService.randomDefaultProfileImage()).willReturn("");
//        given(userRepository.save(Mockito.any(User.class))).willReturn(null);
//
//        // when
//        userService.signup(signupDto);
//
//        // then
//        verify(userRepository, times(1)).existsByAccountId(Mockito.anyString());
//        verify(authService, times(1)).verificationEmail(Mockito.anyString());
//        verify(userRepository, times(1)).save(Mockito.any(User.class));
//    }

    @Test
    public void 아이디_사용가능여부() {
        // given
        given(userRepository.existsByAccountId(Mockito.anyString())).willReturn(true);

        // when
        boolean result = userService.checkAccountId(Mockito.anyString());

        // then
        assertThat(result).isTrue();
    }

    @Test
    public void 사용자_정보_수정(){
        // given
        UserRequestDto.Update updateDto = userStubData.createUpdateDto();
        Optional<User> getUser = Optional.of(userStubData.createUser1());
        User createUser = userStubData.createUser1();
        given(userRepository.findById(Mockito.anyLong())).willReturn(getUser);

        // when
        User updateUser = userService.updateInfo(USER_ID, updateDto);

        // then
        assertThat(createUser.getNickname()).isNotEqualTo(updateUser.getNickname());
    }

    @Test
    public void 사용자_정보_수정_존재하지않는사용자_예외(){
        // given
        Long userId = 1L;
        UserRequestDto.Update updateDto = userStubData.createUpdateDto();
        given(userRepository.findById(Mockito.anyLong())).willReturn(Optional.empty());

        // when & then
        RestApiException exception = assertThrows(RestApiException.class, () ->{
            userService.updateInfo(userId, updateDto);
        });
        assertThat(NOT_FOUND_USER.getMessage()).isEqualTo(exception.getErrorCode().getMessage());
    }

    @Test
    public void 로그인_사용자인지_비교_불일치_예외(){
        // given
        User loginUser = userStubData.createUser1();
        User user = userStubData.createUser2();

        // when & then
        BadRequestException exception = assertThrows(BadRequestException.class, () ->{
            userService.isLoginUser(loginUser, user);
        });
        assertThat(FORBIDDEN.getMessage()).isEqualTo(exception.getErrorCode().getMessage());
    }
}
