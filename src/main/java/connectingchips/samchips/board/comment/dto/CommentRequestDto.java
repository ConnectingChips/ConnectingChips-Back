package connectingchips.samchips.board.comment.dto;

import connectingchips.samchips.board.entity.Board;
import connectingchips.samchips.board.comment.entity.Comment;
import connectingchips.samchips.user.domain.User;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CommentRequestDto {
    private Long userId;
    private Long boardId;
    private String content;

    /* Dto -> Entity */
    public Comment toEntity(Board board, User user){
        return Comment.builder()
                .board(board)
                .user(user)
                .content(content)
                .build();
    }
}
