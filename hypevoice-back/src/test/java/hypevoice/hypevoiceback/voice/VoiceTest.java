package hypevoice.hypevoiceback.voice;

import hypevoice.hypevoiceback.member.domain.Member;
import hypevoice.hypevoiceback.voice.domain.Voice;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static hypevoice.hypevoiceback.fixture.MemberFixture.JAESIK;
import static hypevoice.hypevoiceback.fixture.VoiceFixture.Voice1;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayName("Voice 도메인 테스트")
public class VoiceTest {

    @Test
    @DisplayName("Voice를 생성한다")
    public void createVoice() {
        Member member = JAESIK.toMember();
        Voice voice = Voice1.toVoice(member);

        assertAll(
                () -> assertThat(voice.getMember()).isEqualTo(member),
                () -> assertThat(voice.getName()).isEqualTo(Voice1.getName()),
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
        // given
        Member member = JAESIK.toMember();
        Voice voice = Voice1.toVoice(member);

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
        // given
        Member member = JAESIK.toMember();
        Voice voice = Voice1.toVoice(member);

        // when : 0 -> 1
        voice.increaseLikes();

        // then
        assertThat(voice.getLikes()).isEqualTo(1);
    }

    @Test
    @DisplayName("Voice 좋아요수를 감소시킨다")
    public void decreaseLikes() {
        // given
        Member member = JAESIK.toMember();
        Voice voice = Voice1.toVoice(member);

        // when : 0 -> -1
        voice.decreaseLikes();

        // then
        assertThat(voice.getLikes()).isEqualTo(-1);
    }

}
