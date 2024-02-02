package hypevoice.hypevoiceback.work.dto;

import lombok.Builder;

@Builder
public record WorkResponse(
        Long voiceId,
        Long workId,
        String title,
        String videoLink,
        String photoUrl,
        String scriptUrl,
        String recordUrl,
        String info,
        int isRep
) {

}
