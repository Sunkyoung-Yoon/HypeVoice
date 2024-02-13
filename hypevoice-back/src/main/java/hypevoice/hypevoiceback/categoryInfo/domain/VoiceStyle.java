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
public enum VoiceStyle implements EnumStandard {
    AUTHORITATIVE("S01", "권위있는"),
    CUTE("S02", "귀여운"),
    CHEERFUL("S03", "밝은"),
    WARM("S04", "따뜻한"),
    GLOOMY("S05", "어두운"),
    COLD("S06", "차가운"),
    ETC("S07", "기타"),
    ;

    private final String value;
    private final String title;

    @Override
    public String getValue() {
        return value;
    }

    public static VoiceStyle from(String value) {
        return Arrays.stream(values())
                .filter(VoiceStyle -> VoiceStyle.title.equals(value))
                .findFirst()
                .orElseThrow(() -> BaseException.type(CategoryInfoErrorCode.CATEGORY_NOT_FOUND));
    }

    @jakarta.persistence.Converter
    public static class VoiceStyleConverter extends EnumConverter<VoiceStyle> {
        public VoiceStyleConverter() {
            super(VoiceStyle.class);
        }
    }
}
