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
public enum Age implements EnumStandard {
    BABY("A01", "유아"),
    CHILD("A02", "아동"),
    TEENAGER("A03", "청소년"),
    TWENTY("A04", "청년"),
    MIDDLEAGE("A05", "중년"),
    OLDAGE("A06", "노년")
    ;

    private final String value;
    private final String title;

    @Override
    public String getValue() {
        return value;
    }

    public static Age from(String value) {
        return Arrays.stream(values())
                .filter(Age -> Age.value.equals(value))
                .findFirst()
                .orElseThrow(() -> BaseException.type(CategoryInfoErrorCode.CATEGORY_NOT_FOUND));
    }

    @jakarta.persistence.Converter
    public static class AgeConverter extends EnumConverter<Age> {
        public AgeConverter() {
            super(Age.class);
        }
    }
}
