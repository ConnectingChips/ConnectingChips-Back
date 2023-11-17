package connectingchips.samchips.board.service;

import connectingchips.samchips.board.stub.BoardStubData;
import connectingchips.samchips.board.S3Uploader;
import connectingchips.samchips.board.comment.dto.CommentResponseDto;
import connectingchips.samchips.board.comment.entity.Comment;
import connectingchips.samchips.board.comment.repository.CommentRepository;
import connectingchips.samchips.board.comment.repository.ReplyRepository;
import connectingchips.samchips.board.dto.BoardResponseDto;
import connectingchips.samchips.board.entity.Board;
import connectingchips.samchips.board.repository.BoardRepository;
import connectingchips.samchips.board.stub.CommentStubData;
import connectingchips.samchips.mind.entity.Mind;
import connectingchips.samchips.mind.joinedmind.repository.JoinedMindRepository;
import connectingchips.samchips.mind.repository.MindRepository;
import connectingchips.samchips.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class BoardServiceTest {

    public static final long MIND_ID = 1L;
    public static final long BOARD_ID = 1L;
    private BoardStubData boardStubData;
    private CommentStubData commentStubData;

    @InjectMocks
    private BoardService boardService;
    @Mock
    private  S3Uploader s3Uploader;
    @Mock
    private  BoardRepository boardRepository;
    @Mock
    private  MindRepository mindRepository;
    @Mock
    private  UserRepository userRepository;
    @Mock
    private  JoinedMindRepository joinedMindRepository;
    @Mock
    private  CommentRepository commentRepository;
    @Mock
    private  ReplyRepository replyRepository;

    @BeforeEach
    void init() {
        boardStubData = new BoardStubData();
        commentStubData = new CommentStubData(boardStubData);
    }
    @Test
    void getBoardById() {
    }

    @Test
    void 작심_게시판리스트_조회() {
        //given
        Long mindId = MIND_ID;
        List<Board> boards = boardStubData.createBoards();


        given(mindRepository.findById(Mockito.anyLong())).willReturn(Optional.of(new Mind()));
        given(boardRepository.findAllByMind(Mockito.any(Mind.class))).willReturn(boards);
        given(boardRepository.findById(Mockito.anyLong())).willReturn(Optional.of(new Board()));
        given(commentRepository.countByBoard(Mockito.any(Board.class))).willReturn(0L);

        //when
        List<BoardResponseDto.Read> result = boardService.getMindBoardList(mindId);

        //then
        assertThat(result.get(0).getContent()).isEqualTo(boards.get(0).getContent());
        assertThat(result.get(0).getNickname()).isEqualTo(boards.get(0).getUser().getNickname());
        assertThat(result.get(0).getProfileImage()).isEqualTo(boards.get(0).getUser().getProfileImage());
    }

    @Test
    void getBoardList() {
    }

    @Test
    void getReplyList() {
    }

    @Test
    void 댓글_리스트_조회() {
        //given
        Long boardId = BOARD_ID;
        List<Comment> comments = commentStubData.createComments();

        given(boardRepository.findById(Mockito.anyLong())).willReturn(Optional.of(new Board()));
        given(commentRepository.findAllByBoard(Mockito.any(Board.class))).willReturn(comments);
        //when
        List<CommentResponseDto.Read> result = boardService.getCommentList(boardId);
        //then
        assertThat(result.size()).isEqualTo(comments.size());
        assertThat(result.get(0).getContent()).isEqualTo(comments.get(0).getContent());
        assertThat(result.get(1).getContent()).isEqualTo(comments.get(1).getContent());
        assertThat(result.get(0).getNickname()).isEqualTo(comments.get(1).getUser().getNickname());
    }

    @Test
    void 유저_게시판_수정_자격여부확인() {
        //given
//        Board board = boardStubData.createBoard();
//        given(boardRepository.findById(Mockito.anyLong())).willReturn(Optional.of(board));
        //when
        //then

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