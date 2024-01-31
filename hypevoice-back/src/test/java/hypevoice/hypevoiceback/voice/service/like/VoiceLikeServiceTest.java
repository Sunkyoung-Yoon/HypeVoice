package hypevoice.hypevoiceback.voice.service.like;

import hypevoice.hypevoiceback.common.ServiceTest;
import hypevoice.hypevoiceback.global.exception.BaseException;
import hypevoice.hypevoiceback.member.domain.Member;
import hypevoice.hypevoiceback.voice.domain.Voice;
import hypevoice.hypevoiceback.voice.domain.like.VoiceLike;
import hypevoice.hypevoiceback.voice.excption.VoiceErrorCode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static hypevoice.hypevoiceback.fixture.MemberFixture.GABIN;
import static hypevoice.hypevoiceback.fixture.MemberFixture.SUNKYOUNG;
import static hypevoice.hypevoiceback.fixture.VoiceFixture.Voice1;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayName("Voice [Service Layer] -> VoiceLikeService 테스트")
public class VoiceLikeServiceTest extends ServiceTest {
    @Autowired
    private VoiceLikeService voiceLikeService;

    private Member member1;
    private Member member2;

    private Voice voice;

    @BeforeEach
    void setup() {
        member1 = memberRepository.save(SUNKYOUNG.toMember());
        member2 = memberRepository.save(GABIN.toMember());
        voice = voiceRepository.save(Voice1.toVoice(member1));
    }

    @Nested
    @DisplayName("보이스좋아요 등록")
    class register {
        @Test
        @DisplayName("본인의 보이스는 좋아요를 누를 수 없다")
        void throwExceptionBySelfVoiceLikeNotAllowed() {
            assertThatThrownBy(() -> voiceLikeService.register(member1.getId(), voice.getId()))
                    .isInstanceOf(BaseException.class)
                    .hasMessage(VoiceErrorCode.SELF_VOICE_LIKE_NOT_ALLOWED.getMessage());
        }

        @Test
        @DisplayName("한 보이스에 두 번 이상 좋아요를 누를 수 없다")
        void throwExceptionByAlreadyVoiceLike() {
            // given
            voiceLikeService.register(member2.getId(), voice.getId());

            // when - then
            assertThatThrownBy(() -> voiceLikeService.register(member2.getId(), voice.getId()))
                    .isInstanceOf(BaseException.class)
                    .hasMessage(VoiceErrorCode.ALREADY_VOICE_LIKE.getMessage());
        }

        @Test
        @DisplayName("보이스좋아요 등록에 성공한다")
        void success() {
            // when
            voiceLikeService.register(member2.getId(), voice.getId());

            // then
            VoiceLike findVoiceLike = voiceLikeRepository.findById(1L).orElseThrow();
            assertAll(
                    () -> assertThat(voiceLikeRepository.countByVoice(voice)).isEqualTo(1),
                    () -> assertThat(findVoiceLike.getMember().getId()).isEqualTo(member2.getId()),
                    () -> assertThat(findVoiceLike.getVoice().getId()).isEqualTo(voice.getId())
            );
        }
    }

    @Nested
    @DisplayName("보이스좋아요 취소")
    class cancel {
        @Test
        @DisplayName("좋아요를 누르지 않은 보이스의 좋아요는 취소할 수 없다")
        void throwExceptionByVoiceLikeNotFound() {
            // when - then
            assertThatThrownBy(() -> voiceLikeService.cancel(member2.getId(), voice.getId()))
                    .isInstanceOf(BaseException.class)
                    .hasMessage(VoiceErrorCode.VOICE_LIKE_NOT_FOUND.getMessage());
        }

        @Test
        @DisplayName("보이스좋아요 취소에 성공한다")
        void success() {
            // given
            voiceLikeService.register(member2.getId(), voice.getId());

            // when
            voiceLikeService.cancel(member2.getId(), voice.getId());

            // then
            assertThat(voiceLikeRepository.existsByMemberIdAndVoiceId(member2.getId(), voice.getId())).isFalse();
        }
    }

    @Test
    @DisplayName("보이스좋아요 삭제에 성공한다")
    void success() {
        // given
        voiceLikeService.register(member2.getId(), voice.getId());

        // when
        voiceLikeService.deleteByMember(member2);

        // then
        assertThat(voiceLikeRepository.existsByMemberIdAndVoiceId(member2.getId(), voice.getId())).isFalse();
    }
}
