package connectingchips.samchips.board.comment.controller;

import connectingchips.samchips.board.comment.dto.CommentRequestDto;
import connectingchips.samchips.board.comment.dto.CommentResponseDto;
import connectingchips.samchips.board.comment.dto.ReplyRequestDto;
import connectingchips.samchips.board.comment.dto.ReplyResponseDto;
import connectingchips.samchips.board.comment.service.CommentService;
import connectingchips.samchips.global.commons.dto.BasicResponse;
import connectingchips.samchips.global.commons.dto.DataResponse;
import connectingchips.samchips.user.domain.LoginUser;
import connectingchips.samchips.user.domain.User;
import connectingchips.samchips.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;
    private final UserService userService;

    /* 댓글 추가 */
    @PostMapping("/comments")
    @PreAuthorize("hasRole('USER')")
    public DataResponse<CommentResponseDto.Read> createComment(@RequestBody CommentRequestDto commentReqDto) {
        CommentResponseDto.Read comment = commentService.createComment(commentReqDto);
        return DataResponse.of(HttpStatus.CREATED, comment);
    }

    /* 댓글 삭제 */
    @DeleteMapping("/comments/{comment_id}")
    @PreAuthorize("hasRole('USER')")
    public BasicResponse deleteComment(@PathVariable(value = "comment_id") Long commentId,
                                       @LoginUser User loginUser){
        userService.isLoginUser(loginUser, commentService.getCommentById(commentId).getUser());
        commentService.deleteComment(commentId);
        return BasicResponse.of(HttpStatus.OK);
    }

    /* 답글 추가 */
    @PostMapping("/replies")
    @PreAuthorize("hasRole('USER')")
    public DataResponse<ReplyResponseDto> createReply(@RequestBody ReplyRequestDto replyRequestDto) {
        ReplyResponseDto reply = commentService.createReply(replyRequestDto);
        return DataResponse.of(HttpStatus.CREATED, reply);
    }

    /* 답글 삭제 */
    @DeleteMapping("/replies/{reply_id}")
    @PreAuthorize("hasRole('USER')")
    public BasicResponse deleteReply(@PathVariable(value = "reply_id") Long replyId,
                                     @LoginUser User loginUser){
        userService.isLoginUser(loginUser, commentService.getReplyById(replyId).getUser());
        commentService.deleteReply(replyId);
        return BasicResponse.of(HttpStatus.OK);
    }
}
