package hypevoice.hypevoiceback.studio.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StudioRepository extends JpaRepository<Studio, Long> {

    @Override
    Optional<Studio> findById(Long StudioId);

}
