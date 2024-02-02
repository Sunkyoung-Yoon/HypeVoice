package hypevoice.hypevoiceback.studio.domain;


import hypevoice.hypevoiceback.common.RepositoryTest;
import hypevoice.hypevoiceback.fixture.StudioFixture;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayName("Studio [Repository Test] -> StudioRepository 테스트")
public class StudioRepositoryTest extends RepositoryTest {

    @Autowired
    private StudioRepository studioRepository;
    private Studio studio;

    @BeforeEach
    void setUp() {
        studio = studioRepository.save(StudioFixture.STUDIO_FIXTURE1.toStudio());
    }

    @Test
    @DisplayName("ID(PK)로 스튜디오를 확인한다")
    void findById() {

        Studio findStudio = studioRepository.findById(studio.getId()).orElseThrow();

        assertAll(
                () -> assertThat(findStudio.getSessionId()).isEqualTo(studio.getSessionId()),
                () -> assertThat(findStudio.getTitle()).isEqualTo(studio.getTitle()),
                () -> assertThat(findStudio.getIntro()).isEqualTo(studio.getIntro()),
                () -> assertThat(findStudio.getMemberCount()).isEqualTo(studio.getMemberCount()),
                () -> assertThat(findStudio.getLimitNumber()).isEqualTo(studio.getLimitNumber()),
                () -> assertThat(findStudio.getIsPublic()).isEqualTo(studio.getIsPublic()),
                () -> assertThat(findStudio.getPassword()).isEqualTo(studio.getPassword())

        );

    }
}
