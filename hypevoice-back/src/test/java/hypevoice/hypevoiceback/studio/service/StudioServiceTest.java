package hypevoice.hypevoiceback.studio.service;

import hypevoice.hypevoiceback.common.ServiceTest;
import hypevoice.hypevoiceback.fixture.MemberFixture;
import hypevoice.hypevoiceback.global.exception.BaseException;
import hypevoice.hypevoiceback.member.domain.Member;
import hypevoice.hypevoiceback.studio.domain.Studio;
import hypevoice.hypevoiceback.studio.dto.StudioRequest;
import hypevoice.hypevoiceback.studio.dto.StudioResponse;
import hypevoice.hypevoiceback.studio.exception.StudioErrorCode;
import hypevoice.hypevoiceback.studiomember.domain.StudioMember;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static hypevoice.hypevoiceback.fixture.StudioFixture.STUDIO_FIXTURE1;
import static hypevoice.hypevoiceback.fixture.StudioFixture.STUDIO_FIXTURE2;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayName("Studio [Service Layer] -> StudioService 테스트")
public class StudioServiceTest extends ServiceTest {
    @Autowired
    private StudioService studioService;

    @Autowired
    private StudioFindService studioFindService;
    private Studio studio;
    private Member noMaster;
    private Member master;

    @BeforeEach
    void setUp() {
        noMaster = memberRepository.save(MemberFixture.GABIN.toMember());
        master = memberRepository.save(MemberFixture.SUNKYOUNG.toMember());
        studio = studioRepository.save(STUDIO_FIXTURE1.toStudio());

    }

    @Test
    @DisplayName("스튜디오 생성에 성공한다")
    void success() {
        // when
        StudioRequest studioRequest = new StudioRequest("title1", "intro1", 6, 0, null);

        Long studioId = studioService.createStudio(master.getId(), studioRequest).studioId();
        // then

        Studio findStudio = studioRepository.findById(studioId).orElseThrow();
        assertAll(

                () -> assertThat(findStudio.getTitle()).isEqualTo(studioRequest.title()),
                () -> assertThat(findStudio.getIntro()).isEqualTo(studioRequest.intro()),
                () -> assertThat(findStudio.getLimitNumber()).isEqualTo(studioRequest.limitNumber()),
                () -> assertThat(findStudio.getIsPublic()).isEqualTo(studioRequest.isPublic()),
                () -> assertThat(findStudio.getPassword()).isEqualTo(studioRequest.password())

        );
    }

    @Nested
    @DisplayName("스튜디오 정보 수정")
    class update {
        @Test
        @DisplayName("방장이 아니면 수정 할 수 없다.")
        void throwExceptionByMemberNotHost() {
            studioMemberRepository.save(StudioMember.createStudioMember(noMaster, studio, 0, "connectionId1"));
            StudioRequest studioRequest = new StudioRequest("newTitle", "newIntro", 6, 0, null);
            // when - then
            assertThatThrownBy(() -> studioService.updateStudio(noMaster.getId(), studio.getId(), studioRequest))
                    .isInstanceOf(BaseException.class)
                    .hasMessage(StudioErrorCode.UNABLE_TO_UPDATE_STUDIO.getMessage());
        }

        @Test
        @DisplayName("스튜디오 정보 수정에 성공한다")
        void success() {
            // given
            studioMemberRepository.save(StudioMember.createStudioMember(master, studio, 1, "connectionId1"));
            StudioRequest studioRequest = new StudioRequest("newTitle", "newIntro", 6, 0, null);
            studioService.updateStudio(master.getId(), studio.getId(), studioRequest);

            // when
            Studio findStudio = studioFindService.findById(studio.getId());

            // then
            assertAll(
                    () -> assertThat(findStudio.getTitle()).isEqualTo("newTitle"),
                    () -> assertThat(findStudio.getIntro()).isEqualTo("newIntro"),
                    () -> assertThat(findStudio.getLimitNumber()).isEqualTo(6),
                    () -> assertThat(findStudio.getIsPublic()).isEqualTo(0),
                    () -> assertThat(findStudio.getPassword()).isEqualTo(null)
            );
        }
    }

