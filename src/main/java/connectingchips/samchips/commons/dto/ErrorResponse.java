package connectingchips.samchips.commons.dto;

import connectingchips.samchips.exception.AuthErrorCode;
import connectingchips.samchips.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
public class ErrorResponse {

    private final Integer code;
    private final String message;

    private ErrorResponse(Integer code, String message){
        this.code = code;
        this.message = message;
    }

    public static ErrorResponse of(Integer code, String message){
        return new ErrorResponse(code, message);
    }

    public static ErrorResponse of(ErrorCode errorCode){
        return new ErrorResponse(errorCode.getHttpStatus().value(), errorCode.getMessage());
    }

    public static ErrorResponse of(AuthErrorCode authErrorCode){
        return new ErrorResponse(authErrorCode.getCode(), authErrorCode.getMessage());
    }
}
