package hypevoice.hypevoiceback.voice.domain;

import hypevoice.hypevoiceback.member.domain.Member;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static hypevoice.hypevoiceback.fixture.MemberFixture.JAESIK;
import static hypevoice.hypevoiceback.fixture.VoiceFixture.VOICE_01;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayName("Voice 도메인 테스트")
public class VoiceTest {

    private Member member;
    private Voice voice;

    @BeforeEach
    void setUp() {
        member = JAESIK.toMember();
        voice = VOICE_01.toVoice(member);
    }

    @Test
    @DisplayName("Voice를 생성한다")
    public void createVoice() {

        assertAll(
                () -> assertThat(voice.getMember()).isEqualTo(member),
                () -> assertThat(voice.getName()).isEqualTo(VOICE_01.getName()),
                () -> assertThat(voice.getImageUrl()).isEqualTo(null),
                () -> assertThat(voice.getIntro()).isEqualTo(null),
                () -> assertThat(voice.getEmail()).isEqualTo(null),
                () -> assertThat(voice.getPhone()).isEqualTo(null),
                () -> assertThat(voice.getAddInfo()).isEqualTo(null),
                () -> assertThat(voice.getLikes()).isEqualTo(0)
        );
    }

    @Test
    @DisplayName("Voice의 내용들을 변경한다")
    public void updateVoice() {
        // when
        voice.updateVoice("변경이름1", "image1", "소개1", "test1@ssafy.com", "01011112222", "추가정보1");

        // then
        assertAll(
                () -> assertThat(voice.getName()).isEqualTo("변경이름1"),
                () -> assertThat(voice.getImageUrl()).isEqualTo("image1"),
                () -> assertThat(voice.getIntro()).isEqualTo("소개1"),
                () -> assertThat(voice.getEmail()).isEqualTo("test1@ssafy.com"),
                () -> assertThat(voice.getPhone()).isEqualTo("01011112222"),
                () -> assertThat(voice.getAddInfo()).isEqualTo("추가정보1"),
                () -> assertThat(voice.getLikes()).isEqualTo(0)
        );
    }

    @Test
    @DisplayName("Voice 좋아요수를 증가시킨다")
    public void increaseLikes() {

        // when : 0 -> 1
        voice.increaseLikes();

        // then
        assertThat(voice.getLikes()).isEqualTo(1);
    }

    @Test
    @DisplayName("Voice 좋아요수를 감소시킨다")
    public void decreaseLikes() {

        // when : 0 -> -1
        voice.decreaseLikes();

        // then
        assertThat(voice.getLikes()).isEqualTo(-1);
    }

}
