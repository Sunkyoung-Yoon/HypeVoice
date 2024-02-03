package hypevoice.hypevoiceback.categoryInfo.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CategoryInfoRepository extends JpaRepository<CategoryInfo, Long> {

    Optional<CategoryInfo> findById(Long categoryId);

    Optional<CategoryInfo> findByWorkId(Long workId);
}
