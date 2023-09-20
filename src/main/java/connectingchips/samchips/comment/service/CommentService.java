package connectingchips.samchips.comment.service;

import connectingchips.samchips.board.entity.Board;
import connectingchips.samchips.board.repository.BoardRepository;
import connectingchips.samchips.comment.dto.CommentRequestDto;
import connectingchips.samchips.comment.dto.CommentResponseDto;
import connectingchips.samchips.comment.dto.ReplyRequestDto;
import connectingchips.samchips.comment.dto.ReplyResponseDto;
import connectingchips.samchips.comment.entity.Comment;
import connectingchips.samchips.comment.entity.Reply;
import connectingchips.samchips.comment.repository.CommentRepository;
import connectingchips.samchips.comment.repository.ReplyRepository;
import connectingchips.samchips.exception.BadRequestException;
import connectingchips.samchips.user.domain.User;
import connectingchips.samchips.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static connectingchips.samchips.exception.CommonErrorCode.*;


@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final ReplyRepository replyRepository;
    private final BoardRepository boardRepository;

    @Transactional
    public CommentResponseDto.Read createComment(CommentRequestDto commentReqDto) {
        User user = userRepository.findById(commentReqDto.getUserId())
                .orElseThrow(() -> new BadRequestException(NOT_FOUND_USER));
        Board board = boardRepository.findById(commentReqDto.getBoardId())
                .orElseThrow(() -> new BadRequestException(NOT_FOUND_BOARD_ID));

        Comment comment = commentRepository.save(commentReqDto.toEntity(board, user));
        return new CommentResponseDto.Read(comment);
    }

    @Transactional
    public void deleteComment(Long commentId) {
        commentRepository.findById(commentId)
                .orElseThrow(() -> new BadRequestException(NOT_FOUND_COMMENT_ID));
        commentRepository.deleteById(commentId);
    }

    @Transactional
    public ReplyResponseDto createReply(ReplyRequestDto replyRequestDto) {
        User user = userRepository.findById(replyRequestDto.getUserId())
                .orElseThrow(() -> new BadRequestException(NOT_FOUND_USER));
        Comment comment = commentRepository.findById(replyRequestDto.getCommentId())
                .orElseThrow(() -> new BadRequestException(NOT_FOUND_COMMENT_ID));

        Reply reply = replyRepository.save(replyRequestDto.toEntity(comment, user, replyRequestDto.getContent()));
        return new ReplyResponseDto(reply);
    }

    @Transactional
    public void deleteReply(Long replyId) {
        replyRepository.findById(replyId)
                .orElseThrow(() -> new BadRequestException(NOT_FOUND_REPLY_ID));

        replyRepository.deleteById(replyId);
    }
}
