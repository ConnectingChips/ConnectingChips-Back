package connectingchips.samchips.user.jwt.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import connectingchips.samchips.commons.dto.ErrorResponse;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

// 인증 실패 시
@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private ObjectMapper objectMapper;

    @PostConstruct
    void init() {
        objectMapper = new ObjectMapper();
    }

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        // 유효한 자격증명을 제공하지 않고 접근하려 할때 401(인증 실패)
        ErrorResponse errorResponse = ErrorResponse.of(HttpStatus.UNAUTHORIZED.value(), "UNAUTHORIZED");

        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.getOutputStream().print(objectMapper.writeValueAsString(errorResponse));
    }
}
