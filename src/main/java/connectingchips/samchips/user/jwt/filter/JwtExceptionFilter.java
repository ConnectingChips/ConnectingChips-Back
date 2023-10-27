package connectingchips.samchips.user.jwt.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import connectingchips.samchips.global.commons.dto.ErrorResponse;
import connectingchips.samchips.global.exception.AuthErrorCode;
import connectingchips.samchips.global.exception.RestApiException;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

// Jwt 토큰 관련 예외 처리
@Component
public class JwtExceptionFilter extends OncePerRequestFilter {

    private ObjectMapper objectMapper;

    @PostConstruct
    void init() {
        objectMapper = new ObjectMapper();
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try{
            filterChain.doFilter(request, response);
        }catch (RestApiException e){
            AuthErrorCode authErrorCode = (AuthErrorCode) e.getErrorCode();
            setErrorResponse(response, authErrorCode);
        }
    }

    private void setErrorResponse(HttpServletResponse response, AuthErrorCode authErrorCode) throws IOException {
        ErrorResponse errorResponse = ErrorResponse.of(authErrorCode);

        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(authErrorCode.getHttpStatus().value());
        response.getWriter().print(objectMapper.writeValueAsString(errorResponse));
    }
}
