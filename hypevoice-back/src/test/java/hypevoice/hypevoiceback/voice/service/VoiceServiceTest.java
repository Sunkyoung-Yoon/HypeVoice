package hypevoice.hypevoiceback.voice.service;

import hypevoice.hypevoiceback.common.ServiceTest;
import hypevoice.hypevoiceback.member.domain.Member;
import hypevoice.hypevoiceback.voice.domain.Voice;
import hypevoice.hypevoiceback.voice.domain.VoiceRepository;
import hypevoice.hypevoiceback.voice.dto.VoiceCardListResponse;
import hypevoice.hypevoiceback.voice.dto.VoiceReadResponse;
import hypevoice.hypevoiceback.work.domain.Work;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static hypevoice.hypevoiceback.fixture.MemberFixture.JAESIK;
import static hypevoice.hypevoiceback.fixture.VoiceFixture.VOICE_01;
import static hypevoice.hypevoiceback.fixture.WorkFixture.WORK_01;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayName("Voice [Service Test] -> VoiceService 테스트")
public class VoiceServiceTest extends ServiceTest {

    @Autowired
    private VoiceRepository voiceRepository;

    @Autowired
    private VoiceService voiceService;

    private Voice voice;
    private Member member;
    private Work work;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    @BeforeEach
    void setUp() {
        member = memberRepository.save(JAESIK.toMember());
        voice = voiceRepository.save(VOICE_01.toVoice(member));
        work = workRepository.save(WORK_01.toWork(voice));
    }

    @Test
    @DisplayName("보이스 정보를 수정한다")
    void updateVoice() {
        // when
        voiceService.updateVoice(member.getId(), voice.getId(), "변경이름",
                "변경소개글", "변경이메일", "변경전화번호", "변경추가정보", null);

        // then
        assertAll(
                () -> assertThat(voice.getName()).isEqualTo("변경이름"),
                () -> assertThat(voice.getImageUrl()).isNull(),
                () -> assertThat(voice.getIntro()).isEqualTo("변경소개글"),
                () -> assertThat(voice.getEmail()).isEqualTo("변경이메일"),
                () -> assertThat(voice.getPhone()).isEqualTo("변경전화번호"),
                () -> assertThat(voice.getAddInfo()).isEqualTo("변경추가정보"),
                () -> assertThat(voice.getCreatedDate().format(formatter)).isEqualTo(LocalDateTime.now().format(formatter)),
                () -> assertThat(voice.getModifiedDate().format(formatter)).isEqualTo(LocalDateTime.now().format(formatter))
        );

    }

    @Test
    @DisplayName("보이스를 상세 조회한다")
    void readDetailVoice() {
        // when
        VoiceReadResponse voiceReadResponse = voiceService.readDetailVoice(member.getId());

        // then
        assertAll(
                () -> assertThat(voiceReadResponse.name()).isEqualTo(voice.getName()),
                () -> assertThat(voiceReadResponse.imageUrl()).isEqualTo(voice.getImageUrl()),
                () -> assertThat(voiceReadResponse.intro()).isEqualTo(voice.getIntro()),
                () -> assertThat(voiceReadResponse.email()).isEqualTo(voice.getEmail()),
                () -> assertThat(voiceReadResponse.phone()).isEqualTo(voice.getPhone()),
                () -> assertThat(voiceReadResponse.addInfo()).isEqualTo(voice.getAddInfo()),
                () -> assertThat(voiceReadResponse.likes()).isEqualTo(voice.getLikes())
        );
    }

}
