package hypevoice.hypevoiceback.categoryInfo.dto;

import lombok.Builder;

@Builder
public record CategoryInfoResponse (
    Long workId,
    String mediaClassificationValue,
    String voiceToneValue,
    String voiceStyleValue,
    String genderValue,
    String ageValue
){

}
