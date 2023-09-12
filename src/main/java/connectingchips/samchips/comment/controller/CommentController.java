package connectingchips.samchips.comment.controller;

import connectingchips.samchips.comment.dto.CommentRequestDto;
import connectingchips.samchips.comment.dto.CommentResponseDto;
import connectingchips.samchips.comment.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/comments")
    private ResponseEntity<CommentResponseDto> createComment(@RequestBody CommentRequestDto commentReqDto) {
        CommentResponseDto comment = commentService.createComment(commentReqDto);
        return ResponseEntity.ok(comment);
    }

    @DeleteMapping("/comments/{commentId}")
    private ResponseEntity<Void> deleteComment(@PathVariable Long commentId){
        commentService.deleteComment(commentId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/replies")
    private ResponseEntity<CommentResponseDto> createReply(@RequestBody CommentRequestDto commentReqDto) {
        CommentResponseDto comment = commentService.createComment(commentReqDto);
        return ResponseEntity.ok(comment);
    }

    @DeleteMapping("/replies/{replyId}")
    private ResponseEntity<Void> deleteReply(@PathVariable Long replyId){
        commentService.deleteComment(replyId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
