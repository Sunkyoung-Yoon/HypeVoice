package hypevoice.hypevoiceback.fixture;

import hypevoice.hypevoiceback.member.domain.Member;
import hypevoice.hypevoiceback.voice.domain.Voice;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import static hypevoice.hypevoiceback.fixture.MemberFixture.JAESIK;

@Getter
@RequiredArgsConstructor
public enum VoiceFixture {
    Voice1("최재식"),
    Voice2( "김가빈"),
    Voice3( "윤선경")
    ;

    private final String name;

    public Voice toVoice(Member member) {
        return Voice.createVoice(member, name);
    }

}
