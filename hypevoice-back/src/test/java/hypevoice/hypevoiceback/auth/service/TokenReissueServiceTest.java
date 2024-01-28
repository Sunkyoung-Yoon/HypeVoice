package hypevoice.hypevoiceback.auth.service;

import hypevoice.hypevoiceback.auth.domain.Token;
import hypevoice.hypevoiceback.auth.dto.TokenResponse;
import hypevoice.hypevoiceback.auth.exception.AuthErrorCode;
import hypevoice.hypevoiceback.auth.security.jwt.JwtProvider;
import hypevoice.hypevoiceback.common.ServiceTest;
import hypevoice.hypevoiceback.global.exception.BaseException;
import hypevoice.hypevoiceback.member.domain.Member;
import hypevoice.hypevoiceback.member.domain.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static hypevoice.hypevoiceback.fixture.MemberFixture.SUNKYOUNG;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;


@DisplayName("Auth [Service Layer] -> TokenReissueService 테스트")
class TokenReissueServiceTest extends ServiceTest {
    @Autowired
    private TokenReissueService tokenReissueService;

    @Autowired
    private JwtProvider jwtProvider;

    private Member member;
    private String REFRESHTOKEN;

    @BeforeEach
    void setup() {
        member = memberRepository.save(SUNKYOUNG.toMember());
        REFRESHTOKEN = jwtProvider.createRefreshToken(member.getId(), member.getRole());
    }

    @Nested
    @DisplayName("토큰 재발급")
    class reissueTokens {
        @Test
        @DisplayName("RefreshToken이 유효하지 않으면 예외가 발생한다")
        void throwExceptionByAuthInvalidToken() {
            // when - then
            assertThatThrownBy(() -> tokenReissueService.reissueTokens(member.getId(), REFRESHTOKEN))
                    .isInstanceOf(BaseException.class)
                    .hasMessage(AuthErrorCode.AUTH_INVALID_TOKEN.getMessage());
        }

        @Test
        @DisplayName("RefreshToken을 이용해 AccessToken과 RefreshToken을 재발급받는데 성공한다")
        void success() {
            // given
            tokenRepository.save(Token.createToken(member.getId(), REFRESHTOKEN));

            // when
            TokenResponse tokenResponse = tokenReissueService.reissueTokens(member.getId(), REFRESHTOKEN);

            // then
            assertAll(
                    () -> assertThat(tokenResponse).isNotNull(),
                    () -> assertThat(tokenResponse).usingRecursiveComparison().isNotNull()
            );
        }
    }
}
