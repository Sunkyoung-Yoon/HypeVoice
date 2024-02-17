package hypevoice.hypevoiceback.board.domain;

import hypevoice.hypevoiceback.board.exception.BoardErrorCode;
import hypevoice.hypevoiceback.global.exception.BaseException;
import hypevoice.hypevoiceback.global.utils.EnumConverter;
import hypevoice.hypevoiceback.global.utils.EnumStandard;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

@Getter
@RequiredArgsConstructor
public enum Category implements EnumStandard {
    FEEDBACK("feedback", "피드백")
    ;

    private final String value;
    private final String title;

    @Override
    public String getValue() {
        return value;
    }

    public static Category from(String value) {
        return Arrays.stream(values())
                .filter(Category -> Category.value.equals(value))
                .findFirst()
                .orElseThrow(() -> BaseException.type(BoardErrorCode.CATEGORY_NOT_FOUND));
    }

    @jakarta.persistence.Converter
    public static class CategoryConverter extends EnumConverter<Category> {
        public CategoryConverter() {
            super(Category.class);
        }
    }
}
