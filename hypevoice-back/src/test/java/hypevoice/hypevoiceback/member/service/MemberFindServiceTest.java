package hypevoice.hypevoiceback.member.service;

import hypevoice.hypevoiceback.common.ServiceTest;
import hypevoice.hypevoiceback.global.exception.BaseException;
import hypevoice.hypevoiceback.member.domain.Member;
import hypevoice.hypevoiceback.member.exception.MemberErrorCode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static hypevoice.hypevoiceback.fixture.MemberFixture.SUNKYOUNG;

@DisplayName("Member [Service Layer] -> MemberFindService 테스트")
public class MemberFindServiceTest extends ServiceTest {
    @Autowired
    private MemberFindService memberFindService;

    private Member member;

    @BeforeEach
    void setUp() {
        member = memberRepository.save(SUNKYOUNG.toMember());
    }

    @Test
    @DisplayName("소셜타입과 이메일로 사용자를 조회한다")
    void findBySocialTypeAndEmail() {
        // when
        Member findMember = memberFindService.findBySocialTypeAndEmail(member.getSocialType(), member.getEmail());

        // then
        assertThat(findMember).isEqualTo(findMember);
        assertThatThrownBy(() -> memberFindService.findBySocialTypeAndEmail(member.getSocialType(), "fff"+ member.getEmail()))
                .isInstanceOf(BaseException.class)
                .hasMessage(MemberErrorCode.MEMBER_NOT_FOUND.getMessage());
    }

    @Test
    @DisplayName("ID(PK)로 회원을 조회한다")
    void findById() {
        // when
        Member findMember = memberFindService.findById(member.getId());

        // then
        assertThat(findMember).isEqualTo(findMember);
        assertThatThrownBy(() -> memberFindService.findById(member.getId() + 100L))
                .isInstanceOf(BaseException.class)
                .hasMessage(MemberErrorCode.MEMBER_NOT_FOUND.getMessage());
    }
}
