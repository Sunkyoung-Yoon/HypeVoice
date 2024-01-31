package hypevoice.hypevoiceback.categoryInfo;

import hypevoice.hypevoiceback.categoryInfo.domain.*;
import hypevoice.hypevoiceback.member.domain.Member;
import hypevoice.hypevoiceback.voice.domain.Voice;
import hypevoice.hypevoiceback.work.domain.Work;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static hypevoice.hypevoiceback.categoryInfo.domain.Age.BABY;
import static hypevoice.hypevoiceback.categoryInfo.domain.Age.TEENAGER;
import static hypevoice.hypevoiceback.categoryInfo.domain.Gender.ETC;
import static hypevoice.hypevoiceback.categoryInfo.domain.Gender.MALE;
import static hypevoice.hypevoiceback.categoryInfo.domain.MediaClassification.GAME;
import static hypevoice.hypevoiceback.categoryInfo.domain.MediaClassification.MOVIE;
import static hypevoice.hypevoiceback.categoryInfo.domain.VoiceStyle.HIGH;
import static hypevoice.hypevoiceback.categoryInfo.domain.VoiceStyle.LOW;
import static hypevoice.hypevoiceback.categoryInfo.domain.VoiceTone.AUTHORITATIVE;
import static hypevoice.hypevoiceback.categoryInfo.domain.VoiceTone.CUTE;
import static hypevoice.hypevoiceback.fixture.CategoryInfoFixture.CATEGORY_INFO_01;
import static hypevoice.hypevoiceback.fixture.MemberFixture.JAESIK;
import static hypevoice.hypevoiceback.fixture.VoiceFixture.Voice1;
import static hypevoice.hypevoiceback.fixture.WorkFixture.Work1;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayName("CategoryInfo 도메인 테스트")
public class CategoryInfoTest {
    private Member member;
    private Voice voice;
    private Work work;
    private CategoryInfo category;

    @BeforeEach
    void setUp() {
        member = JAESIK.toMember();
        voice = Voice1.toVoice(member);
        work = Work1.toWork(voice);
        category = CATEGORY_INFO_01.toCategoryInfo(work);
    }

    @Test
    @DisplayName("카테고리 정보 생성에 성공한다")
    void createCategoryInfo() {
        // then
        assertAll(
                () -> assertThat(category.getWork()).isEqualTo(work),
                () -> assertThat(category.getMediaClassification()).isEqualTo(GAME),
                () -> assertThat(category.getVoiceTone()).isEqualTo(AUTHORITATIVE),
                () -> assertThat(category.getVoiceStyle()).isEqualTo(HIGH),
                () -> assertThat(category.getGender()).isEqualTo(MALE),
                () -> assertThat(category.getAge()).isEqualTo(BABY)
        );
    }

    @Test
    @DisplayName("카테고리 정보 수정에 성공한다")
    void updateCategoryInfo() {
        // when
        category.updateCategoryInfo(MOVIE, CUTE, LOW, ETC, TEENAGER);

        // then
        assertAll(
                () -> assertThat(category.getMediaClassification()).isEqualTo(MOVIE),
                () -> assertThat(category.getVoiceTone()).isEqualTo(CUTE),
                () -> assertThat(category.getVoiceStyle()).isEqualTo(LOW),
                () -> assertThat(category.getGender()).isEqualTo(ETC),
                () -> assertThat(category.getAge()).isEqualTo(TEENAGER)
        );
    }
}