    @Nested
    @DisplayName("스튜디오 삭제")
    class delete {

        @Test
        @DisplayName("스튜디오 삭제에 성공한다")
        void success() {
            // given
            studioMemberRepository.save(StudioMember.createStudioMember(master, studio, 1,"connectionId1"));
            studioService.deleteStudio(master.getId(), studio.getId());

            // when - then
            assertThatThrownBy(() -> studioFindService.findById(studio.getId()))
                    .isInstanceOf(BaseException.class)
                    .hasMessage(StudioErrorCode.STUDIO_NOT_FOUND.getMessage());
        }
    }

    @Nested
    @DisplayName("스튜디오 상세 조회")
    class read {
        @Test
        @DisplayName("스튜디오 상세 조회에 성공한다")
        void success() {
            // when
            StudioResponse studioResponse = studioService.findOneStudio(1L, studio.getId());

            // then
            assertAll(

                    () -> assertThat(studioResponse.studioId()).isEqualTo(studio.getId()),
                    () -> assertThat(studioResponse.sessionId()).isEqualTo(studio.getSessionId()),
                    () -> assertThat(studioResponse.title()).isEqualTo(studio.getTitle()),
                    () -> assertThat(studioResponse.intro()).isEqualTo(studio.getIntro()),
                    () -> assertThat(studioResponse.memberCount()).isEqualTo(studio.getMemberCount()),
                    () -> assertThat(studioResponse.limitNumber()).isEqualTo(studio.getLimitNumber()),
                    () -> assertThat(studioResponse.isPublic()).isEqualTo(studio.getIsPublic()),
                    () -> assertThat(studioResponse.onAir()).isEqualTo(studio.getOnAir())
            );
        }
    }

    @Nested
    @DisplayName("스튜디오 전체 조회")
    class readAll {
        @Test
        @DisplayName("스튜디오 전체 조회에 성공한다")
        void success() {
            //

            // when
            Studio studio2 = studioRepository.save(STUDIO_FIXTURE2.toStudio());
            List<StudioResponse> studioResponseList = studioService.findAll("ti", 1);

            // then
            assertAll(

                    () -> assertThat(studioResponseList.get(1).studioId()).isEqualTo(studio.getId()),
                    () -> assertThat(studioResponseList.get(1).sessionId()).isEqualTo(studio.getSessionId()),
                    () -> assertThat(studioResponseList.get(1).title()).isEqualTo(studio.getTitle()),
                    () -> assertThat(studioResponseList.get(1).intro()).isEqualTo(studio.getIntro()),
                    () -> assertThat(studioResponseList.get(1).memberCount()).isEqualTo(studio.getMemberCount()),
                    () -> assertThat(studioResponseList.get(1).limitNumber()).isEqualTo(studio.getLimitNumber()),
                    () -> assertThat(studioResponseList.get(1).isPublic()).isEqualTo(studio.getIsPublic()),
                    () -> assertThat(studioResponseList.get(1).onAir()).isEqualTo(studio.getOnAir()),

                    () -> assertThat(studioResponseList.get(0).studioId()).isEqualTo(studio2.getId()),
                    () -> assertThat(studioResponseList.get(0).sessionId()).isEqualTo(studio2.getSessionId()),
                    () -> assertThat(studioResponseList.get(0).title()).isEqualTo(studio2.getTitle()),
                    () -> assertThat(studioResponseList.get(0).intro()).isEqualTo(studio2.getIntro()),
                    () -> assertThat(studioResponseList.get(0).memberCount()).isEqualTo(studio2.getMemberCount()),
                    () -> assertThat(studioResponseList.get(0).limitNumber()).isEqualTo(studio2.getLimitNumber()),
                    () -> assertThat(studioResponseList.get(0).isPublic()).isEqualTo(studio2.getIsPublic()),
                    () -> assertThat(studioResponseList.get(0).onAir()).isEqualTo(studio2.getOnAir())
            );
        }
    }
}
