package hypevoice.hypevoiceback.studiomember.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Builder;

@Builder
public record StudioMemberRequest(
        @NotBlank
        Long studioId,
        @NotBlank
        Long memberId,
        @PositiveOrZero(message = "0 이상의 수만 설정 가능합니다.")
        @Max(value = 1, message = "1 이하의 값만 설정 가능합니다.")
        int isHost
) {
}
