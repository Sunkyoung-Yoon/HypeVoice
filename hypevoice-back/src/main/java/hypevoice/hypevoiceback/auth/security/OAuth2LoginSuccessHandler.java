package hypevoice.hypevoiceback.auth.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import hypevoice.hypevoiceback.auth.dto.LoginResponse;
import hypevoice.hypevoiceback.auth.service.AuthService;
import hypevoice.hypevoiceback.member.domain.Member;
import hypevoice.hypevoiceback.member.domain.Role;
import hypevoice.hypevoiceback.member.domain.SocialType;
import hypevoice.hypevoiceback.member.service.MemberFindService;
import hypevoice.hypevoiceback.member.service.MemberService;
import hypevoice.hypevoiceback.voice.service.VoiceService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class OAuth2LoginSuccessHandler implements AuthenticationSuccessHandler {

    private final MemberFindService memberFindService;
    private final MemberService memberService;

    private final AuthService authService;
    private final ObjectMapper objectMapper;

    private final VoiceService voiceService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response
            , Authentication authentication) throws IOException {

        CustomOAuth2User oAuth2User = (CustomOAuth2User) authentication.getPrincipal();

        SocialType socialType = oAuth2User.getSocialType();
        String email = oAuth2User.getEmail();
        Role role = oAuth2User.getRole();

        Member loginMember = memberFindService.findBySocialTypeAndEmail(socialType, email);
        // 처음 로그인한 유저라면 랜덤 닉네임 설정 필요
        if(role == Role.GUEST){
            memberService.updateNickname(loginMember.getId(), initialNickname(loginMember.getId()));
            memberService.updateRole(loginMember.getId());
            voiceService.createVoice(loginMember.getId(), loginMember.getUsername());
        }
        else{
            log.info("기존 회원 로그인");
        }

        // 반환할 DTO
        LoginResponse loginResponse = authService.login(socialType, email);

        clearAuthenticationAttributes(request);

        response.setStatus(HttpStatus.OK.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");
        objectMapper.writeValue(response.getWriter(), loginResponse);
    }

    protected void clearAuthenticationAttributes(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if(session==null) return;
        session.removeAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
    }

    // 회원가입한 유저 로그인 시 랜덤 닉네임으로 변경
    private static String initialNickname(Long id) {
        String number = String.valueOf((int)(Math.random() * 99) + 10);
        return "voice"+number+String.valueOf(id);
    }
}