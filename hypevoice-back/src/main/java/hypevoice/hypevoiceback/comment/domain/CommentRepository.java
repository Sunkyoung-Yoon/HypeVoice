package hypevoice.hypevoiceback.comment.domain;

import hypevoice.hypevoiceback.comment.query.CommentListQueryRepository;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Long>, CommentListQueryRepository {
    Optional<Comment> findById(Long commentId);
}
