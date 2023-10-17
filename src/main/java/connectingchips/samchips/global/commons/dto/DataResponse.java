package connectingchips.samchips.global.commons.dto;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class DataResponse<T> extends BasicResponse {

    private final T data;

    private DataResponse(T data){
        super(HttpStatus.OK.value());
        this.data = data;
    }

    private DataResponse(HttpStatus status, T data){
        super(status.value());
        this.data = data;
    }

    public static <T> DataResponse<T> of(T data){
        return new DataResponse<>(data);
    }

    public static <T> DataResponse<T> of(HttpStatus status, T data){
        return new DataResponse<>(status, data);
    }
}
