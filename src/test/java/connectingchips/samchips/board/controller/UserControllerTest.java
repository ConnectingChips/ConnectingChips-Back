package connectingchips.samchips.board.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import connectingchips.samchips.board.stub.UserStubData;
import connectingchips.samchips.user.controller.UserController;
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
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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
    public void 아이디_사용가능여부() throws Exception {
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
}
