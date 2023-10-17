package connectingchips.samchips.board.comment.dto;

import connectingchips.samchips.board.comment.entity.Reply;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ReplyResponseDto {

    private Long replyId;
    private Long commentId;
    private Long userId;
    private String nickname;
    private String profileImage;
    private String content;
    private String createDate;

    /* Entity -> Dto*/
    public ReplyResponseDto(Reply reply){
        this.replyId = reply.getId();
        this.content = reply.getContent();
        this.commentId = reply.getComment().getId();
        this.userId = reply.getUser().getId();
        this.nickname = reply.getUser().getNickname();
        this.profileImage = reply.getUser().getProfileImage();
        StringBuilder sb = new StringBuilder();

        sb.append(reply.getCreatedAt().getMonth().getValue()).append("월 ")
                .append(reply.getCreatedAt().getDayOfMonth()).append("일 ");
        if(reply.getCreatedAt().getHour() < 10) sb.append("0");
        sb.append(reply.getCreatedAt().getHour()).append(":");
        if(reply.getCreatedAt().getMinute() < 10) sb.append("0");
        sb.append(reply.getCreatedAt().getMinute());
        this.createDate = sb.toString();
    }
}
