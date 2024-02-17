package hypevoice.hypevoiceback.studio.dto;

import lombok.Builder;

@Builder
public record StudioResponse(
        Long studioId,
        String sessionId,
        String title,
        String intro,
        int memberCount,
        int limitNumber,
        int isPublic,
        int onAir
) {

}
