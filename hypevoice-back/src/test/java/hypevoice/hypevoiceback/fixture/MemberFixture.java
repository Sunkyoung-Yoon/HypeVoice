package hypevoice.hypevoiceback.fixture;

import hypevoice.hypevoiceback.member.domain.Member;
import hypevoice.hypevoiceback.member.domain.SocialType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum MemberFixture {
    SUNKYOUNG("선경", "abc@naver.com", "hype1" ,SocialType.NAVER, "id"),
    GABIN("가빈", "def@gmail.com", "hype2", SocialType.KAKAO, "id"),
    JAESIK("재식", "ghi@naver.com", "hype3", SocialType.KAKAO, "id")
    ;

    private final String username;
    private final String email;
    private final String nickname;
    private final SocialType socialType;
    private final String socialId;


    public Member toMember() {
        return Member.createMember(username, email, nickname, socialType, socialId);
    }

}
