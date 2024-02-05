package hypevoice.hypevoiceback.studiomember.domain;

import org.springframework.data.jpa.repository.JpaRepository;

public interface StudioMemberRepository extends JpaRepository<StudioMember,Long> {

   boolean existsByMemberIdAndStudioId(Long memberId, Long studioId);

   void deleteByMemberIdAndStudioId(Long memberId, Long studioId);

}
