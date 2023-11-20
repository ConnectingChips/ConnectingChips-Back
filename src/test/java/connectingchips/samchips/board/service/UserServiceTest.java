package connectingchips.samchips.board.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import connectingchips.samchips.board.S3Uploader;
import connectingchips.samchips.board.stub.UserStubData;
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
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.ResultActions;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

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
        Long userId = 1L;
        UserRequestDto.Update updateDto = userStubData.createUpdateDto();
        Optional<User> getUser = Optional.of(userStubData.createUser());
        User createUser = userStubData.createUser();
        given(userRepository.findById(Mockito.anyLong())).willReturn(getUser);

        // when
        User updateUser = userService.updateInfo(userId, updateDto);

        // then
        assertThat(createUser.getNickname()).isNotEqualTo(updateUser.getNickname());
    }
}
