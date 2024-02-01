package hypevoice.hypevoiceback.member.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static hypevoice.hypevoiceback.fixture.MemberFixture.GABIN;
import static hypevoice.hypevoiceback.fixture.MemberFixture.SUNKYOUNG;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayName("Member 도메인 테스트")
public class MemberTest {

    private Member member1;
    private Member member2;

    @BeforeEach
    void setUp() {
        member1 = SUNKYOUNG.toMember();
        member2 = GABIN.toMember();
    }

    @Test
    @DisplayName("Member를 생성한다")
    public void createMember() {

        assertAll(
                () -> assertThat(member1.getRole()).isEqualTo(Role.GUEST),
                () -> assertThat(member1.getUsername()).isEqualTo(SUNKYOUNG.getUsername()),
                () -> assertThat(member1.getEmail()).isEqualTo(SUNKYOUNG.getEmail()),
                () -> assertThat(member1.getNickname()).isNull(),
                () -> assertThat(member1.getSocialType()).isEqualTo(SUNKYOUNG.getSocialType()),
                () -> assertThat(member1.getSocialId()).isEqualTo(SUNKYOUNG.getSocialId())
        );
    }

    @Test
    @DisplayName("Member 이름과 이메일을 변경한다")
    public void updateUsernameAndEmail() {

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
    @DisplayName("Member 닉네임과 프로필이미지Url을 변경한다")
    public void update() {

        //when
        member1.update("닉네임 변경1", "프로필 이미지 Url 변경");

        // then
        assertAll(
                () -> assertThat(member1.getNickname()).isEqualTo("닉네임 변경1"),
                () -> assertThat(member1.getProfileUrl()).isEqualTo("프로필 이미지 Url 변경")
        );

    }

    @Test
    @DisplayName("Member 역할을 변경한다")
    public void updateRole() {

        // when
        member1.updateRole(Role.ADMIN);

        // then
        assertAll(
                () -> assertThat(member1.getRole()).isEqualTo(Role.ADMIN),
                () -> assertThat(member2.getRole()).isEqualTo(Role.GUEST)
        );
    }
}
