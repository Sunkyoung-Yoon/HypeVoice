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
    HIGH("S01", "고음"),
    MIDDLE("S02", "중음"),
    LOW("S03", "저음"),
    ETC("S04", "기타"),
    ;

    private final String value;
    private final String title;

    @Override
    public String getValue() {
        return value;
    }

    public static VoiceStyle from(String value) {
        if(value == null) return null;

        return Arrays.stream(values())
                .filter(VoiceStyle -> VoiceStyle.value.equals(value))
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
