package connectingchips.samchips.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum AuthErrorCode implements ErrorCode{

    // 401 UNAUTHORIZED 인증 정보가 없는 경우
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, 401,"유효한 인증 정보가 없습니다."),
    INVALID_TOKEN(HttpStatus.UNAUTHORIZED, 4011,"유효하지 않은 JWT 서명입니다."),
    EXPIRED_TOKEN(HttpStatus.UNAUTHORIZED, 4012,"만료된 JWT 토큰입니다."),
    UNSUPPORTED_TOKEN(HttpStatus.UNAUTHORIZED, 4013,"지원하지 않는 JWT 토큰입니다."),
    ILLEGAL_ARGUMENT_TOKEN(HttpStatus.UNAUTHORIZED, 4014,"잘못된 JWT 토큰입니다."),
    INVALID_REFRESH_TOKEN(HttpStatus.UNAUTHORIZED, 4014,"유효하지 않은 Refresh 토큰입니다."),

    // 403 FORBIDDEN 권한이 없는 경우
    FORBIDDEN(HttpStatus.FORBIDDEN, 403,"해당 요청에 대한 권한이 없습니다.");

    private final HttpStatus httpStatus;
    private final Integer code;
    private final String message;
}
