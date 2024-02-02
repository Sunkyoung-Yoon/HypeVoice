package hypevoice.hypevoiceback.studio.service;

import hypevoice.hypevoiceback.common.ServiceTest;
import hypevoice.hypevoiceback.fixture.MemberFixture;
import hypevoice.hypevoiceback.global.exception.BaseException;
import hypevoice.hypevoiceback.member.domain.Member;
import hypevoice.hypevoiceback.studio.domain.Studio;
import hypevoice.hypevoiceback.studio.dto.StudioRequest;
import hypevoice.hypevoiceback.studio.exception.StudioErrorCode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static hypevoice.hypevoiceback.fixture.StudioFixture.STUDIO_FIXTURE1;
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

    @BeforeEach
    void setUp() {
        noMaster = memberRepository.save(MemberFixture.SUNKYOUNG.toMember());
        studio = studioRepository.save(STUDIO_FIXTURE1.toStudio());
    }

    @Test
    @DisplayName("스튜디오 생성에 성공한다")
    void success() {
        // when
        Long studioId = studio.getId();
        // then
        Studio findStudio = studioRepository.findById(studioId).orElseThrow();

        assertAll(

                () -> assertThat(findStudio.getId()).isEqualTo(studio.getId()),
                () -> assertThat(findStudio.getSessionId()).isEqualTo(studio.getSessionId()),
                () -> assertThat(findStudio.getTitle()).isEqualTo(studio.getTitle()),
                () -> assertThat(findStudio.getIntro()).isEqualTo(studio.getIntro()),
                () -> assertThat(findStudio.getMemberCount()).isEqualTo(studio.getMemberCount()),
                () -> assertThat(findStudio.getLimitNumber()).isEqualTo(studio.getLimitNumber()),
                () -> assertThat(findStudio.getIsPublic()).isEqualTo(studio.getIsPublic()),
                () -> assertThat(findStudio.getOnAir()).isEqualTo(studio.getOnAir())
        );
    }

    @Nested
    @DisplayName("스튜디오 정보 수정")
    class update {

        @Test
        @DisplayName("스튜디오 정보 수정에 성공한다")
        void success() {
            // given
            StudioRequest studioRequest = new StudioRequest("newTitle", "newIntro", 6, 0, null);
            studioService.updateStudio(studio.getId(), studioRequest);

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
            studioService.deleteStudio(noMaster.getId(), studio.getId());

            // when - then
            assertThatThrownBy(() -> studioFindService.findById(studio.getId()))
                    .isInstanceOf(BaseException.class)
                    .hasMessage(StudioErrorCode.STUDIO_NOT_FOUND.getMessage());
        }
    }

}
