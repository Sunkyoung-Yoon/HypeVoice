package hypevoice.hypevoiceback.auth.security.jwt;

import hypevoice.hypevoiceback.global.exception.BaseException;
import hypevoice.hypevoiceback.global.exception.GlobalErrorCode;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.Serializable;

@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint, Serializable {
    private static final long serialVersionUID = -7858869558953243875L;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        throw BaseException.type(GlobalErrorCode.UNAUTHORIZED); // 자격증명 없이 접근 -> 401

    }
}
