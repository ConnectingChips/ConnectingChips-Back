package connectingchips.samchips.commons.dto;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ErrorResponse extends BasicResponse{

    private String message;

    private ErrorResponse(HttpStatus status, String message){
        super(status.value());
        this.message = message;
    }

    public static ErrorResponse of (HttpStatus status, String message){
        return new ErrorResponse(status, message);
    }
}
