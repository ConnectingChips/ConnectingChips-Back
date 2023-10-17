package connectingchips.samchips.global.commons.dto;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class BasicResponse {

    // Http 상태 코드
    private final Integer statusCode;

    protected BasicResponse(Integer statusCode){
        this.statusCode = statusCode;
    }

    public static BasicResponse of(HttpStatus status){
        return new BasicResponse(status.value());
    }
}
