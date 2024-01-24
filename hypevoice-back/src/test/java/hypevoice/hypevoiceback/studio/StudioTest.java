package hypevoice.hypevoiceback.studio;


import hypevoice.hypevoiceback.studio.domain.Studio;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static hypevoice.hypevoiceback.fixture.StudioFixture.STUDIO_FIXTURE1;
import static hypevoice.hypevoiceback.fixture.StudioFixture.STUDIO_FIXTURE3;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayName("Studio 도메인 테스트")

public class StudioTest {

    @Test
    @DisplayName("Studio를 생성한다")
    public void createStudio() {
        Studio studio = STUDIO_FIXTURE1.toStudio();

        assertAll(
                () -> assertThat(studio.getTitle()).isEqualTo(STUDIO_FIXTURE1.getTitle()),
                () -> assertThat(studio.getIntro()).isEqualTo(STUDIO_FIXTURE1.getIntro()),
                () -> assertThat(studio.getLimitNumber()).isEqualTo(STUDIO_FIXTURE1.getLimitNumber()),
                () -> assertThat(studio.getIsPublic()).isEqualTo(STUDIO_FIXTURE1.getIsPublic()),
                () -> assertThat(studio.getPassword()).isEqualTo(STUDIO_FIXTURE1.getPassword())

        );
    }

    @Test
    @DisplayName("Studio의 정보를 수정한다")
    public void updateStudio() {
        //given
        Studio studio1 = STUDIO_FIXTURE1.toStudio();
        Studio studio3 = STUDIO_FIXTURE3.toStudio();

        //when
        studio1.updateStudio("newTitle1","newIntro1",6,1,"password");
        studio3.updateStudio("newTitle3","newIntro3",6,0,null);

        // then
        assertAll(
                () -> assertThat(studio1.getTitle()).isEqualTo("newTitle1"),
                () -> assertThat(studio1.getIntro()).isEqualTo("newIntro1"),
                () -> assertThat(studio1.getLimitNumber()).isEqualTo(6),
                () -> assertThat(studio1.getIsPublic()).isEqualTo(1),
                () -> assertThat(studio1.getPassword()).isEqualTo("password"),

                () -> assertThat(studio3.getTitle()).isEqualTo("newTitle3"),
                () -> assertThat(studio3.getIntro()).isEqualTo("newIntro3"),
                () -> assertThat(studio3.getLimitNumber()).isEqualTo(6),
                () -> assertThat(studio3.getIsPublic()).isEqualTo(0),
                () -> assertThat(studio3.getPassword()).isEqualTo(null)
        );

    }

}
