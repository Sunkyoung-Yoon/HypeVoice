package hypevoice.hypevoiceback.fixture;

import hypevoice.hypevoiceback.member.domain.Member;
import hypevoice.hypevoiceback.studio.domain.Studio;
import hypevoice.hypevoiceback.studiomember.domain.StudioMember;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum StudioMemberFixture {

    STUDIO_MEMBER_FIXTURE1(1),
    STUDIO_MEMBER_FIXTURE2(0);


    private final int isHost;


    public StudioMember toStudioMember(Member member, Studio studio) {
        return StudioMember.createStudioMember(member, studio, isHost);
    }
}
