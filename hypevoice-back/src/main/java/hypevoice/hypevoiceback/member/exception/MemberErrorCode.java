package hypevoice.hypevoiceback.member.exception;

import hypevoice.hypevoiceback.global.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum MemberErrorCode implements ErrorCode {
    MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "MEMBER_001", "회원 정보를 찾을 수 없습니다."),
    DUPLICATE_NICKNAME(HttpStatus.CONFLICT, "MEMBER_002", "중복된 닉네임이 존재합니다.")
    ;

    private final HttpStatus status;
    private final String errorCode;
    private final String message;
}
