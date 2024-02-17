package hypevoice.hypevoiceback.comment.domain;

import hypevoice.hypevoiceback.board.domain.Board;
import hypevoice.hypevoiceback.member.domain.Member;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static hypevoice.hypevoiceback.fixture.BoardFixture.BOARD_0;
import static hypevoice.hypevoiceback.fixture.CommentFixture.COMMENT_0;
import static hypevoice.hypevoiceback.fixture.MemberFixture.SUNKYOUNG;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayName("Comment 도메인 테스트")
public class CommentTest {
    private Member writer;
    private Board board;

    private Comment comment;

    @BeforeEach
    void setUp() {
        writer = SUNKYOUNG.toMember();
        board = BOARD_0.toBoard(writer);
        comment = COMMENT_0.toComment(writer, board);
    }

    @Test
    @DisplayName("Comment 생성에 성공한다")
    void createComment() {
        // then
        assertAll(
                () -> assertThat(comment.getWriter()).isEqualTo(writer),
                () -> assertThat(comment.getBoard()).isEqualTo(board),
                () -> assertThat(comment.getContent()).isEqualTo(BOARD_0.getContent()),
                () -> assertThat(comment.getVoiceCommentUrl()).isNull()
        );
    }
}
