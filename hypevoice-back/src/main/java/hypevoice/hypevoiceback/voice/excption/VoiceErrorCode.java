package hypevoice.hypevoiceback.voice.excption;

import hypevoice.hypevoiceback.global.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum VoiceErrorCode implements ErrorCode {
    VOICE_NOT_FOUND(HttpStatus.NOT_FOUND,"VOICE_001", "보이스 정보를 찾을 수 없습니다."),
    USER_IS_NOT_VOICE_MEMBER(HttpStatus.BAD_REQUEST, "VOICE_002", "보이스에 대한 권한이 없습니다."),
    ;

    private final HttpStatus status;
    private final String errorCode;
    private final String message;
}
