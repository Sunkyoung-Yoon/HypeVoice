package hypevoice.hypevoiceback.voice.dto;

public record VoiceCardListResponse(
        Long voiceId,
        String photoUrl,
        String mediaClassificationValue,
        String voiceToneValue,
        String voiceStyleValue,
        String genderValue,
        String ageValue,
        String title,
        String recordUrl,
        String profileUrl,
        String name,
        int likes
) {
}
