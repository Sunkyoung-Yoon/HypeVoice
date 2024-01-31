package hypevoice.hypevoiceback.voice.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface VoiceRepository extends JpaRepository<Voice, Long> {

    Optional<Voice> findByMemberId(Long memberId);
}