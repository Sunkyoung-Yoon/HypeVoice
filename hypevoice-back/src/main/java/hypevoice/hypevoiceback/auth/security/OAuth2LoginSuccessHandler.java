package hypevoice.hypevoiceback.auth.security;

import hypevoice.hypevoiceback.auth.dto.LoginResponse;
import hypevoice.hypevoiceback.auth.service.AuthService;
import hypevoice.hypevoiceback.global.utils.CookieUtil;
import hypevoice.hypevoiceback.member.domain.Member;
import hypevoice.hypevoiceback.member.domain.Role;
import hypevoice.hypevoiceback.member.domain.SocialType;
import hypevoice.hypevoiceback.member.service.MemberFindService;
import hypevoice.hypevoiceback.member.service.MemberService;
import hypevoice.hypevoiceback.voice.service.VoiceService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.time.Duration;
import java.util.Optional;

import static hypevoice.hypevoiceback.auth.security.CookieAuthorizationRequestRepository.REDIRECT_URI_PARAM_COOKIE_NAME;
import static org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames.ACCESS_TOKEN;
import static org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames.REFRESH_TOKEN;

@Slf4j
@Component
@RequiredArgsConstructor
public class OAuth2LoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    public static final Duration REFRESH_TOKEN_DURATION = Duration.ofDays(15);
    public static final Duration ACCESS_TOKEN_DURATION = Duration.ofHours(1);
    private final MemberFindService memberFindService;
    private final MemberService memberService;

    private final AuthService authService;
    private final VoiceService voiceService;

    private final CookieAuthorizationRequestRepository authorizationRequestRepository;

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
            memberService.update(loginMember.getId(), initialNickname(loginMember.getId()), null);
            memberService.updateRole(loginMember.getId());
            voiceService.createVoice(loginMember.getId(), loginMember.getUsername());
        }
        else{
            log.info("기존 회원 로그인");
        }

        // 로그인 성공 -> 토큰 발급
        LoginResponse loginResponse = authService.login(socialType, email);

        String targetUrl = determineTargetUrl(request, response, loginResponse);

        if (response.isCommitted()) {
            log.debug("Response has already been committed. Unable to redirect to " + targetUrl);
            return;
        }

        clearAuthenticationAttributes(request, response);
        getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }

    // 회원가입한 유저 로그인 시 랜덤 닉네임으로 변경
    private static String initialNickname(Long id) {
        String number = String.valueOf((int)(Math.random() * 99) + 10);
        return "voice"+number+String.valueOf(id);
    }

    protected String determineTargetUrl(HttpServletRequest request, HttpServletResponse response, LoginResponse loginResponse) {
        Optional<String> redirectUri = CookieUtil.getCookie(request, REDIRECT_URI_PARAM_COOKIE_NAME)
                .map(Cookie::getValue);

        String targetUrl = redirectUri.orElse("/");

        int cookieMaxAge = (int) REFRESH_TOKEN_DURATION.toSeconds();
        int cookieMaxAgeForAccess = (int) ACCESS_TOKEN_DURATION.toSeconds() / 1000;


        // Access Token 저장
        CookieUtil.deleteCookie(request, response, ACCESS_TOKEN);
        CookieUtil.addCookieForAccess(response, ACCESS_TOKEN, loginResponse.accessToken(), cookieMaxAgeForAccess);

        // Refresh Token 저장
        CookieUtil.deleteCookie(request, response, REFRESH_TOKEN);
        CookieUtil.addCookie(response, REFRESH_TOKEN, loginResponse.tokenResponse().refreshToken(), cookieMaxAge);

        return UriComponentsBuilder.fromUriString(targetUrl)
                .build()
                .toUriString();
    }

    //인증정보 요청 내역에서 쿠키를 삭제
    protected void clearAuthenticationAttributes(HttpServletRequest request, HttpServletResponse response) {
        super.clearAuthenticationAttributes(request);
        authorizationRequestRepository.removeAuthorizationRequestCookies(request, response);
    }
}