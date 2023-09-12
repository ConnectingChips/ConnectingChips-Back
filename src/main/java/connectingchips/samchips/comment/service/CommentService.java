package connectingchips.samchips.comment.service;

import connectingchips.samchips.board.entity.Board;
import connectingchips.samchips.comment.dto.CommentRequestDto;
import connectingchips.samchips.comment.dto.CommentResponseDto;
import connectingchips.samchips.comment.entity.Comment;
import connectingchips.samchips.comment.repository.CommentRepository;
import connectingchips.samchips.user.domain.User;
import connectingchips.samchips.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final BoardRepository boardRepository;

    @Transactional
    public CommentResponseDto createComment(CommentRequestDto commentReqDto) {
        User user = userRepository.findById(commentReqDto.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 유저입니다."));
        Board board = boardRepository.findById(commentReqDto.getBoardId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시물입니다"));
        Comment comment = commentRepository.save(commentReqDto.toEntity(board, user));

        return commentResponseDto;
    }
    @Transactional
    public void deleteComment(Long commentId) {
        commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 댓글입니다."));

        commentRepository.deleteById(commentId);
    }

}
