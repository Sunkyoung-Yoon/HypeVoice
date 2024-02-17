package hypevoice.hypevoiceback.comment.service;

import hypevoice.hypevoiceback.board.domain.Board;
import hypevoice.hypevoiceback.comment.domain.Comment;
import hypevoice.hypevoiceback.comment.exception.CommentErrorCode;
import hypevoice.hypevoiceback.common.ServiceTest;
import hypevoice.hypevoiceback.global.exception.BaseException;
import hypevoice.hypevoiceback.member.domain.Member;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static hypevoice.hypevoiceback.fixture.BoardFixture.BOARD_0;
import static hypevoice.hypevoiceback.fixture.CommentFixture.COMMENT_0;
import static hypevoice.hypevoiceback.fixture.MemberFixture.SUNKYOUNG;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("Comment [Service Layer] -> CommentFindService 테스트")
public class CommentFindServiceTest extends ServiceTest {
    @Autowired
    private CommentFindService commentFindService;

    private Member writer;
    private Board board;
    private Comment comment;

    @BeforeEach
    void setUp() {
        writer = memberRepository.save(SUNKYOUNG.toMember());
        board = boardRepository.save(BOARD_0.toBoard(writer));
        comment = commentRepository.save(COMMENT_0.toComment(writer, board));
    }

    @Test
    @DisplayName("ID(PK)로 댓글을 조회한다")
    void findById() {
        // when
        Comment findComment = commentFindService.findById(comment.getId());

        // then
        assertThat(findComment).isEqualTo(comment);
        assertThatThrownBy(() -> commentFindService.findById(comment.getId() + 100L))
                .isInstanceOf(BaseException.class)
                .hasMessage(CommentErrorCode.COMMENT_NOT_FOUND.getMessage());
    }
}
