package hypevoice.hypevoiceback.categoryInfo.domain;

import hypevoice.hypevoiceback.categoryInfo.exception.CategoryInfoErrorCode;
import hypevoice.hypevoiceback.global.exception.BaseException;
import hypevoice.hypevoiceback.global.utils.EnumConverter;
import hypevoice.hypevoiceback.global.utils.EnumStandard;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

@Getter
@RequiredArgsConstructor
public enum MediaClassification implements EnumStandard {
    GAME("M01", "게임"),
    NARRATION("M02", "나레이션"),
    ANIMATION("M03", "애니매이션"),
    AUDIO_DRAMA("M04", "오디오드라마"),
    MOVIE("M05", "영화"),
    ETC("M06", "기타"),
    ;

    private final String value;
    private final String title;

    @Override
    public String getValue() {
        return value;
    }

    public static MediaClassification from(String value) {
        if(value == null) return null;

        return Arrays.stream(values())
                .filter(MediaClassification -> MediaClassification.value.equals(value))
                .findFirst()
                .orElseThrow(() -> BaseException.type(CategoryInfoErrorCode.CATEGORY_NOT_FOUND));
    }

    @jakarta.persistence.Converter
    public static class MediaClassificationConverter extends EnumConverter<MediaClassification> {
        public MediaClassificationConverter() {
            super(MediaClassification.class);
        }
    }
}
