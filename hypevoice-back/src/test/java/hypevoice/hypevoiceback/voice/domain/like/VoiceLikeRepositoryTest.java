package hypevoice.hypevoiceback.voice.domain.like;

import hypevoice.hypevoiceback.common.RepositoryTest;
import hypevoice.hypevoiceback.member.domain.Member;
import hypevoice.hypevoiceback.member.domain.MemberRepository;
import hypevoice.hypevoiceback.voice.domain.Voice;
import hypevoice.hypevoiceback.voice.domain.VoiceRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static hypevoice.hypevoiceback.fixture.MemberFixture.*;
import static hypevoice.hypevoiceback.fixture.VoiceFixture.Voice1;
import static hypevoice.hypevoiceback.fixture.VoiceFixture.Voice2;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayName("Voice [Repository Layer] -> VoiceLikeRepository 테스트")
public class VoiceLikeRepositoryTest extends RepositoryTest {
    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private VoiceRepository voiceRepository;

    @Autowired
    private VoiceLikeRepository voiceLikeRepository;

    private Member[] member = new Member[3];
    private Voice[] voice = new Voice[3];

    @BeforeEach
    void setup() {
        member[0] = memberRepository.save(SUNKYOUNG.toMember());
        member[1] = memberRepository.save(GABIN.toMember());
        member[2] = memberRepository.save(JAESIK.toMember());
        voice[0] = voiceRepository.save(Voice1.toVoice(member[0]));
        voice[1] = voiceRepository.save(Voice2.toVoice(member[1]));
        voice[2] = voiceRepository.save(Voice2.toVoice(member[2]));
    }

    @Test
    @DisplayName("좋아요를 누른 회원 ID와 좋아요가 눌린 보이스 ID를 이용하여 보이스좋아요 정보가 존재하는지 확인한다")
    void existsByMemberIdAndVoiceId() {
        // given
        voiceLikeRepository.save(VoiceLike.registerVoiceLike(member[0], voice[1]));

        // when
        boolean actual1 = voiceLikeRepository.existsByMemberIdAndVoiceId(member[0].getId(), voice[1].getId());
        boolean actual2 = voiceLikeRepository.existsByMemberIdAndVoiceId(member[0].getId(), voice[2].getId());

        // then
        assertAll(
                () -> assertThat(actual1).isTrue(),
                () -> assertThat(actual2).isFalse()
        );
    }

    @Test
    @DisplayName("좋아요를 누른 회원 ID와 좋아요가 눌린 보이스 ID를 이용하여 보이스좋아요 정보를 삭제한다")
    void deleteByMemberIdAndVoiceId() {
        // given
        voiceLikeRepository.save(VoiceLike.registerVoiceLike(member[0], voice[1]));

        // when
        voiceLikeRepository.deleteByMemberIdAndVoiceId(member[0].getId(), voice[1].getId());

        // then
        assertThat(voiceLikeRepository.existsByMemberIdAndVoiceId(member[0].getId(), voice[1].getId())).isFalse();
    }

    @Test
    @DisplayName("보이스에 달린 좋아요 수를 확인한다")
    void countByVoice() {
        // given
        voiceLikeRepository.save(VoiceLike.registerVoiceLike(member[0], voice[2]));
        voiceLikeRepository.save(VoiceLike.registerVoiceLike(member[1], voice[2]));

        // when - then
        assertThat(voiceLikeRepository.countByVoice(voice[2])).isEqualTo(2L);
    }

    @Test
    @DisplayName("회원 ID를 통해 해당 회원의 좋아요를 모두 삭제한다")
    void deleteByMember() {
        // given
        voiceLikeRepository.save(VoiceLike.registerVoiceLike(member[0], voice[1]));
        voiceLikeRepository.save(VoiceLike.registerVoiceLike(member[0], voice[2]));

        // when
        voiceLikeRepository.deleteByMember(member[0]);

        // then
        assertThat(voiceLikeRepository.findAll().isEmpty()).isTrue();
    }
}