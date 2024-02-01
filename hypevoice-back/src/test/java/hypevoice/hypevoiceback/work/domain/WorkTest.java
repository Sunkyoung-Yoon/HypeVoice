package hypevoice.hypevoiceback.work.domain;

import hypevoice.hypevoiceback.member.domain.Member;
import hypevoice.hypevoiceback.voice.domain.Voice;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static hypevoice.hypevoiceback.fixture.MemberFixture.JAESIK;
import static hypevoice.hypevoiceback.fixture.VoiceFixture.VOICE_01;
import static hypevoice.hypevoiceback.fixture.WorkFixture.WORK_01;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayName("Work 도메인 테스트")
public class WorkTest {

    private Member member;
    private Voice voice;
    private Work work;

    @BeforeEach
    void setUp() {
        member = JAESIK.toMember();
        voice = VOICE_01.toVoice(member);
        work = WORK_01.toWork(voice);
    }

    @Test
    @DisplayName("Work를 생성한다")
    public void createWork() {
        // then
        assertAll(
                () -> assertThat(work.getTitle()).isEqualTo(WORK_01.getTitle()),
                () -> assertThat(work.getVideoLink()).isEqualTo(WORK_01.getVideoLink()),
                () -> assertThat(work.getPhotoUrl()).isEqualTo(WORK_01.getPhotoUrl()),
                () -> assertThat(work.getScriptUrl()).isEqualTo(WORK_01.getScriptUrl()),
                () -> assertThat(work.getRecordUrl()).isEqualTo(WORK_01.getRecordUrl()),
                () -> assertThat(work.getInfo()).isEqualTo(WORK_01.getInfo()),
                () -> assertThat(work.getIsRep()).isEqualTo(1),
                () -> assertThat(work.getVoice()).isEqualTo(voice)
        );
    }

    @Test
    @DisplayName("Work의 내용들을 변경한다")
    public void updateWork() {
        //when
        work.updateWork("새로운제목1", "새로운vLink1", "새로운pUrl1", "새로운sUrl1", "새로운rUrl1", "새로운디테일1", 0);

        // then
        assertAll(
                () -> assertThat(work.getTitle()).isEqualTo("새로운제목1"),
                () -> assertThat(work.getVideoLink()).isEqualTo("새로운vLink1"),
                () -> assertThat(work.getPhotoUrl()).isEqualTo("새로운pUrl1"),
                () -> assertThat(work.getScriptUrl()).isEqualTo("새로운sUrl1"),
                () -> assertThat(work.getRecordUrl()).isEqualTo("새로운rUrl1"),
                () -> assertThat(work.getInfo()).isEqualTo("새로운디테일1"),
                () -> assertThat(work.getIsRep()).isEqualTo(0)
        );

    }

    @Test
    @DisplayName("Work 대표설정을 변경한다")
    public void updateRep() {
        //when : 1 -> 0
        work.updateRep();

        // then
        assertThat(work.getIsRep()).isEqualTo(0);
    }
}