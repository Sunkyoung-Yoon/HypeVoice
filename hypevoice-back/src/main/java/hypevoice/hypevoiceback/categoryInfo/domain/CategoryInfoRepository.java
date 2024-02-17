package hypevoice.hypevoiceback.categoryInfo.domain;

import hypevoice.hypevoiceback.categoryInfo.dto.CategoryInfoList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CategoryInfoRepository extends JpaRepository<CategoryInfo, Long> {

    Optional<CategoryInfo> findByWorkId(Long workId);

    @Query("select new hypevoice.hypevoiceback.categoryInfo.dto.CategoryInfoList(c.mediaClassification , c.voiceStyle, c.voiceTone, c.gender, c.age) " +
            "from CategoryInfo c " +
            "where c.work.id = :workId")
    Optional<CategoryInfoList> findCategoryInfoListByWorkId(@Param("workId") Long workId);

    @Query("select c.work.id " +
            "from  CategoryInfo c " +
            "where (:mediaList is null or c.mediaClassification in :mediaList) " +
            "and   (:voiceStyleList is null or c.voiceStyle in :voiceStyleList) " +
            "and   (:voiceToneList is null or c.voiceTone in :voiceToneList) " +
            "and   (:genderList is null or c.gender in :genderList) " +
            "and   (:ageList is null or c.age in :ageList) ")
    Optional<List<Long>> findWorkIdByCategory(List<MediaClassification> mediaList, List<VoiceStyle> voiceStyleList, List<VoiceTone> voiceToneList, List<Gender> genderList, List<Age> ageList);

}
