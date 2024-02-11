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
    UNABLE_TO_UPDATE_STUDIO(HttpStatus.BAD_REQUEST, "STUDIO_005", "스튜디오을 수정 할 수 없습니다."),
    UNABLE_TO_DELETE_STUDIO(HttpStatus.BAD_REQUEST, "STUDIO_006", "스튜디오를 삭제 할 수 없습니다."),
    INCORRECT_PASSWORD(HttpStatus.BAD_REQUEST, "STUDIO_007", "패스워드를 틀렸습니다."),
    NULL_PASSWORD_OF_STUDIO_OR_REQUEST(HttpStatus.BAD_REQUEST, "STUDIO_008", "비공개 방에 패스워드가 설정되지 않았습니다."),
    UNABLE_CONNECT_PRIVATE_ROOM(HttpStatus.BAD_REQUEST, "STUDIO_009", "비공개방에 접근 할 수 없습니다."),
    UNABLE_RECORDING_START(HttpStatus.BAD_REQUEST, "STUDIO_010", "녹음 시작 할 수 없습니다."),
    UNABLE_RECORDING_STOP(HttpStatus.BAD_REQUEST, "STUDIO_011", "녹음을 정지 할 수 없습니다.."),
    UNABLE_RECORDING_DELETE(HttpStatus.BAD_REQUEST, "STUDIO_012", "녹음을 삭제 할 수 없습니다.."),
    UNABLE_RECORDING(HttpStatus.BAD_REQUEST, "STUDIO_013", "녹음을 관리 할 권한이 없습니다.."),
    RECORDING_NOT_FOUND(HttpStatus.NOT_FOUND, "STUDIO_014", "녹음을 찾을 수 올 수 없습니다.."),
    STUDIO_MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "STUDIO_MEMBER_001", "스튜디오에 참여한 인원을 찾을 수 없습니다."),
    ;

    private final HttpStatus status;
    private final String errorCode;
    private final String message;
}
