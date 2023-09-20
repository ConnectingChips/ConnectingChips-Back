package connectingchips.samchips.comment.dto;

import connectingchips.samchips.comment.entity.Comment;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class CommentResponseDto {
    @Getter
    public static class Read {
        private Long commentId;
        private Long boardId;
        private Long userId;
        private String nickname;
        private String content;
        private String profileImage;
        private String createDate;
        private List<ReplyResponseDto> replyList;

        /* Entity -> Dto*/
        public Read (Comment comment){
            this.commentId = comment.getId();
            this.content = comment.getContent();
            this.boardId = comment.getBoard().getBoardId();
            this.userId = comment.getUser().getId();
            this.nickname = comment.getUser().getNickname();
            this.profileImage = comment.getUser().getProfileImage();
            StringBuilder sb = new StringBuilder();
            sb.append(comment.getCreatedAt().getMonth().getValue()).append("월 ")
                    .append(comment.getCreatedAt().getDayOfMonth()).append("일 ");
            if(comment.getCreatedAt().getHour() < 10) sb.append("0");
            sb.append(comment.getCreatedAt().getHour()).append(":");
            if(comment.getCreatedAt().getMinute() < 10) sb.append("0");
            sb.append(comment.getCreatedAt().getMinute());
            this.createDate = sb.toString();
            this.replyList = null;
        }

        public void editRead(List<ReplyResponseDto> commentList) {
            this.replyList = commentList;
        }
    }
}
