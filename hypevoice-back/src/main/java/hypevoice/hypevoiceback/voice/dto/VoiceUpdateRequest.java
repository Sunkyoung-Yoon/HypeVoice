package hypevoice.hypevoiceback.voice.dto;

import lombok.Builder;

@Builder
public record VoiceUpdateRequest(
        String name,
        String intro,
        String email,
        String phone,
        String addInfo
) {
}
