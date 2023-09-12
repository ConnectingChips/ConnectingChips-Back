package connectingchips.samchips.comment.dto;

import connectingchips.samchips.board.entity.Board;
import connectingchips.samchips.user.domain.User;

public class CommentResponseDto {
    private Long id;
    private Long boardId;
    private Long userId;
    private String userName;
    private String content;
    private String profileImage;
    private String createDate;
}
