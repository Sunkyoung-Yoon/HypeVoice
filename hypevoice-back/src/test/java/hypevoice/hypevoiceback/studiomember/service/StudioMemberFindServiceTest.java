package hypevoice.hypevoiceback.studiomember.service;


import hypevoice.hypevoiceback.common.ServiceTest;
import hypevoice.hypevoiceback.global.exception.BaseException;
import hypevoice.hypevoiceback.member.domain.Member;
import hypevoice.hypevoiceback.studio.domain.Studio;
import hypevoice.hypevoiceback.studiomember.domain.StudioMember;
import hypevoice.hypevoiceback.studiomember.exception.StudioMemberErrorCode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static hypevoice.hypevoiceback.fixture.MemberFixture.GABIN;
import static hypevoice.hypevoiceback.fixture.MemberFixture.SUNKYOUNG;
import static hypevoice.hypevoiceback.fixture.StudioFixture.STUDIO_FIXTURE1;
import static hypevoice.hypevoiceback.fixture.StudioFixture.STUDIO_FIXTURE2;
import static hypevoice.hypevoiceback.fixture.StudioMemberFixture.STUDIO_MEMBER_FIXTURE1;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("StudioMember [Service Layer] -> StudioMemberFindService 테스트")
public class StudioMemberFindServiceTest extends ServiceTest {

    @Autowired
    private StudioMemberFindService studioMemberFindService;

    StudioMember studioMember;
    private Studio[] studio = new Studio[2];
    private Member[] member = new Member[2];

    @BeforeEach
    void setUp() {
        studio[0] = studioRepository.save(STUDIO_FIXTURE1.toStudio());
        studio[1] = studioRepository.save(STUDIO_FIXTURE2.toStudio());
        member[0] = memberRepository.save(SUNKYOUNG.toMember());
        member[1] = memberRepository.save(GABIN.toMember());

        studioMember = studioMemberRepository.save(STUDIO_MEMBER_FIXTURE1.toStudioMember(member[0], studio[0]));
    }


    @Test
    @DisplayName("ID(PK) 로 스튜디오 참여자 정보를 조회한다")
    void findById() {
        // when
        StudioMember findStudioMember = studioMemberFindService.findById(1L);

        // then
        assertThat(findStudioMember).usingRecursiveComparison().isEqualTo(studioMember);
        assertThatThrownBy(() -> studioMemberFindService.findById(studioMember.getId() + 100L))
                .isInstanceOf(BaseException.class)
                .hasMessage(StudioMemberErrorCode.STUDIO_MEMBER_NOT_FOUND.getMessage());


    }
}