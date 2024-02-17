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
    HIGH("T01", "고음"),
    MIDDLE("T02", "중음"),
    LOW("T03", "저음"),
    ETC("T04", "기타"),
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
