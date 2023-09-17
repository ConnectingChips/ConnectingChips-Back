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

import connectingchips.samchips.exception.BadRequestException;
import connectingchips.samchips.exception.ExceptionCode;
import connectingchips.samchips.joinedmind.entity.JoinedMind;
import connectingchips.samchips.joinedmind.repository.JoinedMindRepository;

import connectingchips.samchips.mind.entity.Mind;
import connectingchips.samchips.mind.repository.MindRepository;
import connectingchips.samchips.user.domain.User;
import connectingchips.samchips.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.stream.Collectors;

import java.util.Objects;
import java.util.Optional;

import static connectingchips.samchips.exception.ExceptionCode.*;


@Service
@RequiredArgsConstructor
public class BoardService {

    public static final int JOINING = 1;
    private final BoardRepository boardRepository;
    private final MindRepository mindRepository;
    private final UserRepository userRepository;
    private final JoinedMindRepository joinedMindRepository;
    private final CommentRepository commentRepository;
    private final ReplyRepository replyRepository;

    public  List<BoardResponseDto.Read> getMindBoardList(Long mindId){
        List<BoardResponseDto.Read> boardList = getBoardList(mindId);

        for(BoardResponseDto.Read board : boardList) {
            Board tempBoard = boardRepository.findById(board.getBoardId()).orElseThrow(()->new BadRequestException(NOT_FOUND_BOARD_ID));
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
        Mind mind = mindRepository.findById(mindId).orElseThrow(() -> new BadRequestException(NOT_FOUND_MIND_ID));
        List<Board> boards = boardRepository.findAllByMind(mind);
        return boards.stream().map(board -> new BoardResponseDto.Read(board)).collect(Collectors.toList());
    }
    public List<CommentResponseDto.Read> getCommentList(Long boardId){
        Board board = boardRepository.findById(boardId).orElseThrow(() -> new BadRequestException(NOT_FOUND_BOARD_ID));
        List<Comment> comments = commentRepository.findAllByBoard(board);
        return comments.stream().map(comment -> new CommentResponseDto.Read(comment)).collect(Collectors.toList());
    }

    public List<ReplyResponseDto> getReplyList(Long commentId) {
        List<Reply> replies = replyRepository.findAllByCommentId(commentId);
        return replies.stream().map(reply -> new ReplyResponseDto(reply)).collect(Collectors.toList());
    }
    
    public BoardResponseDto.CanEdit isUserEditer(Long boardId, Long userId) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new BadRequestException(NOT_FOUND_BOARD_ID));
        if(userId == board.getUser().getId()) {
            return new BoardResponseDto.CanEdit(true);
        } else return new BoardResponseDto.CanEdit(false);
    }

    @Transactional
    public void createBoard(BoardRequestDto boardRequestDto) {
        Mind mind = mindRepository.
                findById(boardRequestDto.getMindId())
                .orElseThrow(() -> new BadRequestException(NOT_FOUND_MIND_ID));

        User user = userRepository.
                findById(boardRequestDto.getUserId())
                .orElseThrow(() -> new BadRequestException(NOT_FOUND_USER_ID));
        JoinedMind joinedMind = checkJoinMind(user, mind).setIsJoining(JOINING);
        joinedMindRepository.save(joinedMind);
        Board board = Board.builder()
                .content(boardRequestDto.getContent())
                .image(boardRequestDto.getImage())
                .mind(mind)
                .user(user)
                .build();
        boardRepository.save(board);
    }

    private static JoinedMind checkJoinMind(User user, Mind mind) {
        Optional<JoinedMind> first = user.getJoinedMinds().stream().filter(joinedMind -> Objects.equals(joinedMind.getMind().getMindId(), mind.getMindId()))
                .findFirst();
        JoinedMind joinedMind = first.orElseThrow(() -> new BadRequestException(NOT_JOIN_MIND));
        if(joinedMind.getTodayWrite()) throw new BadRequestException(ALREADY_WRITE_BOARD);
        return joinedMind;

    }

    @Transactional
    public BoardResponseDto.Update updateBoard(Long boardId, BoardRequestDto boardRequestDto) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new BadRequestException(NOT_FOUND_BOARD_ID));
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
