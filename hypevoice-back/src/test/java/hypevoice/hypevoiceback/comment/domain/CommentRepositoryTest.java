package hypevoice.hypevoiceback.comment.domain;

import hypevoice.hypevoiceback.board.domain.Board;
import hypevoice.hypevoiceback.board.domain.BoardRepository;
import hypevoice.hypevoiceback.common.RepositoryTest;
import hypevoice.hypevoiceback.member.domain.Member;
import hypevoice.hypevoiceback.member.domain.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static hypevoice.hypevoiceback.fixture.BoardFixture.BOARD_0;
import static hypevoice.hypevoiceback.fixture.CommentFixture.COMMENT_0;
import static hypevoice.hypevoiceback.fixture.MemberFixture.SUNKYOUNG;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayName("Comment [Repository Test] -> CommentRepository 테스트")
public class CommentRepositoryTest extends RepositoryTest {
    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private BoardRepository boardRepository;

    @Autowired
    private CommentRepository commentRepository;

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
    @DisplayName("ID(PK)로 댓글을 확인한다")
    void findById() {
        // when
        Comment findComment = commentRepository.findById(comment.getId()).orElseThrow();

        // then
        assertAll(
                () -> assertThat(findComment.getWriter()).isEqualTo(writer),
                () -> assertThat(findComment.getContent()).isEqualTo(COMMENT_0.getContent()),
                () -> assertThat(findComment.getVoiceCommentUrl()).isEqualTo(COMMENT_0.getVoiceCommentUrl())
        );
    }
}

