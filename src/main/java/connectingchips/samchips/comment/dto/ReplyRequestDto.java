package connectingchips.samchips.comment.dto;

import connectingchips.samchips.board.entity.Board;
import connectingchips.samchips.comment.entity.Comment;
import connectingchips.samchips.comment.entity.Reply;
import connectingchips.samchips.user.domain.User;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ReplyRequestDto {
    private Long userId;
    private Long CommentId;
    private String content;

    public Reply toEntity(Comment comment, User user){
        return Reply.builder()
                .comment(comment)
                .user(user)
                .build();
    }
}
