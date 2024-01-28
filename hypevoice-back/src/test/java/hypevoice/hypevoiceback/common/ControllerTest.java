package hypevoice.hypevoiceback.common;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import hypevoice.hypevoiceback.auth.controller.AuthApiController;
import hypevoice.hypevoiceback.auth.security.CustomOAuth2UserService;
import hypevoice.hypevoiceback.auth.security.OAuth2LoginSuccessHandler;
import hypevoice.hypevoiceback.auth.security.OAuthLoginFailureHandler;
import hypevoice.hypevoiceback.auth.security.jwt.JwtProvider;
import hypevoice.hypevoiceback.auth.service.AuthService;
import hypevoice.hypevoiceback.global.config.SecurityConfig;
import hypevoice.hypevoiceback.member.service.MemberFindService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

@ImportAutoConfiguration(SecurityConfig.class)
@WebMvcTest({
        AuthApiController.class
})
public abstract class ControllerTest {
    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

    @MockBean
    protected MemberFindService memberFindService;

    @MockBean
    protected AuthService authService;

    @MockBean
    protected JwtProvider jwtProvider;

    @MockBean
    protected CustomOAuth2UserService customOAuth2UserService;

    @MockBean
    protected OAuth2LoginSuccessHandler oAuth2LoginSuccessHandler;

    @MockBean
    protected OAuthLoginFailureHandler oAuthLoginFailureHandler;

    protected String convertObjectToJson(Object data) throws JsonProcessingException {
        return objectMapper.writeValueAsString(data);
    }
}
