package connectingchips.samchips.comment.dto;

import connectingchips.samchips.board.entity.Board;
import connectingchips.samchips.comment.entity.Comment;
import connectingchips.samchips.user.domain.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
@NoArgsConstructor
public class CommentResponseDto {
    private Long id;
    private Long boardId;
    private Long userId;
    private String nickname;
    private String content;
    private String profileImage;
    private String createDate;

    /* Entity -> Dto*/
    public CommentResponseDto (User user, Board board, Comment comment){
        this.id = comment.getId();
        this.boardId = board.getBoardId();
        this.userId = user.getId();
        this.nickname = user.getNickname();
        this.content = comment.getContent();
        this.profileImage = user.getProfileImage();
        // 09월 07일 14:2
        StringBuilder sb = new StringBuilder();
        sb.append(comment.getCreatedAt().getMonth()).append("월 ")
                        .append(comment.getCreatedAt().getDayOfMonth()).append("일 ")
                        .append(comment.getCreatedAt().getHour()).append(":").append(comment.getCreatedAt().getMinute());
        this.createDate = sb.toString();
    }
}
