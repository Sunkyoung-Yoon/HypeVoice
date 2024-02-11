package hypevoice.hypevoiceback.fixture;

import hypevoice.hypevoiceback.member.domain.Member;
import hypevoice.hypevoiceback.voice.domain.Voice;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum VoiceFixture {
    VOICE_01(1L, "윤선경"),
    VOICE_02(2L, "김가빈"),
    VOICE_03(3L, "최재식"),
    VOICE_04(4L, "name1"),
    VOICE_05(5L, "name2"),
    VOICE_06(6L, "name3"),
    VOICE_07(7L, "name4"),
    VOICE_08(8L, "name5"),
    VOICE_09(9L, "name6"),
    VOICE_10(10L, "name7")
    ;

    private final Long voiceId;
    private final String name;

    public Voice toVoice(Member member) {
        return Voice.createVoice(member, name);
    }

}
