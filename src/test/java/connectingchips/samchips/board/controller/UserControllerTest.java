package connectingchips.samchips.board.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import connectingchips.samchips.stub.UserStubData;
import connectingchips.samchips.global.email.dto.EmailRequestDto;
import connectingchips.samchips.user.controller.UserController;
import connectingchips.samchips.user.domain.User;
import connectingchips.samchips.user.domain.UserAdapter;
import connectingchips.samchips.user.dto.UserRequestDto;
import connectingchips.samchips.user.dto.UserResponseDto;
import connectingchips.samchips.user.service.AuthService;
import connectingchips.samchips.user.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = UserController.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private ObjectMapper objectMapper;
    private UserStubData userStubData;

    @MockBean
    private UserService userService;
    @MockBean
    private AuthService authService;

    @BeforeEach
    void init() {
        objectMapper = new ObjectMapper();
        userStubData = new UserStubData();
    }

    @Test
    @WithMockUser
    public void 사용자_회원가입() throws Exception {
        // given
        UserRequestDto.Signup signupDto = userStubData.createSignupDto();
        String content = new ObjectMapper().writeValueAsString(signupDto);

        // when
        ResultActions result = mockMvc.perform(
                post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content)
                        .with(csrf()));

        // then
        result
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.statusCode").value(201));
    }

    @Test
    @WithMockUser
    public void 아이디_사용가능_여부() throws Exception {
        // given
        String accountId = "test";
        given(userService.checkAccountId(Mockito.anyString())).willReturn(false);

        // when
        ResultActions result = mockMvc.perform(
                get("/users/check-id")
                        .param("accountId", accountId)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON));

        // then
        result
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.isUsable").value(true));
    }

    @Test
    @WithMockUser
    public void 로그인_여부() throws Exception {
        // given
        UserAdapter userAdapter = userStubData.createUserAdapter();

        // when
        ResultActions result = mockMvc.perform(
                get("/users/check-login")
                        .with(SecurityMockMvcRequestPostProcessors.user(userAdapter))
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON));

        // then
        result
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.isLogin").value(true));
    }

    @Test
    @WithMockUser
    public void 로그인_사용자_정보_반환() throws Exception {
        // given
        UserAdapter userAdapter = userStubData.createUserAdapter();

        // when
        ResultActions result = mockMvc.perform(
                get("/users")
                        .with(SecurityMockMvcRequestPostProcessors.user(userAdapter))
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON));

        // then
        result
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.userId").value(userAdapter.getUser().getId()))
                .andExpect(jsonPath("$.data.nickname").value(userAdapter.getUser().getNickname()));
    }

    @Test
    @WithMockUser
    public void 인증_이메일_전송() throws Exception {
        // given
        EmailRequestDto.Authentication emailAuthenticationDto = userStubData.createEmailAuthenticationDto();
        String content = new ObjectMapper().writeValueAsString(emailAuthenticationDto);

        // when
        ResultActions result = mockMvc.perform(
                post("/users/authentication-email")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content)
                        .with(csrf()));

        // then
        result
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.statusCode").value(200));
    }

    @Test
    @WithMockUser
    public void 사용자_정보_수정() throws Exception {
        // given
        UserAdapter userAdapter = userStubData.createUserAdapter();
        UserRequestDto.Update updateDto = userStubData.createUpdateDto();
        String content = objectMapper.writeValueAsString(updateDto);

        // when
        ResultActions result = mockMvc.perform(
                put("/users")
                        .with(SecurityMockMvcRequestPostProcessors.user(userAdapter))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content)
                        .with(csrf()));

        // then
        result
                .andExpect(status().isOk());
    }
}
