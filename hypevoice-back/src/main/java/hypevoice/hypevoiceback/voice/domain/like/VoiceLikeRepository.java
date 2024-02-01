package hypevoice.hypevoiceback.voice.domain.like;

import hypevoice.hypevoiceback.member.domain.Member;
import hypevoice.hypevoiceback.voice.domain.Voice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface VoiceLikeRepository extends JpaRepository<VoiceLike, Long> {
    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query("DELETE FROM VoiceLike i WHERE i.member.id = :memberId AND i.voice.id = :voiceId")
    void deleteByMemberIdAndVoiceId(@Param("memberId") Long memberId, @Param("voiceId") Long voiceId);

    boolean existsByMemberIdAndVoiceId(Long memberId, Long voiceId);

    int countByVoice(Voice voice);

    void deleteByMember(Member member);
}
