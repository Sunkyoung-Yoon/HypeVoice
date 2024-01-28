package hypevoice.hypevoiceback.auth.security;

import hypevoice.hypevoiceback.member.domain.Member;
import hypevoice.hypevoiceback.member.domain.SocialType;
import lombok.Builder;

import java.util.Map;


@Builder
public record OAuthAttributes (
    Map<String, Object> attributes,
    String nameAttributeKey,
    String username,
    String email,
    SocialType socialType,
    String socialId
) {
    public static OAuthAttributes of(SocialType socialType, String userNameAttributeName,
                                           Map<String,Object> attributes) {
        return switch (socialType) {
            case NAVER -> ofNaver(socialType, userNameAttributeName, attributes);
            case KAKAO -> ofKakao(socialType, userNameAttributeName, attributes);
        };
    }

    private static OAuthAttributes ofNaver(SocialType socialType, String userNameAttributeName,
                                                 Map<String,Object> attributes) {
        Map<String,Object> response = (Map<String, Object>) attributes.get("response");

        return OAuthAttributes.builder()
                .username((String) response.get("name"))
                .email((String) response.get("email"))
                .socialType(socialType)
                .socialId(userNameAttributeName)
                .attributes(attributes)
                .nameAttributeKey(userNameAttributeName)
                .build();
    }

    private static OAuthAttributes ofKakao(SocialType socialType, String userNameAttributeName,
                                                 Map<String,Object> attributes) {
        Map<String,Object> kakao_account = (Map<String, Object>) attributes.get("kakao_account");
        Map<String,Object> profile = (Map<String, Object>) kakao_account.get("profile");

        return OAuthAttributes.builder()
                .username((String) profile.get("nickname"))
                .email((String) kakao_account.get("email"))
                .socialType(socialType)
                .socialId(userNameAttributeName)
                .attributes(attributes)
                .nameAttributeKey(userNameAttributeName)
                .build();
    }

    //User 엔티티 생성 OAuthAttributes에서 엔티티 생성은 처음 가입할 때 이루어진다.
    //가입할 때의 기본권한은 GUEST
    public Member toEntity(){
        return Member.builder()
                .username(username)
                .email(email)
                .socialType(socialType)
                .socialId(socialId)
                .build();
    }
}
