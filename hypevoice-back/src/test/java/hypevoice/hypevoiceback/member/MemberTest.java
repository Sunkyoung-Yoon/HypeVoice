package hypevoice.hypevoiceback.member;

import hypevoice.hypevoiceback.member.domain.Member;
import hypevoice.hypevoiceback.member.domain.Role;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static hypevoice.hypevoiceback.fixture.MemberFixture.GABIN;
import static hypevoice.hypevoiceback.fixture.MemberFixture.SUNKYOUNG;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayName("Member 도메인 테스트")
public class MemberTest {

    @Test
    @DisplayName("Member를 생성한다")
    public void createMember() {
        Member member = SUNKYOUNG.toMember();

        assertAll(
                () -> assertThat(member.getRole()).isEqualTo(Role.USER),
                () -> assertThat(member.getUsername()).isEqualTo(SUNKYOUNG.getUsername()),
                () -> assertThat(member.getEmail()).isEqualTo(SUNKYOUNG.getEmail()),
                () -> assertThat(member.getSocialType()).isEqualTo(SUNKYOUNG.getSocialType()),
                () -> assertThat(member.getSocialId()).isEqualTo(SUNKYOUNG.getSocialId())
        );
    }

    @Test
    @DisplayName("Member 이름과 이메일을 변경한다")
    public void updateUsernameAndEmail() {
        //given
        Member member1 = SUNKYOUNG.toMember();
        Member member2 = GABIN.toMember();

        //when
        member1.updateMember("새로운이름1", "newEmail1@naver.com");
        member2.updateMember("새로운이름2", "newEmail2@naver.com");

        // then
        assertAll(
                () -> assertThat(member1.getUsername()).isEqualTo("새로운이름1"),
                () -> assertThat(member1.getEmail()).isEqualTo("newEmail1@naver.com"),
                () -> assertThat(member2.getUsername()).isEqualTo("새로운이름2"),
                () -> assertThat(member2.getEmail()).isEqualTo("newEmail2@naver.com")
        );

    }

    @Test
    @DisplayName("Member 닉네임을 변경한다")
    public void updateNickname() {
        //given
        Member member1 = SUNKYOUNG.toMember();
        Member member2 = GABIN.toMember();

        //when
        member1.updateNickname("닉네임 변경1");
        member2.updateNickname("닉네임 변경2");

        // then
        assertAll(
                () -> assertThat(member1.getNickname()).isEqualTo("닉네임 변경1"),
                () -> assertThat(member2.getNickname()).isEqualTo("닉네임 변경2")
        );

    }
}
