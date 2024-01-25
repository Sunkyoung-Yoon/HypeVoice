package hypevoice.hypevoiceback.work;

import hypevoice.hypevoiceback.work.domain.Work;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static hypevoice.hypevoiceback.fixture.WorkFixture.Work1;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayName("Work 도메인 테스트")
public class WorkTest {

    @Test
    @DisplayName("Work를 생성한다")
    public void createWork() {
        Work work = Work1.toWork();

        assertAll(
                () -> assertThat(work.getIntro()).isEqualTo(Work1.getIntro()),
                () -> assertThat(work.getVideoLink()).isEqualTo(Work1.getVideoLink()),
                () -> assertThat(work.getPhotoUrl()).isEqualTo(Work1.getPhotoUrl()),
                () -> assertThat(work.getScriptUrl()).isEqualTo(Work1.getScriptUrl()),
                () -> assertThat(work.getRecordUrl()).isEqualTo(Work1.getRecordUrl()),
                () -> assertThat(work.getInfo()).isEqualTo(Work1.getInfo())
        );
    }

    @Test
    @DisplayName("Work의 내용들을 변경한다")
    public void updateWork() {
        //given
        Work work = Work1.toWork();

        //when
        work.updateWork("새로운한줄소개1", "새로운vLink1", "새로운pURL1", "새로운sURL1", "새로운rURL1", "새로운디테일1");

        // then
        assertAll(
                () -> assertThat(work.getIntro()).isEqualTo("새로운한줄소개1"),
                () -> assertThat(work.getVideoLink()).isEqualTo("새로운vLink1"),
                () -> assertThat(work.getPhotoUrl()).isEqualTo("새로운pURL1"),
                () -> assertThat(work.getScriptUrl()).isEqualTo("새로운sURL1"),
                () -> assertThat(work.getRecordUrl()).isEqualTo("새로운rURL1"),
                () -> assertThat(work.getInfo()).isEqualTo("새로운디테일1")
        );

    }

    @Test
    @DisplayName("Work 대표설정을 변경한다")
    public void updateRep() {
        //given
        Work work = Work1.toWork();

        //when : 0 -> 1
        work.updateRep();

        // then
        assertThat(work.getIsRep()).isEqualTo(1);
    }
}
