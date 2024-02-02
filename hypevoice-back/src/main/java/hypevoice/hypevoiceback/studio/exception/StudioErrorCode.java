package hypevoice.hypevoiceback.studio.exception;

import hypevoice.hypevoiceback.global.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum StudioErrorCode implements ErrorCode {
    SESSION_NOT_FOUND(HttpStatus.NOT_FOUND, "STUDIO_000", "세션을 찾을 수 없습니다."),
    UNABLE_TO_CREATE_SESSION(HttpStatus.BAD_REQUEST, "STUDIO_001", "세션을 생성 할 수 없습니다."),
    STUDIO_NOT_FOUND(HttpStatus.NOT_FOUND, "STUDIO_002", "스튜디오를 찾을 수 없습니다."),
    STUDIO_IS_FULL(HttpStatus.BAD_REQUEST, "STUDIO_003", "스튜디오의 인원이 다 찼습니다."),
    STUDIO_ALREADY_JOINED(HttpStatus.BAD_REQUEST, "STUDIO_004", "이미 참여중인 스튜디오가 있습니다."),
    STUDIO_MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "STUDIO_MEMBER_001", "스튜디오에 참여한 인원을 찾을 수 없습니다."),
    ;

    private final HttpStatus status;
    private final String errorCode;
    private final String message;
}
