package connectingchips.samchips.comment.controller;

import connectingchips.samchips.comment.dto.CommentRequestDto;
import connectingchips.samchips.comment.dto.CommentResponseDto;
import connectingchips.samchips.comment.dto.ReplyRequestDto;
import connectingchips.samchips.comment.dto.ReplyResponseDto;
import connectingchips.samchips.comment.service.CommentService;
import connectingchips.samchips.commons.dto.BasicResponse;
import connectingchips.samchips.commons.dto.DataResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/comments")
    public DataResponse<CommentResponseDto.Read> createComment(@RequestBody CommentRequestDto commentReqDto) {
        CommentResponseDto.Read comment = commentService.createComment(commentReqDto);
        return DataResponse.of(HttpStatus.CREATED, comment);
    }

    @DeleteMapping("/comments/{comment_id}")
    public BasicResponse deleteComment(@PathVariable(value = "comment_id") Long commentId){
        commentService.deleteComment(commentId);
        return BasicResponse.of(HttpStatus.OK);
    }

    @PostMapping("/replies")
    public DataResponse<ReplyResponseDto> createReply(@RequestBody ReplyRequestDto replyRequestDto) {
        ReplyResponseDto reply = commentService.createReply(replyRequestDto);
        return DataResponse.of(HttpStatus.CREATED, reply);
    }

    @DeleteMapping("/replies/{reply_id}")
    public BasicResponse deleteReply(@PathVariable(value = "reply_id") Long replyId){
        System.out.println(replyId);
        commentService.deleteReply(replyId);
        return BasicResponse.of(HttpStatus.OK);
    }
}
