package hypevoice.hypevoiceback.categoryInfo.exception;

import hypevoice.hypevoiceback.global.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum CategoryInfoErrorCode implements ErrorCode {
    CATEGORY_NOT_FOUND(HttpStatus.BAD_REQUEST, "CATEGORY_001", "카테고리 정보를 찾을 수 없습니다."),
    MEMBER_IS_NOT_WORK_MEMBER(HttpStatus.BAD_REQUEST, "CATEGORY_002", "작업물의 카테고리를 수정할 권한이 없습니다."),
    CATEGORY_IS_NULL(HttpStatus.BAD_REQUEST, "CATEGORY_003", "모든 카테고리가 설정되어있지 않습니다.")
    ;

    private final HttpStatus status;
    private final String errorCode;
    private final String message;
}
