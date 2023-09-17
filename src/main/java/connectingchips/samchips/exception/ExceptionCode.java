package connectingchips.samchips.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum ExceptionCode {

    INVALID_REQUEST(1000, "올바르지 않은 요청입니다."),

    NOT_FOUND_USER_ID(1001, "존재하지 않는 유저 번호입니다."),
    ALREADY_JOIN_MEMBERSHIP(1002, "이미 가입되어 있는 유저입니다."),


    NOT_FOUND_MIND_ID(2001, "존재하지 않는 작심 번호입니다."),
    ALREADY_JOIN_MIND(2002, "이미 참여하고 있는 작심입니다."),
    NOT_JOIN_MIND(2003,"작심에 참여하고 있지 않아 글을 쓸 수 없습니다."),

    NOT_FOUND_JOINED_MIND_ID(3001, "존재하지 않는 참여한 작심 번호입니다."),

    NOT_FOUND_MIND_TYPE_ID(4001, "존재하지 않는 작심 종류 번호입니다."),

    NOT_FOUND_BOARD_ID(5001, "존재하지 않는 게시글 번호입니다."),
    ALREADY_WRITE_BOARD(5002, "오늘 해당 작심 게시글을 작성했습니다"),

    NOT_FOUND_COMMENT_ID(6001, "존재하지 않는 댓글 번호입니다."),

    NOT_FOUND_REPLY_ID(7001,"존재하지 않는 답글 번호입니다.");


    private final int code;
    private final String message;

}
