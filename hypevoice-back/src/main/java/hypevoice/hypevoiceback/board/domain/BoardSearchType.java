package hypevoice.hypevoiceback.board.domain;

import hypevoice.hypevoiceback.board.exception.BoardErrorCode;
import hypevoice.hypevoiceback.global.exception.BaseException;
import hypevoice.hypevoiceback.global.utils.EnumStandard;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum BoardSearchType implements EnumStandard {
    TITLE("제목"),
    CONTENT("내용"),
    WRITER("작성자"),
    TITLE_AND_CONTENT("제목과내용")
    ;

    private final String value;

    public static BoardSearchType from(String value) {
        return Arrays.stream(values())
                .filter(boardSearchType -> boardSearchType.value.equals(value))
                .findFirst()
                .orElseThrow(() -> BaseException.type(BoardErrorCode.SEARCH_TYPE_NOT_FOUND));
    }
}
