package hypevoice.hypevoiceback.member.domain;

import hypevoice.hypevoiceback.global.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor
@Table(name="members")
public class Member extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    private String username;
    private String email;
    private String nickname;
    private String profileUrl;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Convert(converter = SocialType.SocialTypeConverter.class)
    private SocialType socialType; // NAVER, GOOGLE
    private String socialId; // 해당 OAuth 의 key(id)


    @Builder
    private Member(String username, String email, String nickname, SocialType socialType, String socialId) {
        this.username = username;
        this.email = email;
        this.nickname = nickname;
        this.role = Role.USER;
        this.socialType = socialType;
        this.socialId = socialId;
    }

    public static Member createMember(String username, String email, String nickname, SocialType socialType, String socialId){
        return new Member(username, email, nickname, socialType, socialId);
    }

    public void updateMember(String username, String email){
        this.username = username;
        this.email = email;
    }

    public void updateNickname(String nickname){
        this.nickname = nickname;
    }

    public void updateProfileUrl(String profileUrl){
        this.profileUrl = profileUrl;
    }

}
