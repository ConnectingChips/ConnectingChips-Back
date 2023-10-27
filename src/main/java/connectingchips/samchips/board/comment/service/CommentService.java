package connectingchips.samchips.board.comment.service;

import connectingchips.samchips.board.comment.dto.CommentRequestDto;
import connectingchips.samchips.board.comment.dto.CommentResponseDto;
import connectingchips.samchips.board.comment.dto.ReplyRequestDto;
import connectingchips.samchips.board.comment.dto.ReplyResponseDto;
import connectingchips.samchips.board.comment.entity.Reply;
import connectingchips.samchips.board.comment.repository.CommentRepository;
import connectingchips.samchips.board.comment.repository.ReplyRepository;
import connectingchips.samchips.board.entity.Board;
import connectingchips.samchips.board.repository.BoardRepository;
import connectingchips.samchips.board.comment.entity.Comment;
import connectingchips.samchips.global.exception.BadRequestException;
import connectingchips.samchips.user.domain.User;
import connectingchips.samchips.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static connectingchips.samchips.global.exception.CommonErrorCode.*;


@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final ReplyRepository replyRepository;
    private final BoardRepository boardRepository;

    public Comment getCommentById(Long commentId){
        return commentRepository.findById(commentId).orElseThrow(() -> new BadRequestException(NOT_FOUND_COMMENT_ID));
    }

    public Reply getReplyById(Long replyId){
        return replyRepository.findById(replyId).orElseThrow(() -> new BadRequestException(NOT_FOUND_REPLY_ID));
    }

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
