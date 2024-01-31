package hypevoice.hypevoiceback.fixture;

import hypevoice.hypevoiceback.categoryInfo.domain.*;
import hypevoice.hypevoiceback.work.domain.Work;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import static hypevoice.hypevoiceback.categoryInfo.domain.Age.BABY;
import static hypevoice.hypevoiceback.categoryInfo.domain.Gender.MALE;
import static hypevoice.hypevoiceback.categoryInfo.domain.MediaClassification.GAME;
import static hypevoice.hypevoiceback.categoryInfo.domain.VoiceStyle.HIGH;
import static hypevoice.hypevoiceback.categoryInfo.domain.VoiceTone.AUTHORITATIVE;

@Getter
@RequiredArgsConstructor
public enum CategoryInfoFixture {
    CATEGORY_INFO_01(GAME, AUTHORITATIVE, HIGH, MALE, BABY);

    private final MediaClassification mediaClassification;
    private final VoiceTone voiceTone;
    private final VoiceStyle voiceStyle;
    private final Gender gender;
    private final Age age;

    public CategoryInfo toCategoryInfo(Work work) {
        return CategoryInfo.createCategoryInfo(work, mediaClassification, voiceTone, voiceStyle, gender, age);
    }
}
