package hypevoice.hypevoiceback.comment.service;

import hypevoice.hypevoiceback.board.domain.Board;
import hypevoice.hypevoiceback.comment.domain.Comment;
import hypevoice.hypevoiceback.comment.exception.CommentErrorCode;
import hypevoice.hypevoiceback.common.ServiceTest;
import hypevoice.hypevoiceback.global.exception.BaseException;
import hypevoice.hypevoiceback.member.domain.Member;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static hypevoice.hypevoiceback.fixture.BoardFixture.BOARD_0;
import static hypevoice.hypevoiceback.fixture.CommentFixture.COMMENT_0;
import static hypevoice.hypevoiceback.fixture.MemberFixture.GABIN;
import static hypevoice.hypevoiceback.fixture.MemberFixture.SUNKYOUNG;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayName("Comment [Service Layer] -> CommentService 테스트")
public class CommentServiceTest extends ServiceTest {
    @Autowired
    private CommentService commentService;

    @Autowired
    private CommentFindService commentFindService;

    private Member writer;
    private Member not_writer;
    private Board board;

    private Comment comment;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    @BeforeEach
    void setup() {
        writer = memberRepository.save(SUNKYOUNG.toMember());
        not_writer = memberRepository.save(GABIN.toMember());
        board = boardRepository.save(BOARD_0.toBoard(writer));
        comment = commentRepository.save(COMMENT_0.toComment(writer, board));
    }

    @Test
    @DisplayName("댓글 등록에 성공한다")
    void success() {
        // when
        commentService.create(writer.getId(), board.getId(), "내용", "음성댓글");

        // then
        Comment findComment = commentRepository.findById(2L).orElseThrow();
        assertAll(
                () -> assertThat(findComment.getWriter().getId()).isEqualTo(writer.getId()),
                () -> assertThat(findComment.getBoard().getId()).isEqualTo(board.getId()),
                () -> assertThat(findComment.getContent()).isEqualTo("내용"),
                () -> assertThat(findComment.getVoiceCommentUrl()).isEqualTo("음성댓글"),
                () -> assertThat(findComment.getCreatedDate().format(formatter)).isEqualTo(LocalDateTime.now().format(formatter)),
                () -> assertThat(findComment.getModifiedDate().format(formatter)).isEqualTo(LocalDateTime.now().format(formatter))
        );
    }

    @Nested
    @DisplayName("댓글 삭제")
    class delete {
        @Test
        @DisplayName("다른 사람의 댓글은 삭제할 수 없다")
        void throwExceptionByUserNotCommentWriter() {
            // when - then
            assertThatThrownBy(() -> commentService.delete(not_writer.getId(),board.getId()))
                    .isInstanceOf(BaseException.class)
                    .hasMessage(CommentErrorCode.USER_IS_NOT_COMMENT_WRITER.getMessage());
        }

        @Test
        @DisplayName("댓글 삭제에 성공한다")
        void success() {
            // given
            commentService.delete(writer.getId(), board.getId());

            // when - then
            assertThatThrownBy(() -> commentFindService.findById(comment.getId()))
                    .isInstanceOf(BaseException.class)
                    .hasMessage(CommentErrorCode.COMMENT_NOT_FOUND.getMessage());
        }
    }
}
