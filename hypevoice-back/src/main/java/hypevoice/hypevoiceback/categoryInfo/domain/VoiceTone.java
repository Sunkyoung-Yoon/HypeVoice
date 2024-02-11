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
public enum VoiceTone implements EnumStandard {
    AUTHORITATIVE("T01", "권위있는"),
    CUTE("T02", "귀여운"),
    CHEERFUL("T03", "밝은"),
    WARM("T04", "따뜻한"),
    GLOOMY("T05", "어두운"),
    COLD("T06", "차가운"),
    ETC("T07", "기타"),
    ;

    private final String value;
    private final String title;

    @Override
    public String getValue() {
        return value;
    }

    public static VoiceTone from(String value) {
        return Arrays.stream(values())
                .filter(VoiceTone -> VoiceTone.title.equals(value))
                .findFirst()
                .orElseThrow(() -> BaseException.type(CategoryInfoErrorCode.CATEGORY_NOT_FOUND));
    }

    @jakarta.persistence.Converter
    public static class VoiceToneConverter extends EnumConverter<VoiceTone> {
        public VoiceToneConverter() {
            super(VoiceTone.class);
        }
    }
}
