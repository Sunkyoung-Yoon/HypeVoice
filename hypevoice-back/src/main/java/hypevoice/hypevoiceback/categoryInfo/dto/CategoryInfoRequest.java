package hypevoice.hypevoiceback.categoryInfo.dto;

public record CategoryInfoRequest (
        String mediaClassification,
        String voiceTone,
        String voiceStyle,
        String gender,
        String age
){

}
