package hypevoice.hypevoiceback.voice.dto;

import lombok.Builder;

@Builder
public record VoiceReadResponse (
        Long memberId,
        String name,
        String imageUrl,
        String intro,
        String email,
        String phone,
        String addInfo,
        int likes
){
}
