package hypevoice.hypevoiceback.member.dto;

import lombok.Builder;

@Builder
public record MemberResponse(
        Long memberId,
        String username,
        String nickname,
        String email,
        String profileUrl
) {
}
