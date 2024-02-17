package hypevoice.hypevoiceback.work.dto;

import hypevoice.hypevoiceback.categoryInfo.dto.CategoryInfoValue;
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
        int isRep,
        CategoryInfoValue categoryInfoValue

) {

}
