package hypevoice.hypevoiceback.categoryInfo.domain;

import hypevoice.hypevoiceback.categoryInfo.dto.CategoryInfoListResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CategoryInfoRepository extends JpaRepository<CategoryInfo, Long> {

    Optional<CategoryInfo> findByWorkId(Long workId);

    @Query("select new hypevoice.hypevoiceback.categoryInfo.dto.CategoryInfoListResponse(c.mediaClassification , c.voiceTone, c.voiceStyle, c.gender, c.age) " +
            "from CategoryInfo c " +
            "where c.work.id = :workId")
    Optional<CategoryInfoListResponse> findCategoryInfoListByWorkId(@Param("workId") Long workId);

    @Query("select c.work.id " +
            "from  CategoryInfo c " +
            "where (:mediaClassification IS NULL OR c.mediaClassification = :mediaClassification) " +
            "AND   (:voiceTone IS NULL OR c.voiceTone = :voiceTone) " +
            "AND   (:voiceStyle IS NULL OR c.voiceStyle = :voiceStyle) " +
            "AND   (:gender IS NULL OR c.gender = :gender) " +
            "AND   (:age IS NULL OR c.age = :age) ")
    Optional<List<Long>> findWorkIdByCategory(MediaClassification mediaClassification, VoiceTone voiceTone, VoiceStyle voiceStyle, Gender gender, Age age);
}
