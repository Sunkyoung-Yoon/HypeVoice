package hypevoice.hypevoiceback.categoryInfo.service;

import hypevoice.hypevoiceback.categoryInfo.domain.*;
import hypevoice.hypevoiceback.categoryInfo.dto.CategoryInfoValue;
import hypevoice.hypevoiceback.common.ServiceTest;
import hypevoice.hypevoiceback.member.domain.Member;
import hypevoice.hypevoiceback.voice.domain.Voice;
import hypevoice.hypevoiceback.work.domain.Work;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static hypevoice.hypevoiceback.fixture.CategoryInfoFixture.CATEGORY_INFO_02;
import static hypevoice.hypevoiceback.fixture.MemberFixture.JAESIK;
import static hypevoice.hypevoiceback.fixture.VoiceFixture.VOICE_01;
import static hypevoice.hypevoiceback.fixture.WorkFixture.WORK_01;
import static hypevoice.hypevoiceback.fixture.WorkFixture.WORK_02;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayName("CategoryInfo [Service Layer] -> CategoryInfoService 테스트")
public class CategoryInfoServiceTest extends ServiceTest {
    @Autowired
    private CategoryInfoService categoryInfoService;

    @Autowired
    private CategoryInfoFindService categoryInfoFindService;

    private Member member;
    private Voice voice;
    private Work work1;
    private Work work2;
    private CategoryInfo categoryInfo;

    @BeforeEach
    void setUp() {
        member = memberRepository.save(JAESIK.toMember());
        voice = voiceRepository.save(VOICE_01.toVoice(member));
        work1 = workRepository.save(WORK_01.toWork(voice));
        work2 = workRepository.save(WORK_02.toWork(voice));
        categoryInfo = categoryInfoRepository.save(CATEGORY_INFO_02.toCategoryInfo(work2));
    }

    @Test
    @DisplayName("카테고리 정보 생성에 성공한다")
    void createSuccess() {
        // when
        categoryInfoService.createCategoryInfo(member.getId(), work1.getId(), "M01", "T04", "S02", "G01", "A04");

        // then
        CategoryInfo findCategoryInfo = categoryInfoFindService.findByWorkId(work1.getId());
        assertAll(
                () -> assertThat(findCategoryInfo.getMediaClassification()).isEqualTo(MediaClassification.GAME),
                () -> assertThat(findCategoryInfo.getVoiceTone()).isEqualTo(VoiceTone.WARM),
                () -> assertThat(findCategoryInfo.getVoiceStyle()).isEqualTo(VoiceStyle.MIDDLE),
                () -> assertThat(findCategoryInfo.getGender()).isEqualTo(Gender.MALE),
                () -> assertThat(findCategoryInfo.getAge()).isEqualTo(Age.TWENTY)
        );
    }

    @Nested
    @DisplayName("카테고리 수정")
    class update {

        @Test
        @DisplayName("카테고리 수정에 성공한다")
        void updateSuccess() {
            // given
            categoryInfoService.updateCategoryInfo(member.getId(), work2.getId(), "M06", "T07", "S04", "G03", "A06");

            // when
            CategoryInfo findCategoryInfo = categoryInfoFindService.findByWorkId(work2.getId());

            // then
            assertAll(
                    () -> assertThat(findCategoryInfo.getMediaClassification()).isEqualTo(MediaClassification.ETC),
                    () -> assertThat(findCategoryInfo.getVoiceTone()).isEqualTo(VoiceTone.ETC),
                    () -> assertThat(findCategoryInfo.getVoiceStyle()).isEqualTo(VoiceStyle.ETC),
                    () -> assertThat(findCategoryInfo.getGender()).isEqualTo(Gender.ETC),
                    () -> assertThat(findCategoryInfo.getAge()).isEqualTo(Age.OLDAGE)
            );
        }
    }

    @Nested
    @DisplayName("카테고리 선택")
    class read {
        @Test
        @DisplayName("카테고리 선택에 성공한다")
        void success() {
            // when
            CategoryInfoValue categoryInfoResponse = categoryInfoService.readCategoryInfo(categoryInfo.getWork().getId());

            // then
            assertAll(
                    () -> assertThat(categoryInfoResponse.mediaClassificationValue()).isEqualTo(categoryInfo.getMediaClassification().getValue()),
                    () -> assertThat(categoryInfoResponse.voiceToneValue()).isEqualTo(categoryInfo.getVoiceTone().getValue()),
                    () -> assertThat(categoryInfoResponse.voiceStyleValue()).isEqualTo(categoryInfo.getVoiceStyle().getValue()),
                    () -> assertThat(categoryInfoResponse.genderValue()).isEqualTo(categoryInfo.getGender().getValue()),
                    () -> assertThat(categoryInfoResponse.ageValue()).isEqualTo(categoryInfo.getAge().getValue()),
                    () -> assertThat(categoryInfoResponse.workId()).isEqualTo(categoryInfo.getWork().getId())
            );
        }
    }
}
