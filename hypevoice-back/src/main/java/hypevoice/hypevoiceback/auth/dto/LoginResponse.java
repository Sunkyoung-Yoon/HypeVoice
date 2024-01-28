package hypevoice.hypevoiceback.auth.dto;

public record LoginResponse(
        Long memberId,
        String nickname,

        String role,
        String accessToken,
        TokenResponse tokenResponse
) {
}
