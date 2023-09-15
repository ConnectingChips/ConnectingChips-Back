package connectingchips.samchips.comment.dto;

import connectingchips.samchips.board.entity.Board;
import connectingchips.samchips.comment.entity.Comment;
import connectingchips.samchips.user.domain.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Getter
@NoArgsConstructor
public class CommentResponseDto {

    public static class ReturnList {
        private int commentCount;
        private List<Comment> commentList;
    }

    public static class Read {
        private Long commentId;
        private Long boardId;
        private Long userId;
        private String nickname;
        private String content;
        private String profileImage;
        private String createDate;

        /* Entity -> Dto*/
        public Read (Comment comment){
            this.commentId = comment.getId();
            this.content = comment.getContent();
            this.boardId = comment.getBoard().getBoardId();
            this.userId = comment.getUser().getId();
            this.nickname = comment.getUser().getNickname();
            this.profileImage = comment.getUser().getProfileImage();
            // 09월 07일 14:2
            StringBuilder sb = new StringBuilder();
            sb.append(comment.getCreatedAt().getMonth()).append("월 ")
                    .append(comment.getCreatedAt().getDayOfMonth()).append("일 ")
                    .append(comment.getCreatedAt().getHour()).append(":").append(comment.getCreatedAt().getMinute());
            this.createDate = sb.toString();
        }
    }
}
