package hypevoice.hypevoiceback.voice.dto;

public record VoiceCardListResponse(
        Long voiceId,
        Long workId,
        String photoUrl,
        String mediaClassificationValue,
        String voiceStyleValue,
        String voiceToneValue,
        String genderValue,
        String ageValue,
        String title,
        String recordUrl,
        String imageUrl,
        String name,
        int likes
) {
}
