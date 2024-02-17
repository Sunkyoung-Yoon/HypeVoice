package hypevoice.hypevoiceback.categoryInfo.dto;

import java.util.List;

public record CategoryInfoListRequest(
        List<String> mediaClassificationValueList,
        List<String> voiceStyleValueList,
        List<String> voiceToneValueList,
        List<String> genderValueList,
        List<String> ageValueList
) {
}
