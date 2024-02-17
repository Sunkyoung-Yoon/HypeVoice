package hypevoice.hypevoiceback.auth.service;

import hypevoice.hypevoiceback.auth.dto.TokenResponse;
import hypevoice.hypevoiceback.auth.exception.AuthErrorCode;
import hypevoice.hypevoiceback.auth.security.jwt.JwtProvider;
import hypevoice.hypevoiceback.global.exception.BaseException;
import hypevoice.hypevoiceback.member.domain.Member;
import hypevoice.hypevoiceback.member.service.MemberFindService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TokenReissueService {
    private final TokenService tokenService;
    private final JwtProvider jwtProvider;
    private final MemberFindService memberFindService;

    @Transactional
    public TokenResponse reissueTokens(Long memberId, String refreshToken) {
        Member findMember = memberFindService.findById(memberId);

        // 사용자가 보유하고 있는 Refresh Token인지
        if (!tokenService.isRefreshTokenExists(memberId, refreshToken)) {
            throw BaseException.type(AuthErrorCode.AUTH_INVALID_TOKEN);
        }

        String newAccessToken = jwtProvider.createAccessToken(findMember.getId(), findMember.getRole());
        String newRefreshToken = jwtProvider.createRefreshToken(findMember.getId(), findMember.getRole());

        // RTR 정책에 의해 memberId에 해당하는 사용자가 보유하고 있는 Refresh Token 업데이트
        tokenService.reissueRefreshTokenByRtrPolicy(memberId, newRefreshToken);

        return new TokenResponse(newAccessToken, newRefreshToken);
    }
}
