package hypevoice.hypevoiceback.board.service;

import hypevoice.hypevoiceback.board.domain.Board;
import hypevoice.hypevoiceback.board.exception.BoardErrorCode;
import hypevoice.hypevoiceback.common.ServiceTest;
import hypevoice.hypevoiceback.global.exception.BaseException;
import hypevoice.hypevoiceback.member.domain.Member;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static hypevoice.hypevoiceback.fixture.BoardFixture.BOARD_0;
import static hypevoice.hypevoiceback.fixture.MemberFixture.SUNKYOUNG;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("Board [Service Layer] -> BoardFindService 테스트")
public class BoardFindServiceTest extends ServiceTest {
    @Autowired
    private BoardFindService boardFindService;

    private Member writer;
    private Board board;

    @BeforeEach
    void setUp() {
        writer = memberRepository.save(SUNKYOUNG.toMember());
        board = boardRepository.save(BOARD_0.toBoard(writer));
    }

    @Test
    @DisplayName("ID(PK)로 게시글을 조회한다")
    void findById() {
        // when
        Board findBoard = boardFindService.findById(board.getId());

        // then
        assertThat(findBoard).isEqualTo(board);
        assertThatThrownBy(() -> boardFindService.findById(board.getId() + 100L))
                .isInstanceOf(BaseException.class)
                .hasMessage(BoardErrorCode.BOARD_NOT_FOUND.getMessage());
    }
}
