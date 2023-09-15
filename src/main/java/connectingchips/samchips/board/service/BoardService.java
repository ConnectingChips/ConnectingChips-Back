package connectingchips.samchips.board.service;

import connectingchips.samchips.board.dto.BoardRequestDto;
import connectingchips.samchips.board.dto.BoardResponseDto;
import connectingchips.samchips.board.entity.Board;
import connectingchips.samchips.board.repository.BoardRepository;
import connectingchips.samchips.comment.dto.CommentResponseDto;
import connectingchips.samchips.comment.dto.ReplyResponseDto;
import connectingchips.samchips.comment.entity.Comment;
import connectingchips.samchips.comment.entity.Reply;
import connectingchips.samchips.comment.repository.CommentRepository;
import connectingchips.samchips.comment.repository.ReplyRepository;
import connectingchips.samchips.mind.entity.Mind;
import connectingchips.samchips.mind.repository.MindRepository;
import connectingchips.samchips.user.domain.User;
import connectingchips.samchips.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;
    private final MindRepository mindRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;
    private final ReplyRepository replyRepository;

    public  List<BoardResponseDto.Read> getMindBoardList(Long mindId){
        List<BoardResponseDto.Read> boardList = getBoardList(mindId);

        for(BoardResponseDto.Read board : boardList) {
            Board tempBoard = boardRepository.findById(board.getBoardId()).orElseThrow(()->new IllegalArgumentException("존재하지 않는 게시글입니다."));
            int commentCount = (int) commentRepository.countByBoard(tempBoard);
            List<CommentResponseDto.Read> commentList = getCommentList(board.getBoardId());
            board.editRead(commentCount, commentList);

            for(CommentResponseDto.Read comment : commentList) {
                List<ReplyResponseDto> replyList = getReplyList(comment.getCommentId());
                comment.editRead(replyList);
            }
        }
        return boardList;
    }

    public List<BoardResponseDto.Read> getBoardList(Long mindId) {
        Mind mind = mindRepository.findById(mindId).orElseThrow(() -> new IllegalArgumentException("작심이 존재하지 않습니다"));
        List<Board> boards = boardRepository.findAllByMind(mind);
        return boards.stream().map(board -> new BoardResponseDto.Read(board)).collect(Collectors.toList());
    }
    public List<CommentResponseDto.Read> getCommentList(Long boardId){
        Board board = boardRepository.findById(boardId).orElseThrow(() -> new IllegalArgumentException("게시물이 존재하지 않습니다"));
        List<Comment> comments = commentRepository.findAllByBoard(board);
        return comments.stream().map(comment -> new CommentResponseDto.Read(comment)).collect(Collectors.toList());
    }

    public List<ReplyResponseDto> getReplyList(Long commentId) {
        List<Reply> replies = replyRepository.findAllByCommentId(commentId);
        return replies.stream().map(reply -> new ReplyResponseDto(reply)).collect(Collectors.toList());
    }

    public BoardResponseDto.CanEdit isUserEditer(Long boardId, Long userId) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new IllegalArgumentException("게시물이 존재하지 않습니다"));
        if(userId == board.getUser().getId()) {
            return new BoardResponseDto.CanEdit(true);
        } else return new BoardResponseDto.CanEdit(false);
    }

    @Transactional
    public void createBoard(BoardRequestDto boardRequestDto) {
        Mind mind = mindRepository.getReferenceById(boardRequestDto.getMindId());
        User user = userRepository.getReferenceById(boardRequestDto.getUserId());
        Board board = Board.builder()
                .content(boardRequestDto.getContent())
                .image(boardRequestDto.getImage())
                .mind(mind)
                .user(user)
                .build();
        boardRepository.save(board);
    }
    @Transactional
    public BoardResponseDto.Update updateBoard(Long boardId, BoardRequestDto boardRequestDto) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new IllegalArgumentException("존재 하지 않는 게시물입니다"));
        board.editContent(boardRequestDto.getContent());

        return new BoardResponseDto.Update(boardId, board.getContent());
    }

    @Transactional
    public void deleteBoard(Long boardId) {
        if(boardRepository.existsById(boardId)) {
            boardRepository.deleteById(boardId);
        }
    }
}
