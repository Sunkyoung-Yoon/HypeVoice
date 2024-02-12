package hypevoice.hypevoiceback.voice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
public record VoiceUpdateRequest(
        @NotBlank(message = "보이스 이름 입력은 필수입니다.")
        @Size(min = 1, message = "보이스 이름은 최소 1자 이상으로 작성해주세요.")
        @Size(max = 10, message = "보이스 이름은 최대 10자 이내로 작성해주세요.")
        String name,
        String intro,
        String email,
        String phone,
        String addInfo
) {
}
