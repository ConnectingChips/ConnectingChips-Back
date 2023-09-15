package connectingchips.samchips.comment.service;

import connectingchips.samchips.board.dto.BoardResponseDto;
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
import connectingchips.samchips.user.domain.User;
import connectingchips.samchips.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final ReplyRepository replyRepository;
    private final BoardRepository boardRepository;

    public List<CommentResponseDto.Read> getCommentList(Long boardId){
        List<Comment> comments = commentRepository.findAllByBoardId(boardId);
        return comments.stream().map(comment -> new CommentResponseDto.Read(comment)).collect(Collectors.toList());
    }

    public List<ReplyResponseDto> getReplyList(Long commentId) {
        List<Reply> replies = replyRepository.findAllByCommentId(commentId);
        return replies.stream().map(reply -> new ReplyResponseDto(reply)).collect(Collectors.toList());
    }
    @Transactional
    public CommentResponseDto.Read createComment(CommentRequestDto commentReqDto) {
        User user = userRepository.findById(commentReqDto.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 유저입니다."));
        Board board = boardRepository.findById(commentReqDto.getBoardId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시물입니다"));

        Comment comment = commentRepository.save(commentReqDto.toEntity(board, user));
        return new CommentResponseDto.Read(comment);
    }

    @Transactional
    public void deleteComment(Long commentId) {
        commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 댓글입니다."));
        commentRepository.deleteById(commentId);
    }

    @Transactional
    public ReplyResponseDto createReply(ReplyRequestDto replyRequestDto) {
        User user = userRepository.findById(replyRequestDto.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 유저입니다."));
        Comment comment = commentRepository.findById(replyRequestDto.getCommentId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 댓글입니다"));
        Reply reply = replyRepository.save(replyRequestDto.toEntity(comment, user));
        return new ReplyResponseDto(reply);
    }

    public void deleteReply(Long replyId) {
        commentRepository.findById(replyId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 답글입니다."));

        commentRepository.deleteById(replyId);
    }
}
