package hypevoice.hypevoiceback.studio.service;

import hypevoice.hypevoiceback.common.ServiceTest;
import hypevoice.hypevoiceback.global.exception.BaseException;
import hypevoice.hypevoiceback.studio.domain.Studio;
import hypevoice.hypevoiceback.studio.exception.StudioErrorCode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static hypevoice.hypevoiceback.fixture.StudioFixture.STUDIO_FIXTURE1;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("Studio [Service Layer] -> StudioFindService 테스트")
public class StudioFindServiceTest extends ServiceTest {

    @Autowired
    private StudioFindService studioFindService;

    private Studio studio;

    @BeforeEach
    void setUp() {
        studio = studioRepository.save(STUDIO_FIXTURE1.toStudio());
    }


    @Test
    @DisplayName("ID(PK)로 스튜디오를 조회한다")
    void findById() {
        // when
        Studio findStudio = studioFindService.findById(studio.getId());

        // then
        assertThat(findStudio).isEqualTo(studio);
        assertThatThrownBy(() -> studioFindService.findById(studio.getId() + 100L))
                .isInstanceOf(BaseException.class)
                .hasMessage(StudioErrorCode.STUDIO_NOT_FOUND.getMessage());


    }
}
