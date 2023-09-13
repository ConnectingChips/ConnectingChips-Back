package connectingchips.samchips.comment.dto;

import connectingchips.samchips.comment.entity.Comment;
import connectingchips.samchips.comment.entity.Reply;
import connectingchips.samchips.user.domain.User;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ReplyResponseDto {
    private Long id;
    private Long commentId;
    private Long userId;
    private String nickname;
    private String profileImage;
    private String content;
    private String createDate;

    /* Entity -> Dto*/
    public ReplyResponseDto(User user, Comment comment, Reply reply){
        this.id = reply.getId();
        this.commentId = comment.getId();
        this.userId = user.getId();
        this.nickname = user.getNickname();
        this.profileImage = user.getProfileImage();
        this.content = reply.getContent();
        StringBuilder sb = new StringBuilder();
        sb.append(comment.getCreatedAt().getMonth()).append("월 ")
                .append(comment.getCreatedAt().getDayOfMonth()).append("일 ")
                .append(comment.getCreatedAt().getHour()).append(":").append(comment.getCreatedAt().getMinute());
        this.createDate = sb.toString();
    }
}
