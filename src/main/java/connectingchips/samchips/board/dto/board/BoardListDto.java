package connectingchips.samchips.board.dto.board;

import connectingchips.samchips.board.dto.comment.CommentResponseDto;
import connectingchips.samchips.board.dto.comment.ReplyResponseDto;

import java.util.List;

public class BoardListDto {

    public static class CommentToReply {

    }

    private CommentResponseDto.Read commentList;
    private List<ReplyResponseDto> replyList;
}
