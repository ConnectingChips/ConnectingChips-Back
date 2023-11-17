package connectingchips.samchips.board.dto.board;

import connectingchips.samchips.board.entity.Board;
import connectingchips.samchips.board.dto.comment.CommentResponseDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
public class BoardResponseDto {
    @Getter
    public static class Read{
        private Long boardId;
        private String content;
        private String image;
        private Long userId;
        private String nickname;
        private String profileImage;
        private String createDate;
        private int commentCount;
        private List<CommentResponseDto.Read> commentList;

        public Read(Board board) {
            this.boardId = board.getBoardId();
            this.content = board.getContent();
            this.image = board.getImage();

            this.userId = board.getUser().getId();
            this.nickname = board.getUser().getNickname();
            this.profileImage = board.getUser().getProfileImage();

            StringBuilder sb = new StringBuilder();
            sb.append(board.getCreatedAt().getYear()).append("년 ")
                    .append(board.getCreatedAt().getMonth().getValue()).append("월 ")
                    .append(board.getCreatedAt().getDayOfMonth()).append("일");
            this.createDate = sb.toString();
            this.commentCount = 0;
            this.commentList = new ArrayList<>();
        }
        public void editRead(int commentCount, List<CommentResponseDto.Read> commentList) {
            this.commentCount = commentCount;
            this.commentList = commentList;
        }
    }

    @Getter
    @AllArgsConstructor
    public static class Update{
        private Long boardId;
        private String content;
    }

    @Getter
    @AllArgsConstructor
    public static class CanEdit {
        private boolean canEdit;
    }
}
