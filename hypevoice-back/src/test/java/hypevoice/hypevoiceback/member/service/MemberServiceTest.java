package hypevoice.hypevoiceback.member.service;

import hypevoice.hypevoiceback.common.ServiceTest;
import hypevoice.hypevoiceback.global.exception.BaseException;
import hypevoice.hypevoiceback.member.domain.Member;
import hypevoice.hypevoiceback.member.domain.Role;
import hypevoice.hypevoiceback.member.exception.MemberErrorCode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static hypevoice.hypevoiceback.fixture.MemberFixture.GABIN;
import static hypevoice.hypevoiceback.fixture.MemberFixture.SUNKYOUNG;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayName("Member [Service Layer] -> MemberService 테스트")
public class MemberServiceTest extends ServiceTest {
    @Autowired
    private MemberService memberService;

    @Autowired
    private MemberFindService memberFindService;

    private Member member1;
    private Member member2;

    @BeforeEach
    void setUp() {
        member1 = memberRepository.save(SUNKYOUNG.toMember());
        member2 = memberRepository.save(GABIN.toMember());
    }

    @Nested
    @DisplayName("회원 정보 수정")
    class updateNickname {
        @Test
        @DisplayName("중복된 닉네임이 존재한다면 회원 정보 수정에 실패한다")
        void throwExceptionByDuplicateNickname() {
            // given
            memberService.update(member1.getId(), "닉네임체인지", null);

            // when - then
            assertThatThrownBy(() -> memberService.update(member2.getId(), "닉네임체인지", "프로필이미지Url"))
                    .isInstanceOf(BaseException.class)
                    .hasMessage(MemberErrorCode.DUPLICATE_NICKNAME.getMessage());
        }

        @Test
        @DisplayName("회원 정보 변경에 성공한다.")
        void success() {
            // given
            memberService.update(member1.getId(), "닉네임체인지", "프로필이미지Url");

            // when
            Member findMember = memberFindService.findById(member1.getId());

            // then
            assertAll(
                    () -> assertThat(findMember.getNickname()).isEqualTo("닉네임체인지"),
                    () -> assertThat(findMember.getProfileUrl()).isEqualTo("프로필이미지Url")
            );
        }
    }

    @Test
    @DisplayName("역할(USER) 변경에 성공한다.")
    void updateRole() {
        // given
        memberService.updateRole(member1.getId());

        // when
        Member findMember = memberFindService.findById(member1.getId());

        // then
        assertThat(findMember.getRole()).isEqualTo(Role.USER);
    }
}
