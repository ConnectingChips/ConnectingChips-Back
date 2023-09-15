package connectingchips.samchips.board.dto;

import connectingchips.samchips.comment.dto.CommentResponseDto;
import connectingchips.samchips.comment.dto.ReplyResponseDto;

import java.util.List;

public class BoardListDto {

    public static class CommentToReply {

    }

    private CommentResponseDto.Read commentList;
    private List<ReplyResponseDto> replyList;
}
