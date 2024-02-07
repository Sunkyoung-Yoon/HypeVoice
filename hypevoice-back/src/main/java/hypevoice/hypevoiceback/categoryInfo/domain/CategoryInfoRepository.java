package hypevoice.hypevoiceback.categoryInfo.domain;

import hypevoice.hypevoiceback.categoryInfo.dto.CategoryInfoList;
import hypevoice.hypevoiceback.voice.dto.VoiceCardListResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CategoryInfoRepository extends JpaRepository<CategoryInfo, Long> {

    Optional<CategoryInfo> findByWorkId(Long workId);

    @Query("select new hypevoice.hypevoiceback.categoryInfo.dto.CategoryInfoList(c.mediaClassification , c.voiceTone, c.voiceStyle, c.gender, c.age) " +
            "from CategoryInfo c " +
            "where c.work.id = :workId")
    Optional<CategoryInfoList> findCategoryInfoListByWorkId(@Param("workId") Long workId);

    @Query("select c.work.id " +
            "from  CategoryInfo c " +
            "where (:mediaClassification is null or c.mediaClassification = :mediaClassification) " +
            "and   (:voiceTone is null or c.voiceTone = :voiceTone) " +
            "and   (:voiceStyle is null or c.voiceStyle = :voiceStyle) " +
            "and   (:gender is null or c.gender = :gender) " +
            "and   (:age is null or c.age = :age) ")
    Optional<List<Long>> findWorkIdByCategory(MediaClassification mediaClassification, VoiceTone voiceTone, VoiceStyle voiceStyle, Gender gender, Age age);

    @Query("select c.work.id " +
            "from  CategoryInfo c " +
            "where c.work.isRep = 1 " +
            "and   (:mediaClassification is null or c.mediaClassification = :mediaClassification) " +
            "and   (:voiceTone is null or c.voiceTone = :voiceTone) " +
            "and   (:voiceStyle is null or c.voiceStyle = :voiceStyle) " +
            "and   (:gender is null or c.gender = :gender) " +
            "and   (:age is null or c.age = :age) ")
    Optional<List<VoiceCardListResponse>> findVoiceCardListResponseByCategory(MediaClassification mediaClassification, VoiceTone voiceTone, VoiceStyle voiceStyle, Gender gender, Age age);
}
