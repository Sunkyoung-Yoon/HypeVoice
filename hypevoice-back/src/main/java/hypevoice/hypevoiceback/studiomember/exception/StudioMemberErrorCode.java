package hypevoice.hypevoiceback.studiomember.exception;

import hypevoice.hypevoiceback.global.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
@Getter
@RequiredArgsConstructor
public enum StudioMemberErrorCode implements ErrorCode {
    STUDIO_MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "STUDIO_MEMBER_001", "스튜디오에 참여한 인원을 찾을 수 없습니다."),

    ;

    private final HttpStatus status;
    private final String errorCode;
    private final String message;
}
