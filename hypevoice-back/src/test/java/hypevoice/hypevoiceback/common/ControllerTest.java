package hypevoice.hypevoiceback.common;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import hypevoice.hypevoiceback.auth.controller.AuthApiController;
import hypevoice.hypevoiceback.auth.security.CustomOAuth2UserService;
import hypevoice.hypevoiceback.auth.security.OAuth2LoginSuccessHandler;
import hypevoice.hypevoiceback.auth.security.OAuthLoginFailureHandler;
import hypevoice.hypevoiceback.auth.security.jwt.JwtAccessDeniedHandler;
import hypevoice.hypevoiceback.auth.security.jwt.JwtAuthenticationEntryPoint;
import hypevoice.hypevoiceback.auth.security.jwt.JwtProvider;
import hypevoice.hypevoiceback.auth.service.AuthService;
import hypevoice.hypevoiceback.board.controller.BoardApiController;
import hypevoice.hypevoiceback.board.service.BoardFindService;
import hypevoice.hypevoiceback.board.service.BoardService;
import hypevoice.hypevoiceback.comment.controller.CommentApiController;
import hypevoice.hypevoiceback.comment.service.CommentFindService;
import hypevoice.hypevoiceback.comment.service.CommentService;
import hypevoice.hypevoiceback.global.config.SecurityConfig;
import hypevoice.hypevoiceback.member.service.MemberFindService;
import hypevoice.hypevoiceback.voice.controller.VoiceController;
import hypevoice.hypevoiceback.voice.controller.like.VoiceLikeController;
import hypevoice.hypevoiceback.voice.service.VoiceFindService;
import hypevoice.hypevoiceback.voice.service.VoiceService;
import hypevoice.hypevoiceback.voice.service.like.VoiceLikeService;
import hypevoice.hypevoiceback.work.controller.WorkController;
import hypevoice.hypevoiceback.work.service.WorkFindService;
import hypevoice.hypevoiceback.work.service.WorkService;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;

@ImportAutoConfiguration(SecurityConfig.class)
@WebMvcTest({
        AuthApiController.class,
        BoardApiController.class,
        CommentApiController.class,
        VoiceController.class,
        VoiceLikeController.class,
        WorkController.class
})
@WithMockUser("test")
public abstract class ControllerTest {


    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    protected MockMvc mockMvc;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(webApplicationContext)
                .apply(springSecurity())
                .build();
    }

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

    @MockBean
    protected JwtAccessDeniedHandler jwtAccessDeniedHandler;

    @MockBean
    protected JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    @MockBean
    protected BoardFindService boardFindService;

    @MockBean
    protected BoardService boardService;

    @MockBean
    protected CommentFindService commentFindService;

    @MockBean
    protected CommentService commentService;

    @MockBean
    protected VoiceService voiceService;

    @MockBean
    protected VoiceFindService voiceFindService;

    @MockBean
    protected VoiceLikeService voiceLikeService;

    @MockBean
    protected WorkService workService;

    @MockBean
    protected WorkFindService workFindService;

    protected String convertObjectToJson(Object data) throws JsonProcessingException {
        return objectMapper.writeValueAsString(data);
    }
}
