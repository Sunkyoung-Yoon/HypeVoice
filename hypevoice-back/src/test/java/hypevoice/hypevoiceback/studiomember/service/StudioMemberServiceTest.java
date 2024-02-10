package hypevoice.hypevoiceback.studiomember.service;

import hypevoice.hypevoiceback.common.ServiceTest;
import hypevoice.hypevoiceback.global.exception.BaseException;
import hypevoice.hypevoiceback.member.domain.Member;
import hypevoice.hypevoiceback.member.dto.MemberResponse;
import hypevoice.hypevoiceback.studio.domain.Studio;
import hypevoice.hypevoiceback.studio.dto.StudioResponse;
import hypevoice.hypevoiceback.studiomember.domain.StudioMember;
import hypevoice.hypevoiceback.studiomember.exception.StudioMemberErrorCode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static hypevoice.hypevoiceback.fixture.MemberFixture.*;
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
    private Member[] member = new Member[3];

    @BeforeEach
    void setUp() {

        studio[0] = studioRepository.save(STUDIO_FIXTURE1.toStudio());
        studio[1] = studioRepository.save(STUDIO_FIXTURE2.toStudio());

        member[0] = memberRepository.save(SUNKYOUNG.toMember());
        member[1] = memberRepository.save(GABIN.toMember());
        member[2] = memberRepository.save(JAESIK.toMember());


    }

    @Test
    @DisplayName("스튜디오 멤버 생성에 성공한다")
    void success() {
        // when

        studioMemberService.create(member[0].getId(), studio[0].getId(), 0, "connection1");

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
            studioMemberService.create(member[0].getId(), studio[0].getId(), 0, "connection1");

            studioMemberService.delete(member[0].getId(), studio[0].getId());

            // when - then
            assertThatThrownBy(() -> studioMemberFindService.findById(1L))
                    .isInstanceOf(BaseException.class)
                    .hasMessage(StudioMemberErrorCode.STUDIO_MEMBER_NOT_FOUND.getMessage());
        }
    }


    @Nested
    @DisplayName("스튜디오에 참여한 멤버 전체 조회")
    class findAll {

        @Test
        @DisplayName("스튜디오에 참여한 멤버 전체 조회에 성공한다")
        void success() {
            // given
            studioMemberService.create(member[0].getId(), studio[0].getId(), 0, "connection1");
            List<MemberResponse> memberResponseList = studioMemberService.findAllByStudioId(studio[0].getId());

            // when - then
            assertAll(
                    () -> assertThat(memberResponseList.get(0).memberId()).isEqualTo(member[0].getId()),
                    () -> assertThat(memberResponseList.get(0).username()).isEqualTo(member[0].getUsername()),
                    () -> assertThat(memberResponseList.get(0).nickname()).isEqualTo(member[0].getNickname()),
                    () -> assertThat(memberResponseList.get(0).email()).isEqualTo(member[0].getEmail()),
                    () -> assertThat(memberResponseList.get(0).profileUrl()).isEqualTo(member[0].getProfileUrl())
            );


        }
    }

    @Nested
    @DisplayName("스튜디오 조회")
    class findStudio {

        @Test
        @DisplayName("스튜디오 조회에 성공한다")
        void success() {

            // given
            studioMemberService.create(member[0].getId(), studio[0].getId(), 0, "connection1");
            studioMemberService.create(member[1].getId(), studio[0].getId(), 0, "connection1");
            studioMemberService.create(member[2].getId(), studio[0].getId(), 0, "connection1");
            StudioResponse studioResponse = studioMemberService.findByMemberId(member[0].getId());
            // when - then

            assertAll(

                    () -> assertThat(studioResponse.studioId()).isEqualTo(studio[0].getId()),
                    () -> assertThat(studioResponse.sessionId()).isEqualTo(studio[0].getSessionId()),
                    () -> assertThat(studioResponse.title()).isEqualTo(studio[0].getTitle()),
                    () -> assertThat(studioResponse.intro()).isEqualTo(studio[0].getIntro()),
                    () -> assertThat(studioResponse.memberCount()).isEqualTo(studio[0].getMemberCount()),
                    () -> assertThat(studioResponse.limitNumber()).isEqualTo(studio[0].getLimitNumber()),
                    () -> assertThat(studioResponse.isPublic()).isEqualTo(studio[0].getIsPublic()),
                    () -> assertThat(studioResponse.onAir()).isEqualTo(studio[0].getOnAir())
            );

        }
    }
}
