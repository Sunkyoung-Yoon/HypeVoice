package hypevoice.hypevoiceback.voice.domain;

import hypevoice.hypevoiceback.voice.dto.VoiceCard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface VoiceRepository extends JpaRepository<Voice, Long> {

    @Query("select distinct new hypevoice.hypevoiceback.voice.dto.VoiceCard(v.id, w.photoUrl, w.categoryInfo, w.title, w.recordUrl, v.imageUrl, v.name, v.likes) " +
            "from Voice v right join Work w " +
            "on v.id = w.voice.id " +
            "where w.isRep = 1 and (v.name like concat('%', :keyword, '%') or w.title like concat('%', :keyword, '%')) " )
    Optional <List<VoiceCard>> findByKeyword(String keyword);

}