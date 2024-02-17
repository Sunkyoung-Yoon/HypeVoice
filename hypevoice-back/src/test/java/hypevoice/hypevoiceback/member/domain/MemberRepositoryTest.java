package hypevoice.hypevoiceback.member.domain;

import hypevoice.hypevoiceback.common.RepositoryTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static hypevoice.hypevoiceback.fixture.MemberFixture.SUNKYOUNG;

@DisplayName("Member [Repository Test] -> MemberRepository 테스트")
public class MemberRepositoryTest extends RepositoryTest {
    @Autowired
    private MemberRepository memberRepository;

    private Member member;

    @BeforeEach
    void setUp() {
        member = memberRepository.save(SUNKYOUNG.toMember());
    }

    @Test
    @DisplayName("소셜타입과 이메일로 사용자를 조회한다")
    void findBySocialTypeAndEmail() {
        // when
        Member findMember = memberRepository.findBySocialTypeAndEmail(SUNKYOUNG.getSocialType(), SUNKYOUNG.getEmail()).orElseThrow();

        // then
        assertAll(
                () -> assertThat(findMember.getRole()).isEqualTo(Role.GUEST),
                () -> assertThat(findMember.getUsername()).isEqualTo(SUNKYOUNG.getUsername()),
                () -> assertThat(findMember.getEmail()).isEqualTo(SUNKYOUNG.getEmail()),
                () -> assertThat(findMember.getNickname()).isNull(),
                () -> assertThat(findMember.getSocialType()).isEqualTo(SUNKYOUNG.getSocialType()),
                () -> assertThat(findMember.getSocialId()).isEqualTo(SUNKYOUNG.getSocialId())
        );
    }

    @Test
    @DisplayName("ID(PK)로 사용자를 확인한다")
    void findById() {
        // when
        Member findMember = memberRepository.findById(member.getId()).orElseThrow();

        // then
        assertAll(
                () -> assertThat(findMember.getRole()).isEqualTo(Role.GUEST),
                () -> assertThat(findMember.getUsername()).isEqualTo(SUNKYOUNG.getUsername()),
                () -> assertThat(findMember.getEmail()).isEqualTo(SUNKYOUNG.getEmail()),
                () -> assertThat(findMember.getNickname()).isNull(),
                () -> assertThat(findMember.getSocialType()).isEqualTo(SUNKYOUNG.getSocialType()),
                () -> assertThat(findMember.getSocialId()).isEqualTo(SUNKYOUNG.getSocialId())
        );
    }

    @Test
    @DisplayName("이메일로 사용자를 조회한다")
    void findByEmail() {
        // when
        Member findMember = memberRepository.findByEmail(SUNKYOUNG.getEmail()).orElseThrow();

        // then
        assertAll(
                () -> assertThat(findMember.getRole()).isEqualTo(Role.GUEST),
                () -> assertThat(findMember.getUsername()).isEqualTo(SUNKYOUNG.getUsername()),
                () -> assertThat(findMember.getEmail()).isEqualTo(SUNKYOUNG.getEmail()),
                () -> assertThat(findMember.getNickname()).isNull(),
                () -> assertThat(findMember.getSocialType()).isEqualTo(SUNKYOUNG.getSocialType()),
                () -> assertThat(findMember.getSocialId()).isEqualTo(SUNKYOUNG.getSocialId())
        );
    }

}
