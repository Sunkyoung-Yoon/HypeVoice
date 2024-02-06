package hypevoice.hypevoiceback.work.domain;

import hypevoice.hypevoiceback.work.dto.WorkList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface WorkRepository extends JpaRepository<Work,Long> {
    Optional<Work> findById(Long workId);

    @Query("select new hypevoice.hypevoiceback.work.dto.WorkList(w.id, w.title, w.videoLink, w.photoUrl, w.scriptUrl, w.recordUrl, w.info, w.isRep) " +
            "from Work w " +
            "where w.voice.id = :voiceId")
    List<WorkList> findAllByVoiceId(@Param("voiceId") Long voiceId);

}
