package hypevoice.hypevoiceback.studiomember.service;

import hypevoice.hypevoiceback.common.ServiceTest;
import hypevoice.hypevoiceback.global.exception.BaseException;
import hypevoice.hypevoiceback.member.domain.Member;
import hypevoice.hypevoiceback.studio.domain.Studio;
import hypevoice.hypevoiceback.studiomember.domain.StudioMember;
import hypevoice.hypevoiceback.studiomember.exception.StudioMemberErrorCode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static hypevoice.hypevoiceback.fixture.MemberFixture.GABIN;
import static hypevoice.hypevoiceback.fixture.MemberFixture.SUNKYOUNG;
import static hypevoice.hypevoiceback.fixture.StudioFixture.STUDIO_FIXTURE1;
import static hypevoice.hypevoiceback.fixture.StudioFixture.STUDIO_FIXTURE2;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayName("StudioMember [Service Layer] -> StudioMemberService 테스트")
public class StudioMemberServiceTest extends ServiceTest {
    @Autowired
    private StudioMemberService studioMemberService;

    @Autowired
    private StudioMemberFindService studioMemberFindService;

    private Studio[] studio = new Studio[2];
    private Member[] member = new Member[2];

    @BeforeEach
    void setUp() {

        studio[0] = studioRepository.save(STUDIO_FIXTURE1.toStudio());
        studio[1] = studioRepository.save(STUDIO_FIXTURE2.toStudio());
        member[0] = memberRepository.save(SUNKYOUNG.toMember());
        member[1] = memberRepository.save(GABIN.toMember());


    }

    @Test
    @DisplayName("스튜디오 멤버 생성에 성공한다")
    void success() {
        // when

        studioMemberService.create(member[0].getId(), studio[0].getId(), 0);

        // then
        StudioMember findStudioMember = studioMemberRepository.findById(1L).orElseThrow();

        assertAll(

                () -> assertThat(findStudioMember.getMember()).usingRecursiveComparison().isEqualTo(member[0]),
                () -> assertThat(findStudioMember.getStudio()).usingRecursiveComparison().isEqualTo(studio[0]),
                () -> assertThat(findStudioMember.getIsHost()).isEqualTo(0)

        );
    }

    @Nested
    @DisplayName("스튜디오 멤버 삭제")
    class delete {

        @Test
        @DisplayName("스튜디오 멤버 삭제에 성공한다")
        void success() {
            // given
            studioMemberService.create(member[0].getId(), studio[0].getId(), 0);

            studioMemberService.delete(member[0].getId(), studio[0].getId());

            // when - then
            assertThatThrownBy(() -> studioMemberFindService.findById(1L))
                    .isInstanceOf(BaseException.class)
                    .hasMessage(StudioMemberErrorCode.STUDIO_MEMBER_NOT_FOUND.getMessage());
        }
    }

}
