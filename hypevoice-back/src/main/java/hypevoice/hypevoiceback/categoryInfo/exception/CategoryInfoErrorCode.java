package hypevoice.hypevoiceback.categoryInfo.exception;

import hypevoice.hypevoiceback.global.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum CategoryInfoErrorCode implements ErrorCode {
    CATEGORY_NOT_FOUND(HttpStatus.NOT_FOUND, "CATEGORY_001", "카테고리 정보를 찾을 수 없습니다."),
    ;

    private final HttpStatus status;
    private final String errorCode;
    private final String message;
}
