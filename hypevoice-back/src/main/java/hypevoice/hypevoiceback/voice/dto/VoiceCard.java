package hypevoice.hypevoiceback.voice.dto;

import hypevoice.hypevoiceback.categoryInfo.domain.CategoryInfo;
import lombok.Builder;

@Builder
public record VoiceCard(
        Long voiceId,
        String photoUrl,
        CategoryInfo categoryInfo,
        String title,
        String recordUrl,
        String profileUrl,
        String name,
        int likes
) {
}
