package hypevoice.hypevoiceback.categoryInfo.dto;

import java.util.List;

public record CategoryInfoListRequest(
        List<String> mediaClassificationValueList,
        List<String> voiceToneValueList,
        List<String> voiceStyleValueList,
        List<String> genderValueList,
        List<String> ageValueList
) {
}
