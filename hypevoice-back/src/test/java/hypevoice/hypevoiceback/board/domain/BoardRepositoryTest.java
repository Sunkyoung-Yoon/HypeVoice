package hypevoice.hypevoiceback.board.domain;

import hypevoice.hypevoiceback.common.RepositoryTest;
import hypevoice.hypevoiceback.member.domain.Member;
import hypevoice.hypevoiceback.member.domain.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static hypevoice.hypevoiceback.fixture.BoardFixture.BOARD_0;
import static hypevoice.hypevoiceback.fixture.MemberFixture.SUNKYOUNG;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayName("Board [Repository Test] -> BoardRepository 테스트")
public class BoardRepositoryTest extends RepositoryTest {
    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private BoardRepository boardRepository;
    private Member writer;
    private Board board;

    @BeforeEach
    void setUp() {
        writer = memberRepository.save(SUNKYOUNG.toMember());
        board = boardRepository.save(BOARD_0.toBoard(writer));
    }

    @Test
    @DisplayName("ID(PK)로 게시글을 확인한다")
    void findById() {
        // when
        Board findBoard = boardRepository.findById(board.getId()).orElseThrow();

        // then
        assertAll(
                () -> assertThat(findBoard.getWriter()).isEqualTo(writer),
                () -> assertThat(findBoard.getTitle()).isEqualTo(BOARD_0.getTitle()),
                () -> assertThat(findBoard.getContent()).isEqualTo(BOARD_0.getContent()),
                () -> assertThat(findBoard.getView()).isEqualTo(0),
                () -> assertThat(findBoard.getRecordUrl()).isNull(),
                () -> assertThat(findBoard.getCategory()).isEqualTo(BOARD_0.getCategory())
        );
    }
}

