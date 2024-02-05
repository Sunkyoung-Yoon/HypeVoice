package hypevoice.hypevoiceback.board.domain;

import hypevoice.hypevoiceback.board.query.BoardListQueryRepository;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BoardRepository extends JpaRepository<Board, Long>, BoardListQueryRepository {
    Optional<Board> findById(Long boardId);
}
