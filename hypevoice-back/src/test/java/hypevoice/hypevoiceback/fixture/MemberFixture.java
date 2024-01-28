package hypevoice.hypevoiceback.fixture;

import hypevoice.hypevoiceback.member.domain.Member;
import hypevoice.hypevoiceback.member.domain.SocialType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum MemberFixture {
    SUNKYOUNG("선경", "abc@naver.com",SocialType.NAVER, "id"),
    GABIN("가빈", "def@gmail.com", SocialType.KAKAO, "id"),
    JAESIK("재식", "ghi@naver.com", SocialType.KAKAO, "id")
    ;

    private final String username;
    private final String email;
    private final SocialType socialType;
    private final String socialId;


    public Member toMember() {
        return Member.createMember(username, email, socialType, socialId);
    }

}
