package hypevoice.hypevoiceback.member.service;

import hypevoice.hypevoiceback.common.ServiceTest;
import hypevoice.hypevoiceback.member.domain.Member;
import hypevoice.hypevoiceback.member.domain.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static hypevoice.hypevoiceback.fixture.MemberFixture.SUNKYOUNG;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Member [Service Layer] -> MemberService 테스트")
public class MemberServiceTest extends ServiceTest {
    @Autowired
    private MemberService memberService;

    @Autowired
    private MemberFindService memberFindService;

    private Member member;

    @BeforeEach
    void setUp() {
        member = memberRepository.save(SUNKYOUNG.toMember());
    }

    @Test
    @DisplayName("닉네임 변경에 성공한다.")
    void updateNickname() {
        // given
        memberService.updateNickname(member.getId(), "닉네임체인지");

        // when
        Member findMember = memberFindService.findById(member.getId());

        // then
        assertThat(findMember.getNickname()).isEqualTo("닉네임체인지");
    }

    @Test
    @DisplayName("역할(USER) 변경에 성공한다.")
    void updateRole() {
        // given
        memberService.updateRole(member.getId());

        // when
        Member findMember = memberFindService.findById(member.getId());

        // then
        assertThat(findMember.getRole()).isEqualTo(Role.USER);
    }
}
