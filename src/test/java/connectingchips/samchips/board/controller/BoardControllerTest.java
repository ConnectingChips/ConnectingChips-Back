package connectingchips.samchips.board.controller;

import connectingchips.samchips.stub.BoardStubData;
import connectingchips.samchips.board.dto.board.BoardResponseDto;
import connectingchips.samchips.board.entity.Board;
import connectingchips.samchips.board.service.BoardService;

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
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = BoardController.class)
class BoardControllerTest {

    @Autowired
    private MockMvc mockMvc;
    private BoardStubData boardStubData;

    @MockBean
    private BoardService boardService;

    @BeforeEach
    void init() {
        boardStubData = new BoardStubData();
    }

    @Test
    @WithMockUser
    void 작심_게시글_ID기반조회() throws Exception {
        //given
        Board board = boardStubData.createBoard();

        BoardResponseDto.Read read = new BoardResponseDto.Read(board);
        List<BoardResponseDto.Read> reads = new ArrayList<>();
        reads.add(read);

        given(boardService.getMindBoardList(Mockito.anyLong())).willReturn(reads);

        //when
        ResultActions result = mockMvc.perform(
                get("/boards/{mind_id}",1L)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON));

        //then
        result
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].content").value(reads.get(0).getContent()));

    }

    @Test
    @WithMockUser
    void 유저_게시판_수정_자격여부확인() throws Exception {
        //given
        Long boardId = 1L;
        Long userId = 1L;
        BoardResponseDto.CanEdit canEdit = new BoardResponseDto.CanEdit(true);

        given(boardService.isUserEditor(Mockito.anyLong(),Mockito.anyLong())).willReturn(canEdit);

        MultiValueMap<String, String> info = new LinkedMultiValueMap<>();

        info.add("board_id", String.valueOf(boardId));
        info.add("user_id", String.valueOf(userId));

        //when
        ResultActions result = mockMvc.perform(
                get("/boards/authentication")
                        .params(info)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON));
        //then
        result
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.canEdit").value(canEdit.isCanEdit()));

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