package hypevoice.hypevoiceback.board.domain;


import hypevoice.hypevoiceback.member.domain.Member;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static hypevoice.hypevoiceback.fixture.BoardFixture.BOARD_0;
import static hypevoice.hypevoiceback.fixture.MemberFixture.SUNKYOUNG;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayName("Board 도메인 테스트")
public class BoardTest {
    private Member writer;
    private Board board;

    @BeforeEach
    void setUp() {
        writer = SUNKYOUNG.toMember();
        board = BOARD_0.toBoard(writer);
    }

    @Test
    @DisplayName("Board 생성에 성공한다")
    void createBoard() {
        // then
        assertAll(
                () -> assertThat(board.getWriter()).isEqualTo(writer),
                () -> assertThat(board.getTitle()).isEqualTo(BOARD_0.getTitle()),
                () -> assertThat(board.getContent()).isEqualTo(BOARD_0.getContent()),
                () -> assertThat(board.getView()).isEqualTo(0),
                () -> assertThat(board.getCategory()).isEqualTo(Category.FEEDBACK)
        );
    }

    @Test
    @DisplayName("Board 제목과 내용을 변경한다")
    void updateTitleAndContent() {
        // when
        board.updateBoard("제목변경", "내용변경");

        // then
        assertAll(
                () -> assertThat(board.getTitle()).isEqualTo("제목변경"),
                () -> assertThat(board.getContent()).isEqualTo("내용변경")
        );
    }
}