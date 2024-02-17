package hypevoice.hypevoiceback.auth.security;

import hypevoice.hypevoiceback.member.domain.Member;
import hypevoice.hypevoiceback.member.domain.MemberRepository;
import hypevoice.hypevoiceback.member.domain.SocialType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Service
public class CustomOAuth2UserService implements  OAuth2UserService<OAuth2UserRequest, OAuth2User> {
    private final MemberRepository memberRepository;

    private static final String NAVER = "naver";
    private static final String KAKAO = "kakao";

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        log.info("CustomOAuth2UserService.loadUser() 실행 : OAuth2 로그인 요청 진입");

        OAuth2UserService<OAuth2UserRequest, OAuth2User> delegate = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = delegate.loadUser(userRequest);

        String registrationId  = userRequest.getClientRegistration().getRegistrationId();

        SocialType socialType = getSocialType(registrationId);

        String userNameAttributeName =
                userRequest.getClientRegistration()
                        .getProviderDetails().getUserInfoEndpoint()
                        .getUserNameAttributeName();


        // OAuth2 유저 로그인 속성 (attributes)
        Map<String, Object> attributes = oAuth2User.getAttributes();


        // 소셜 타입 따라 OAuthAttributes 객체 생성
        OAuthAttributes extractAttributes = OAuthAttributes.of(socialType, userNameAttributeName, attributes);

        // Member 객체 생성 후 반환
        Member createdMember = saveOrUpdate(extractAttributes, socialType, extractAttributes.email());

        // CustomOAuthUser 객체 생성 반환
        return new CustomOAuth2User(
                Collections.singleton(new SimpleGrantedAuthority(createdMember.getRoleKey())),
                attributes,
                extractAttributes.nameAttributeKey(),
                createdMember.getEmail(),
                createdMember.getSocialType(),
                createdMember.getRole());
    }

    private SocialType getSocialType(String registrationId) {
        if(NAVER.equals(registrationId)) {
            return SocialType.NAVER;
        }
        return SocialType.KAKAO;
    }

    private Member saveOrUpdate(OAuthAttributes attributes, SocialType socialType, String email){
        Member createdMember = memberRepository.findBySocialTypeAndEmail(socialType, email)
                .map(entity -> entity.updateMember(attributes.username(), attributes.email()))
                .orElse(attributes.toEntity());

        return memberRepository.save(createdMember);
    }
}

