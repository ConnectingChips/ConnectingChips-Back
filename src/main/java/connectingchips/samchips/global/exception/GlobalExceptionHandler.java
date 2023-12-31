package connectingchips.samchips.global.exception;

import connectingchips.samchips.global.commons.dto.ErrorResponse;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Objects;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            final MethodArgumentNotValidException e,
            final HttpHeaders headers,
            final HttpStatusCode status,
            final WebRequest request
    ) {
        log.warn(e.getMessage(), e);

        final String errMessage = Objects.requireNonNull(e.getBindingResult().getFieldError()).getDefaultMessage();
        return ResponseEntity.badRequest()
                .body(ErrorResponse.of(CommonErrorCode.INVALID_REQUEST.getHttpStatus().value(), errMessage));
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ErrorResponse> handleBadRequestException(final BadRequestException e) {
        log.warn(e.getMessage(), e);

        return handleExceptionInternal(e.getErrorCode());
    }

    @ExceptionHandler(RestApiException.class)
    public ResponseEntity<ErrorResponse> handleRestApiException(final RestApiException e) {
        log.warn(e.getMessage(), e);

        return handleExceptionInternal(e.getErrorCode());
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponse> constraintViolationException(ConstraintViolationException e) {
        log.warn(e.getMessage(), e);

        return handleExceptionInternal(HttpStatus.BAD_REQUEST, e.getMessage());
    }

    @ExceptionHandler(WebClientResponseException.class)
    public ResponseEntity<ErrorResponse> webClientResponseException(WebClientResponseException e) {
        log.warn(e.getMessage(), e);

        return handleExceptionInternal(e.getStatusCode(), e.getMessage());
    }

    private ResponseEntity<ErrorResponse> handleExceptionInternal(ErrorCode errorCode) {
        ErrorResponse errorResponse;
        if(errorCode instanceof AuthErrorCode){
            errorResponse = ErrorResponse.of((AuthErrorCode) errorCode);
        }else{
            errorResponse = ErrorResponse.of(errorCode);
        }
        return ResponseEntity.status(errorCode.getHttpStatus())
                .body(errorResponse);
    }

    private ResponseEntity<ErrorResponse> handleExceptionInternal(HttpStatus httpStatus, String massage) {
        return ResponseEntity.status(httpStatus)
                .body(ErrorResponse.of(httpStatus.value(), massage));
    }

    private ResponseEntity<ErrorResponse> handleExceptionInternal(HttpStatusCode httpStatus, String massage) {
        return ResponseEntity.status(httpStatus)
                .body(ErrorResponse.of(httpStatus.value(), massage));
    }
}
