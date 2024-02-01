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

    @Column(unique = true)
    private String nickname;
    private String profileUrl;

    @Convert(converter = Role.RoleConverter.class)
    private Role role;

    @Convert(converter = SocialType.SocialTypeConverter.class)
    private SocialType socialType; // NAVER, GOOGLE
    private String socialId; // 해당 OAuth 의 key(id)


    @Builder
    private Member(String username, String email, SocialType socialType, String socialId) {
        this.username = username;
        this.email = email;
        this.profileUrl = null;
        this.role = Role.GUEST;
        this.socialType = socialType;
        this.socialId = socialId;
    }

    public static Member createMember(String username, String email, SocialType socialType, String socialId){
        return new Member(username, email, socialType, socialId);
    }

    public Member updateMember(String username, String email){
        this.username = username;
        this.email = email;
        return this;
    }

    public void update(String nickname, String profileUrl){
        this.nickname = nickname;
        this.profileUrl = profileUrl;
    }

    public String getRoleKey() {
        return this.role.getAuthority();
    }

    public void updateRole(Role role) {
        this.role = role;
    }

}
