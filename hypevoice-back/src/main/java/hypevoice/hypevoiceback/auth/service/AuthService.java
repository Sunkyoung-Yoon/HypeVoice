package hypevoice.hypevoiceback.auth.service;

import hypevoice.hypevoiceback.auth.dto.LoginResponse;
import hypevoice.hypevoiceback.auth.dto.TokenResponse;
import hypevoice.hypevoiceback.auth.security.jwt.JwtProvider;
import hypevoice.hypevoiceback.member.domain.Member;
import hypevoice.hypevoiceback.member.domain.SocialType;
import hypevoice.hypevoiceback.member.service.MemberFindService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AuthService {
    private final MemberFindService memberFindService;
    private final JwtProvider jwtProvider;
    private final TokenService tokenService;

    @Transactional
    public LoginResponse login(SocialType socialType, String email) {
        Member member = memberFindService.findBySocialTypeAndEmail(socialType, email);

        String accessToken = jwtProvider.createAccessToken(member.getId(), member.getRole());
        String refreshToken = jwtProvider.createRefreshToken(member.getId(), member.getRole());
        tokenService.synchronizeRefreshToken(member.getId(), refreshToken);

        return new LoginResponse(
                member.getId(),
                member.getNickname(),
                member.getRoleKey(),
                accessToken,
                new TokenResponse(accessToken, refreshToken)
        );
    }

    @Transactional
    public void logout(Long memberId) {
        tokenService.deleteRefreshTokenByMemberId(memberId);
    }
}
