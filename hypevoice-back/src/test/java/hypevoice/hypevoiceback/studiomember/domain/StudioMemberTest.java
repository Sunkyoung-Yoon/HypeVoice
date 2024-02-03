package hypevoice.hypevoiceback.studiomember.domain;


import hypevoice.hypevoiceback.member.domain.Member;
import hypevoice.hypevoiceback.studio.domain.Studio;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static hypevoice.hypevoiceback.fixture.MemberFixture.SUNKYOUNG;
import static hypevoice.hypevoiceback.fixture.StudioFixture.STUDIO_FIXTURE1;
import static hypevoice.hypevoiceback.fixture.StudioMemberFixture.STUDIO_MEMBER_FIXTURE1;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayName("StudioMember 도메인 테스트")
public class StudioMemberTest {


    @Test
    @DisplayName("StudioMember를 생성한다")
    public void createStudioMember() {
        Member member = SUNKYOUNG.toMember();
        Studio studio = STUDIO_FIXTURE1.toStudio();
        StudioMember studioMember = STUDIO_MEMBER_FIXTURE1.toStudioMember(member,studio);

        assertAll(
                () -> assertThat(studioMember.getMember()).usingRecursiveComparison().isEqualTo(member),
                () -> assertThat(studioMember.getStudio()).usingRecursiveComparison().isEqualTo(studio),
                () -> assertThat(studioMember.getIsHost()).isEqualTo(STUDIO_MEMBER_FIXTURE1.getIsHost())
        );
    }

}
