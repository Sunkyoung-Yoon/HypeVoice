package hypevoice.hypevoiceback.categoryInfo.dto;

public record CategoryInfoRequest (
        String mediaClassification,
        String voiceStyle,
        String voiceTone,
        String gender,
        String age
){

}
