package hypevoice.hypevoiceback.member.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findBySocialTypeAndEmail(SocialType socialType, String email);

    Optional<Member> findById(Long id);

    Optional<Member> findByEmail(String email);

}
