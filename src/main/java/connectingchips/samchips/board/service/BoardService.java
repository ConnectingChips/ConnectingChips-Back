package connectingchips.samchips.board.service;

import connectingchips.samchips.board.dto.board.BoardRequestDto;
import connectingchips.samchips.board.dto.board.BoardResponseDto;
import connectingchips.samchips.board.entity.Board;
import connectingchips.samchips.board.repository.BoardRepository;

import connectingchips.samchips.board.dto.comment.CommentResponseDto;
import connectingchips.samchips.board.dto.comment.ReplyResponseDto;
import connectingchips.samchips.board.entity.Comment;
import connectingchips.samchips.board.entity.Reply;
import connectingchips.samchips.board.repository.CommentRepository;
import connectingchips.samchips.board.repository.ReplyRepository;

import connectingchips.samchips.global.exception.BadRequestException;
import connectingchips.samchips.mind.entity.JoinedMind;
import connectingchips.samchips.mind.repository.JoinedMindRepository;

import connectingchips.samchips.mind.entity.Mind;
import connectingchips.samchips.mind.repository.MindRepository;
import connectingchips.samchips.user.domain.User;
import connectingchips.samchips.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


import java.io.IOException;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

import static connectingchips.samchips.global.exception.CommonErrorCode.*;
import static connectingchips.samchips.mind.service.JoinedMindService.FULL_COUNT;


@Service
@RequiredArgsConstructor
public class BoardService {

    public static final int JOINING = 1;
    private final S3Uploader s3Uploader;
    private final BoardRepository boardRepository;
    private final MindRepository mindRepository;
    private final UserRepository userRepository;
    private final JoinedMindRepository joinedMindRepository;
    private final CommentRepository commentRepository;
    private final ReplyRepository replyRepository;

    public Board getBoardById(Long boardId){
        return boardRepository.findById(boardId)
                .orElseThrow(() -> new BadRequestException(NOT_FOUND_BOARD_ID));
    }

    public List<BoardResponseDto.Read> getMindBoardList(Long mindId){
        List<BoardResponseDto.Read> boardList = getBoardList(mindId);

        for(BoardResponseDto.Read board : boardList) {
            Board tempBoard = boardRepository.findById(board.getBoardId()).orElseThrow(()->new BadRequestException(NOT_FOUND_BOARD_ID));
            List<CommentResponseDto.Read> commentList = getCommentList(board.getBoardId());
            int commentCount = (int) commentRepository.countByBoard(tempBoard);

            for(CommentResponseDto.Read comment : commentList) {
                List<ReplyResponseDto> replyList = getReplyList(comment.getCommentId());
                commentCount += replyList.size();
                comment.editRead(replyList);
            }
            board.editRead(commentCount, commentList);
        }
        return boardList;
    }

    public List<BoardResponseDto.Read> getBoardList(Long mindId) {
        Mind mind = mindRepository.findById(mindId).orElseThrow(() -> new BadRequestException(NOT_FOUND_MIND_ID));
        List<Board> boards = boardRepository.findAllByMind(mind);
        return boards.stream()
                .sorted(Comparator.comparing(Board::getBoardId).reversed())
                .map(board -> new BoardResponseDto.Read(board))
                .collect(Collectors.toList());
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
    
    public BoardResponseDto.CanEdit isUserEditor(Long boardId, Long userId) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new BadRequestException(NOT_FOUND_BOARD_ID));
        if(userId.equals(board.getUser().getId())) {
            return new BoardResponseDto.CanEdit(true);
        } else return new BoardResponseDto.CanEdit(false);
    }

    @Transactional
    public void createBoard(MultipartFile file, BoardRequestDto.Save boardRequestDto) throws IOException {
        Mind mind = mindRepository.
                findById(boardRequestDto.getMindId())
                .orElseThrow(() -> new BadRequestException(NOT_FOUND_MIND_ID));

        User user = userRepository.
                findById(boardRequestDto.getUserId())
                .orElseThrow(() -> new BadRequestException(NOT_FOUND_USER));

        String imageURL = s3Uploader.uploadFile(file,"board");

        changeJoinedMind(user, mind);

        Board board = Board.builder()
                .content(boardRequestDto.getContent())
                .image(imageURL)
                .mind(mind)
                .user(user)
                .build();

        boardRepository.save(board);
    }

    private void changeJoinedMind(User user, Mind mind) {
        JoinedMind joinedMind = checkJoinMind(user, mind).updateTodayWrite(true);
        joinedMind.updateCount(joinedMind.getCount()+JOINING);

        // 일반 사용자라면 참여한 작심 개수에 따라 예외 발생
        if(!user.getRoles().contains("ROLE_ADMIN")){
            if(joinedMind.getCount() > FULL_COUNT) throw new BadRequestException(INVALID_REQUEST);
        }

        joinedMindRepository.save(joinedMind);
    }

    private static JoinedMind checkJoinMind(User user, Mind mind) {
        Optional<JoinedMind> first = user.getJoinedMinds().stream().filter(joinedMind -> Objects.equals(joinedMind.getMind().getMindId(), mind.getMindId()))
                .findFirst();
        JoinedMind joinedMind = first.orElseThrow(() -> new BadRequestException(NOT_JOIN_MIND));
        if(joinedMind.getTodayWrite()) throw new BadRequestException(ALREADY_WRITE_BOARD);
        return joinedMind;
    }

    @Transactional
    public BoardResponseDto.Update updateBoard(Long boardId, BoardRequestDto.Edit boardRequestDto) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new BadRequestException(NOT_FOUND_BOARD_ID));
        board.updateContent(boardRequestDto.getContent());

        return new BoardResponseDto.Update(boardId, board.getContent());
    }

    @Transactional
    public void deleteBoard(Long boardId) {
        //====================================추가된 부분==========================================
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new BadRequestException(NOT_FOUND_BOARD_ID));
        User user = board.getUser();
        Optional<JoinedMind> first = user.getJoinedMinds().stream()
                .filter(joinedMind -> Objects.equals(joinedMind.getMind().getMindId(), board.getMind().getMindId()) && board.getCreatedAt().toLocalDate().equals(LocalDate.now()))
                .findFirst();
        first.ifPresent(joinedMind -> {joinedMind.updateCount(joinedMind.getCount()-1);
            joinedMindRepository.save(joinedMind.updateTodayWrite(false));
        });
        //==================================== 추가된 부분 =========================================
        if(boardRepository.existsById(boardId)) {
            boardRepository.deleteById(boardId);
        }
    }
}
