package hypevoice.hypevoiceback.work.exception;

import hypevoice.hypevoiceback.global.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum WorkErrorCode implements ErrorCode {
    WORK_NOT_FOUND(HttpStatus.NOT_FOUND,"WORK_001", "작업물 정보를 찾을 수 없습니다."),
    USER_IS_NOT_VOICE_MEMBER(HttpStatus.BAD_REQUEST, "WORK_002", "보이스에 대한 권한이 없습니다."),
    ;

    private final HttpStatus status;
    private final String errorCode;
    private final String message;
}
