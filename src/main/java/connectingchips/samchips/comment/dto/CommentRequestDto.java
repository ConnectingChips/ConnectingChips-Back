package connectingchips.samchips.comment.dto;

import connectingchips.samchips.board.entity.Board;
import connectingchips.samchips.comment.entity.Comment;
import connectingchips.samchips.user.domain.User;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CommentRequestDto {
    private Long userId;
    private Long boardId;
    private String content;

    public Comment toEntity(Board board, User user){
        return Comment.builder()
                .board(board)
                .user(user)
                .content(content)
                .build();
    }
}
