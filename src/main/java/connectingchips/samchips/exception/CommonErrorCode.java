package connectingchips.samchips.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum CommonErrorCode implements ErrorCode{

    // 400 BAD_REQUEST 잘못된 요청
    INVALID_REQUEST(HttpStatus.BAD_REQUEST, "올바르지 않은 요청입니다."),

    // 404 NOT_FOUND 잘못된 리소스 접근
    NOT_FOUND_USER(HttpStatus.NOT_FOUND, "존재하지 않는 유저입니다."),
    NOT_FOUND_MIND_ID(HttpStatus.NOT_FOUND, "존재하지 않는 작심 번호입니다."),
    NOT_JOIN_MIND(HttpStatus.NOT_FOUND,"작심에 참여하고 있지 않습니다."),
    NOT_FOUND_JOINED_MIND_ID(HttpStatus.NOT_FOUND, "존재하지 않는 참여한 작심 번호입니다."),
    NOT_FOUND_MIND_TYPE_ID(HttpStatus.NOT_FOUND, "존재하지 않는 작심 종류 번호입니다."),
    NOT_FOUND_BOARD_ID(HttpStatus.NOT_FOUND, "존재하지 않는 게시글 번호입니다."),
    NOT_FOUND_COMMENT_ID(HttpStatus.NOT_FOUND, "존재하지 않는 댓글 번호입니다."),
    NOT_FOUND_REPLY_ID(HttpStatus.NOT_FOUND,"존재하지 않는 답글 번호입니다."),

    // 409 CONFLICT 중복된 리소스
    ALREADY_JOIN_MEMBERSHIP(HttpStatus.CONFLICT, "이미 가입되어 있는 유저입니다."),
    ALREADY_JOIN_MIND(HttpStatus.CONFLICT, "이미 참여하고 있는 작심입니다."),
    ALREADY_WRITE_BOARD(HttpStatus.CONFLICT, "오늘 해당 작심 게시글을 작성했습니다");

    private final HttpStatus httpStatus;
    private final String message;
}
