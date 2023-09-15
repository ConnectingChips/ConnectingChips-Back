package connectingchips.samchips.board.dto;

import connectingchips.samchips.board.entity.Board;
import connectingchips.samchips.comment.entity.Comment;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class BoardResponseDto {

    /**
     *    "status" : 200
     *    "data": [
     *         {
     *             "id": 1, <- BoardId
     *             "userId": 1,
     *             "userName": "칩스",
     *             "content": "내용",
     *             "createDate": "2023년 09월 06일",
     *             "likeCount": 16,
     *             "canLike": true,
     *             "image", "이미지",
     *             "commentCount" : "4"
     *             "commentList":[
     *                   {
     *                        "id": 2, <- CommentId
     *                        "userId": 2,
     *                        "userName": "프론트칩스",
     *                        "content": "내용",
     *                        "profileImage": "프로필 이미지",
     *                        "createDate": "09월 07일 14:26"
     *                        "replyList:"[
     *                             {
     *                                  "id": 3, <- ReplyId
     *                                  "userId": 3,
     *                                  "userName": "백엔드칩스",
     *                                  "content": "내용",
     *                                  "profileImage": "프로필 이미지"
     *                                  "createDate": "09월 07일 14:26"
     *                             },
     */
    public static class Read{
        private Long boardId;
        private String content;
        private String image;
        private Long userId;
        private String nickname;
        private String createDate;

        public Read(Board board) {
            this.boardId = board.getBoardId();
            this.content = board.getContent();
            this.image = board.getImage();

            this.userId = board.getUser().getId();
            this.nickname = board.getUser().getNickname();

            StringBuilder sb = new StringBuilder();
            sb.append(board.getCreatedAt().getDayOfYear()).append("년 ")
                    .append(board.getCreatedAt().getMonth()).append("월 ")
                    .append(board.getCreatedAt().getDayOfMonth()).append("일");
            this.createDate = sb.toString();

        }

    }

    public static class Update{
        private Long boardId;
        private String content;

        public Update(Long boardId, String content){
            this.boardId = boardId;
            this.content = content;
        }
    }

    public static class CanEdit {
        private boolean canEdit;

        public CanEdit(boolean isUserEditer) {
            this.canEdit = isUserEditer;
        }
    }
}
