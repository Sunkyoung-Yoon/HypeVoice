package hypevoice.hypevoiceback.board.domain;


import hypevoice.hypevoiceback.comment.domain.Comment;
import hypevoice.hypevoiceback.member.domain.Member;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static hypevoice.hypevoiceback.fixture.BoardFixture.BOARD_0;
import static hypevoice.hypevoiceback.fixture.MemberFixture.GABIN;
import static hypevoice.hypevoiceback.fixture.MemberFixture.SUNKYOUNG;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayName("Board 도메인 테스트")
public class BoardTest {
    private Member writer1;
    private Member writer2;
    private Board board;

    @BeforeEach
    void setUp() {
        writer1 = SUNKYOUNG.toMember();
        writer2 = GABIN.toMember();
        board = BOARD_0.toBoard(writer1);
    }

    @Test
    @DisplayName("Board 생성에 성공한다")
    void createBoard() {
        // then
        assertAll(
                () -> assertThat(board.getWriter()).isEqualTo(writer1),
                () -> assertThat(board.getTitle()).isEqualTo(BOARD_0.getTitle()),
                () -> assertThat(board.getContent()).isEqualTo(BOARD_0.getContent()),
                () -> assertThat(board.getView()).isEqualTo(0),
                () -> assertThat(board.getRecordUrl()).isNull(),
                () -> assertThat(board.getCategory()).isEqualTo(Category.FEEDBACK)
        );
    }

    @Test
    @DisplayName("Board 제목과 내용을 변경한다")
    void updateTitleAndContent() {
        // when
        board.updateBoard("제목변경", "내용변경", null);

        // then
        assertAll(
                () -> assertThat(board.getTitle()).isEqualTo("제목변경"),
                () -> assertThat(board.getContent()).isEqualTo("내용변경"),
                () -> assertThat(board.getRecordUrl()).isEqualTo(null)
        );
    }

    @Test
    @DisplayName("Board에 Comment를 추가한다")
    void addComment() {
        for(int i=1; i<=2; i++){
            board.addComment(writer1, "댓글" + i, "음성댓글"+i);
        }
        for(int i=3; i<=5; i++){
            board.addComment(writer2, "댓글" + i, "음성댓글"+i);
        }

        assertAll(
                () -> assertThat(board.getCommentList()).hasSize(5),
                () -> assertThat(board.getCommentList())
                        .map(Comment::getContent)
                        .containsExactlyInAnyOrder("댓글1", "댓글2", "댓글3", "댓글4", "댓글5"),
                () -> assertThat(board.getCommentList())
                        .map(Comment::getVoiceCommentUrl)
                        .containsExactlyInAnyOrder("음성댓글1", "음성댓글2", "음성댓글3", "음성댓글4", "음성댓글5"),
                () -> assertThat(board.getCommentList())
                        .map(Comment::getWriter)
                        .containsExactlyInAnyOrder(writer1, writer1, writer2, writer2, writer2)
        );
    }
}