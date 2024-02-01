package hypevoice.hypevoiceback.voice.exception;

import hypevoice.hypevoiceback.global.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum VoiceErrorCode implements ErrorCode {
    VOICE_NOT_FOUND(HttpStatus.NOT_FOUND,"VOICE_001", "보이스 정보를 찾을 수 없습니다."),
    USER_IS_NOT_VOICE_MEMBER(HttpStatus.BAD_REQUEST, "VOICE_002", "보이스에 대한 권한이 없습니다."),
    ALREADY_VOICE_LIKE(HttpStatus.CONFLICT, "VOICE_003","이미 좋아요를 누른 보이스입니다."),
    SELF_VOICE_LIKE_NOT_ALLOWED(HttpStatus.CONFLICT, "VOICE_004", "본인 보이스는 좋아요를 누를 수 없습니다."),
    VOICE_LIKE_NOT_FOUND(HttpStatus.NOT_FOUND, "VOICE_005", "좋아요를 누르지 않은 보이스는 좋아요 취소를 할 수 없습니다.")
    ;

    private final HttpStatus status;
    private final String errorCode;
    private final String message;
}
