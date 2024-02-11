package hypevoice.hypevoiceback.studio.domain;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface StudioRepository extends JpaRepository<Studio, Long> {

    @Override
    Optional<Studio> findById(Long StudioId);


    @Query("select distinct s " +
            "from Studio s " +
            "where s.title like concat('%', :word, '%')" +
            "or s.intro like concat('%', :word, '%')" +
            "order by s.createdDate desc")
    Page<Studio> findAllByTitleOrIntro(Pageable pageable, String word);

}
