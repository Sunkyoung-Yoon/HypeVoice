package hypevoice.hypevoiceback.fixture;

import hypevoice.hypevoiceback.member.domain.Member;
import hypevoice.hypevoiceback.voice.domain.Voice;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum VoiceFixture {
    Voice1(1L, "최재식"),
    Voice2(2L, "김가빈"),
    Voice3(3L, "윤선경");

    private final Long voiceId;
    private final String name;

    public Voice toVoice(Member member) {
        return Voice.createVoice(member, name);
    }

}
