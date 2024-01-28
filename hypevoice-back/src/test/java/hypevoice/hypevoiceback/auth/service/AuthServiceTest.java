package hypevoice.hypevoiceback.auth.service;

import com.fasterxml.jackson.annotation.JacksonInject;
import hypevoice.hypevoiceback.auth.domain.Token;
import hypevoice.hypevoiceback.auth.dto.LoginResponse;
import hypevoice.hypevoiceback.auth.security.jwt.JwtProvider;
import hypevoice.hypevoiceback.common.ServiceTest;
import hypevoice.hypevoiceback.member.domain.Member;
import hypevoice.hypevoiceback.member.service.MemberFindService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static hypevoice.hypevoiceback.fixture.MemberFixture.SUNKYOUNG;

@DisplayName("Auth [Service Layer] -> AuthService 테스트")
public class AuthServiceTest extends ServiceTest {
    @Autowired
    private AuthService authService;

    @Autowired
    private JwtProvider jwtProvider;

    @Autowired
    private MemberFindService memberFindService;

    private Member member;
    private Token token;

    @BeforeEach
    void setUp() {
        member = memberRepository.save(SUNKYOUNG.toMember());
    }

    @Test
    @DisplayName("로그인에 성공한다")
    void successLogin() {
        // when
        LoginResponse loginResponse = authService.login(member.getSocialType(), member.getEmail());

        System.out.println(loginResponse);
        // then
        Assertions.assertAll(
                () -> assertThat(loginResponse).isNotNull(),
                () -> assertThat(jwtProvider.getId(loginResponse.accessToken())).isEqualTo(member.getId()),
                () -> assertThat(jwtProvider.getId(loginResponse.tokenResponse().refreshToken())).isEqualTo(member.getId()),
                () -> {
                    Token findToken = tokenRepository.findByMemberId(member.getId()).orElseThrow();
                    assertThat(findToken.getRefreshToken()).isEqualTo(loginResponse.tokenResponse().refreshToken());
                }
        );
    }

    @Test
    @DisplayName("로그아웃에 성공한다")
    void successLogout() {
        // given
        authService.login(member.getSocialType(), member.getEmail());

        // when
        authService.logout(member.getId());

        // then
        Optional<Token> findToken = tokenRepository.findByMemberId(member.getId());
        assertThat(findToken).isEmpty();
        }
}
