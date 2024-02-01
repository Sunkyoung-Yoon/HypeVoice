package hypevoice.hypevoiceback.voice.domain.like;

import hypevoice.hypevoiceback.member.domain.Member;
import hypevoice.hypevoiceback.voice.domain.Voice;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "voice_like")
public class VoiceLike {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "voice_like_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "member_id", referencedColumnName = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "voice_id", referencedColumnName = "voice_id")
    private Voice voice;

    private VoiceLike(Member member, Voice voice) {
        this.member = member;
        this.voice = voice;
    }

    public static VoiceLike registerVoiceLike(Member member, Voice voice) {
        return new VoiceLike(member, voice);
    }
}
