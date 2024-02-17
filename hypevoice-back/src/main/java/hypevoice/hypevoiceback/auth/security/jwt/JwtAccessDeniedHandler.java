package hypevoice.hypevoiceback.auth.security.jwt;

import hypevoice.hypevoiceback.global.exception.BaseException;
import hypevoice.hypevoiceback.global.exception.GlobalErrorCode;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class JwtAccessDeniedHandler implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        throw BaseException.type(GlobalErrorCode.INVALID_USER); // 필요한 권한 x -> 403

    }
}
