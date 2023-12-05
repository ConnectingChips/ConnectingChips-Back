package connectingchips.samchips.mind.controller;

import connectingchips.samchips.mind.entity.JoinedMind;
import connectingchips.samchips.mind.entity.Mind;
import connectingchips.samchips.mind.service.JoinedMindService;
import connectingchips.samchips.stub.MindStubData;
import connectingchips.samchips.stub.UserStubData;
import connectingchips.samchips.user.domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(JoinedMindController.class)
public class JoinedMindControllerTest {

    @Autowired
    private MockMvc mockMvc;
    private UserStubData userStubData;
    private MindStubData mindStubData;
    @MockBean
    private JoinedMindService joinedMindService;

    @BeforeEach
    void init() {
        userStubData = new UserStubData();
        mindStubData = new MindStubData();
    }


    @Test
    void 유저를_작심에_참여시키기() throws Exception {
        /*
        //given
        User user = userStubData.createUser1();
        Mind mind = mindStubData.createMind();
        JoinedMind joinedMind = mindStubData.createJoinedMind(user, mind);

        joinedMindService.makeMindRelation(1L,user);

        //when
        ResultActions result = mockMvc.perform(
                get("/joined-minds/{mind_id}",1L)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON));

        //then
        result.andDo(MockMvcResultHandlers.print())
                .andExpect(status().isCreated());
        */
    }

    @Test
    void 유저를_작심에_탈퇴시키기() throws Exception {
        /*
//        {
//            "accountId": "abcde123",
//                "password": "abcde123",
//                "email": "abcde123@gmail.com",
//                "nickname": "칩스"
//        }

        //given
        User user = userStubData.createUser1();
        Mind mind = mindStubData.createMind();
        JoinedMind joinedMind = mindStubData.createJoinedMind(user, mind);

        joinedMindService.exitMindRelation(1L, user);

        //when
        ResultActions result = mockMvc.perform(
                get("/joined-minds/{mind_id}/exit")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON));

        //then
        result.andDo(MockMvcResultHandlers.print())
                .andExpect(status().isCreated());

         */
    }
}
