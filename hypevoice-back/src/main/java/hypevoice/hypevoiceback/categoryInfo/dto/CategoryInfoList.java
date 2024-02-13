package hypevoice.hypevoiceback.categoryInfo.dto;

import hypevoice.hypevoiceback.categoryInfo.domain.*;

// 작업물 리스트 안 CategoryInfoResponse
public record CategoryInfoList(
        MediaClassification mediaClassification,
        VoiceStyle voiceStyle,
        VoiceTone voiceTone,
        Gender gender,
        Age age
) {

}
