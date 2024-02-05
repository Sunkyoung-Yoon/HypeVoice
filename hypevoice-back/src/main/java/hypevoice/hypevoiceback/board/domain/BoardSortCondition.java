package hypevoice.hypevoiceback.board.domain;

import hypevoice.hypevoiceback.board.exception.BoardErrorCode;
import hypevoice.hypevoiceback.global.exception.BaseException;
import hypevoice.hypevoiceback.global.utils.EnumStandard;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum BoardSortCondition implements EnumStandard {
    TIME("최신순"),
    HIT("조회순");

    private final String value;

    public static BoardSortCondition from(String value) {
        return Arrays.stream(values())
                .filter(boardSortCondition -> boardSortCondition.value.equals(value))
                .findFirst()
                .orElseThrow(() -> BaseException.type(BoardErrorCode.SORT_CONDITION_NOT_FOUND));
    }
}
