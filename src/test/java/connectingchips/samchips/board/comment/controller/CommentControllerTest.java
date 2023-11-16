package connectingchips.samchips.board.comment.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import connectingchips.samchips.board.comment.dto.CommentRequestDto;
import connectingchips.samchips.board.comment.dto.CommentResponseDto;
import connectingchips.samchips.board.comment.service.CommentService;
import connectingchips.samchips.board.stub.BoardStubData;
import connectingchips.samchips.board.stub.CommentStubData;
import connectingchips.samchips.user.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = CommentController.class)
class CommentControllerTest {
    @Autowired
    private MockMvc mockMvc;

    private BoardStubData boardStubData;
    private ObjectMapper objectMapper;
    private CommentStubData commentStubData;

    @MockBean
    private CommentService commentService;
    @MockBean
    private UserService userService;


    @BeforeEach
    void init() {
        objectMapper = new ObjectMapper();
        boardStubData = new BoardStubData();
        commentStubData = new CommentStubData(boardStubData);
    }
    @Test
    void createComment() throws Exception {
        //given

        CommentResponseDto.Read read = commentStubData.createCommentResponseDtoRead();
        String content = objectMapper.writeValueAsString(read);
        given(commentService.createComment(Mockito.any(CommentRequestDto.class))).willReturn(read);

        //when
        ResultActions result = mockMvc.perform(
                post("/comments")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content));
        //then
        result.andExpect(status().isCreated())
                .andExpect(jsonPath("$.data.nickname").value(read.getNickname()));

    }

    @Test
    void deleteComment() {
    }

    @Test
    void createReply() {
    }

    @Test
    void deleteReply() {
    }
}