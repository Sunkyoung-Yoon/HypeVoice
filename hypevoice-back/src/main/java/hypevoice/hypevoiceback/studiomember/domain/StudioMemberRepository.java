package hypevoice.hypevoiceback.studiomember.domain;

import hypevoice.hypevoiceback.member.domain.Member;
import hypevoice.hypevoiceback.studio.domain.Studio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface StudioMemberRepository extends JpaRepository<StudioMember,Long> {

   Optional<StudioMember> findByMemberIdAndStudioId(Long memberId, Long studioid);

   boolean existsByMemberIdAndStudioId(Long memberId, Long studioId);

   void deleteByMemberIdAndStudioId(Long memberId, Long studioId);
   void deleteByStudioId(Long studioId);
   @Query("select m " +
           "from Member m left  join StudioMember sm " +
           "on m.id = sm.member.id " +
           "where sm.studio.id = :studioId")
   List<Member> findAllByStudioId(Long studioId);

   @Query("select s " +
           "from Studio s left  join StudioMember sm " +
           "on s.id = sm.studio.id " +
           "where sm.member.id = :memberId")
   Studio findByMemberId(Long memberId);
}
