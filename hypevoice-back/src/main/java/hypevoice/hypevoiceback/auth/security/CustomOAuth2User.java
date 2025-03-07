package hypevoice.hypevoiceback.auth.security;

import hypevoice.hypevoiceback.member.domain.Role;
import hypevoice.hypevoiceback.member.domain.SocialType;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;

import java.util.Collection;
import java.util.Map;

@Getter
public class CustomOAuth2User extends DefaultOAuth2User {

    private String email;
    private SocialType socialType;
    private Role role;

    public CustomOAuth2User (Collection<? extends GrantedAuthority> authorities,
                             Map<String, Object> attributes,
                             String nameAttributeKey,
                             String email,
                             SocialType socialType,
                             Role role
                             ) {

        super(authorities, attributes, nameAttributeKey);
        this.email = email;
        this.socialType = socialType;
        this.role = role;
    }

}