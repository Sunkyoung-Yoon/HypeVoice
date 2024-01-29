package hypevoice.hypevoiceback.board.exception;

import hypevoice.hypevoiceback.global.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum BoardErrorCode implements ErrorCode {
    BOARD_NOT_FOUND(HttpStatus.NOT_FOUND, "BOARD_001", "게시글 정보를 찾을 수 없습니다."),
    USER_IS_NOT_BOARD_WRITER(HttpStatus.BAD_REQUEST, "BOARD_002", "게시글 작성자가 아닙니다."),
    CATEGORY_NOT_FOUND(HttpStatus.NOT_FOUND, "BOARD_003", "존재하지 않는 카테고리입니다.")
    ;

    private final HttpStatus status;
    private final String errorCode;
    private final String message;
}
