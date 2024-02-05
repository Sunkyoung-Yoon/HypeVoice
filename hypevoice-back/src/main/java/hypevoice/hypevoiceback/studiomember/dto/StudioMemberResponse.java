package hypevoice.hypevoiceback.studiomember.dto;

import lombok.Builder;

@Builder
public record StudioMemberResponse(
        Long studioId,
        Long memberId,
        int isHost
) {
}
