package connectingchips.samchips.board.dto.comment;

import connectingchips.samchips.board.entity.Reply;
import connectingchips.samchips.board.entity.Comment;
import connectingchips.samchips.user.domain.User;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ReplyRequestDto {
    private Long userId;
    private Long commentId;
    private String content;

    public Reply toEntity(Comment comment, User user, String content){
        return Reply.builder()
                .comment(comment)
                .user(user)
                .content(content)
                .build();
    }
}
