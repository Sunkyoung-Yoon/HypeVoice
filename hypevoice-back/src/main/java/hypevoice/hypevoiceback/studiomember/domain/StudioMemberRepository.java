package hypevoice.hypevoiceback.studiomember.domain;

import hypevoice.hypevoiceback.member.domain.Member;
import hypevoice.hypevoiceback.studio.domain.Studio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface StudioMemberRepository extends JpaRepository<StudioMember,Long> {

   boolean existsByMemberIdAndStudioId(Long memberId, Long studioId);

   void deleteByMemberIdAndStudioId(Long memberId, Long studioId);

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
