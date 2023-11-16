package connectingchips.samchips.board.controller;

import connectingchips.samchips.board.dto.BoardResponseDto;
import connectingchips.samchips.board.entity.Board;
import connectingchips.samchips.board.service.BoardService;
import connectingchips.samchips.global.security.config.SecurityConfig;
import connectingchips.samchips.user.jwt.JwtSecurityConfig;

import connectingchips.samchips.mind.entity.Mind;
import connectingchips.samchips.mind.joinedmind.entity.JoinedMind;
import connectingchips.samchips.mind.mindtype.entity.MindType;
import connectingchips.samchips.user.domain.SocialType;
import connectingchips.samchips.user.domain.User;
import connectingchips.samchips.user.service.UserService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(controllers = BoardController.class,
        excludeAutoConfiguration = {SecurityConfig.class,JwtSecurityConfig.class})
@MockBean({JpaMetamodelMappingContext.class})
class BoardControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BoardService boardService;

    @MockBean
    private UserService userService;

    @Test
    void 작심_게시글_ID기반조회() throws Exception {
//        //given
//        User user = User.builder()
//                .email("test@test.com")
//                .password("test123456")
//                .nickname("테스트닉네임")
//                .accountId("test12345")
//                .ageRange("17")
//                .gender("남")
//                .socialType(SocialType.SAMCHIPS)
//                .profileImage("test.png")
//                .build();
//        MindType mindtype = MindType.builder()
//                .name("작심타입테스트")
//                .build();
//        Mind mind = Mind.builder()
//                .name("작심테스트")
//                .mindType(mindtype)
//                .writeFormat("작성방식예제")
//                .introduce("소개 예제")
//                .myListImage("mylist.jpg")
//                .pageImage("page.jpg")
//                .totalListImage("totallist.jpg")
//                .introImage("intro.jpg")
//                .exampleImage("example.jpg")
//                .build();
//
//        Board board = Board.builder()
//                .mind(mind)
//                .user(user)
//                .content("게시판내용")
//                .image("게시판이미지.jpg")
//                .build();
//
//        JoinedMind joinedMind = JoinedMind.builder()
//                .user(user)
//                .mind(mind)
//                .build();
//        List<JoinedMind> joinedMinds = user.getJoinedMinds();
//        joinedMinds.add(joinedMind);
//        user.editJoinedMinds(joinedMinds);
//
//        BoardResponseDto.Read read = new BoardResponseDto.Read(board);
//        List<BoardResponseDto.Read> reads = new ArrayList<>();
//        reads.add(read);
//
//        given(boardService.getMindBoardList(Mockito.anyLong())).willReturn(reads);
//
//        //when
//        ResultActions result = mockMvc.perform(
//                get("/boards/{mind_id}",1L)
//                        .accept(MediaType.APPLICATION_JSON)
//                        .contentType(MediaType.APPLICATION_JSON));
//
//
//        //then
//        result
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.mindBoardList[0].content").value(reads.get(0).getContent()));

    }

    @Test
    void getBoardUser() {
    }

    @Test
    void createBoard() {
    }

    @Test
    void updateBoard() {
    }

    @Test
    void deleteBoard() {
    }
}