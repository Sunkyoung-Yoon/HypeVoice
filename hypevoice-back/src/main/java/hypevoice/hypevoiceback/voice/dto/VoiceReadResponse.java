package hypevoice.hypevoiceback.voice.dto;

import lombok.Builder;

@Builder
public record VoiceReadResponse (
        String name,
        String imageUrl,
        String intro,
        String email,
        String phone,
        String addInfo,
        int likes,
        long totalSizeMega
){
}
