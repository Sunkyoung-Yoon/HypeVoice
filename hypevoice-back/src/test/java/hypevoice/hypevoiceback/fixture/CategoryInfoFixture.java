package hypevoice.hypevoiceback.fixture;

import hypevoice.hypevoiceback.categoryInfo.domain.*;
import hypevoice.hypevoiceback.work.domain.Work;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import static hypevoice.hypevoiceback.categoryInfo.domain.Age.*;
import static hypevoice.hypevoiceback.categoryInfo.domain.Gender.*;
import static hypevoice.hypevoiceback.categoryInfo.domain.MediaClassification.*;
import static hypevoice.hypevoiceback.categoryInfo.domain.VoiceStyle.*;
import static hypevoice.hypevoiceback.categoryInfo.domain.VoiceTone.*;

@Getter
@RequiredArgsConstructor
public enum CategoryInfoFixture {
    CATEGORY_INFO_01(GAME, HIGH, AUTHORITATIVE, MALE, BABY),
    CATEGORY_INFO_02(MOVIE, MIDDLE , CHEERFUL, FEMALE, OLDAGE),
    CATEGORY_INFO_03(MOVIE, MIDDLE , CHEERFUL, FEMALE, OLDAGE),
    CATEGORY_INFO_04(MOVIE, MIDDLE , CHEERFUL, FEMALE, OLDAGE),
    CATEGORY_INFO_05(MOVIE, MIDDLE , CHEERFUL, FEMALE, OLDAGE),
    CATEGORY_INFO_06(MOVIE, MIDDLE , CHEERFUL, FEMALE, OLDAGE),
    CATEGORY_INFO_07(MOVIE, MIDDLE , CHEERFUL, FEMALE, OLDAGE),
    CATEGORY_INFO_08(MOVIE, MIDDLE , CHEERFUL, FEMALE, OLDAGE),
    CATEGORY_INFO_09(MOVIE, MIDDLE , CHEERFUL, FEMALE, OLDAGE),
    CATEGORY_INFO_10(MOVIE, MIDDLE , CHEERFUL, FEMALE, OLDAGE)
    ;

    private final MediaClassification mediaClassification;
    private final VoiceTone voiceTone;
    private final VoiceStyle voiceStyle;
    private final Gender gender;
    private final Age age;

    public CategoryInfo toCategoryInfo(Work work) {
        return CategoryInfo.createCategoryInfo(work, mediaClassification, voiceStyle, voiceTone, gender, age);
    }
}
