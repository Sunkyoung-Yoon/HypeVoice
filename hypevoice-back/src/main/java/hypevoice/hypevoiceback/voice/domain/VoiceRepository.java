package hypevoice.hypevoiceback.voice.domain;

import org.springframework.data.jpa.repository.JpaRepository;

public interface VoiceRepository extends JpaRepository<Voice,Long> {
}