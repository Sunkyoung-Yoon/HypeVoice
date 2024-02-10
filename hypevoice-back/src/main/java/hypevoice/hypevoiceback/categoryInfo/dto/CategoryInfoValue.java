package hypevoice.hypevoiceback.categoryInfo.dto;

import lombok.Builder;

@Builder
public record CategoryInfoValue(
    Long workId,
    String mediaClassificationValue,
    String voiceToneValue,
    String voiceStyleValue,
    String genderValue,
    String ageValue
){

}
