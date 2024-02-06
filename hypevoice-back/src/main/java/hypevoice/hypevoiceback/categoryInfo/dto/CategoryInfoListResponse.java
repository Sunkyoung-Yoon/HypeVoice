package hypevoice.hypevoiceback.categoryInfo.dto;

import hypevoice.hypevoiceback.categoryInfo.domain.*;

// 작업물 리스트 안 CategoryInfoResponse
public record CategoryInfoListResponse(
        MediaClassification mediaClassification,
        VoiceTone voiceTone,
        VoiceStyle voiceStyle,
        Gender gender,
        Age age
) {

}
