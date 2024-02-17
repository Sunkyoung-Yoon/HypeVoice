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
public enum Gender implements EnumStandard {
    MALE("G01", "남성"),
    FEMALE("G02", "여성"),
    ETC("G03", "기타"),
    ;

    private final String value;
    private final String title;

    @Override
    public String getValue() {
        return value;
    }

    public static Gender from(String value) {
        return Arrays.stream(values())
                .filter(Gender -> Gender.title.equals(value))
                .findFirst()
                .orElseThrow(() -> BaseException.type(CategoryInfoErrorCode.CATEGORY_NOT_FOUND));
    }

    @jakarta.persistence.Converter
    public static class GenderConverter extends EnumConverter<Gender> {
        public GenderConverter() {
            super(Gender.class);
        }
    }
}
