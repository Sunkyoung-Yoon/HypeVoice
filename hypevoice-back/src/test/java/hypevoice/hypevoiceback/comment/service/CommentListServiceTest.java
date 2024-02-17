package hypevoice.hypevoiceback.comment.service;

import hypevoice.hypevoiceback.board.domain.Board;
import hypevoice.hypevoiceback.comment.domain.Comment;
import hypevoice.hypevoiceback.comment.dto.CommentList;
import hypevoice.hypevoiceback.comment.dto.CustomCommentListResponse;
import hypevoice.hypevoiceback.common.ServiceTest;
import hypevoice.hypevoiceback.member.domain.Member;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static hypevoice.hypevoiceback.fixture.BoardFixture.*;
import static hypevoice.hypevoiceback.fixture.CommentFixture.*;
import static hypevoice.hypevoiceback.fixture.MemberFixture.SUNKYOUNG;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

public class CommentListServiceTest extends ServiceTest {
    @Autowired
    private CommentListService commentListService;

    private Member writer;
    private Board board;
    private Comment[] commentList = new Comment[21];

    private static final int PAGE = 0;
    private static final int PAGE_SIZE = 20;

    @BeforeEach
    void setUp() {
        writer = memberRepository.save(SUNKYOUNG.toMember());
        board = boardRepository.save(BOARD_0.toBoard(writer));

        commentList[0] = commentRepository.save(COMMENT_0.toComment(writer, board));
        commentList[1] = commentRepository.save(COMMENT_1.toComment(writer, board));
        commentList[2] = commentRepository.save(COMMENT_2.toComment(writer, board));
        commentList[3] = commentRepository.save(COMMENT_3.toComment(writer, board));
        commentList[4] = commentRepository.save(COMMENT_4.toComment(writer, board));
        commentList[5] = commentRepository.save(COMMENT_5.toComment(writer, board));
        commentList[6] = commentRepository.save(COMMENT_6.toComment(writer, board));
        commentList[7] = commentRepository.save(COMMENT_7.toComment(writer, board));
        commentList[8] = commentRepository.save(COMMENT_8.toComment(writer, board));
        commentList[9] = commentRepository.save(COMMENT_9.toComment(writer, board));
        commentList[10] = commentRepository.save(COMMENT_10.toComment(writer, board));
        commentList[11] = commentRepository.save(COMMENT_11.toComment(writer, board));
        commentList[12] = commentRepository.save(COMMENT_12.toComment(writer, board));
        commentList[13] = commentRepository.save(COMMENT_13.toComment(writer, board));
        commentList[14] = commentRepository.save(COMMENT_14.toComment(writer, board));
        commentList[15] = commentRepository.save(COMMENT_15.toComment(writer, board));
        commentList[16] = commentRepository.save(COMMENT_16.toComment(writer, board));
        commentList[17] = commentRepository.save(COMMENT_17.toComment(writer, board));
        commentList[18] = commentRepository.save(COMMENT_18.toComment(writer, board));
        commentList[19] = commentRepository.save(COMMENT_19.toComment(writer, board));
        commentList[20] = commentRepository.save(COMMENT_20.toComment(writer, board));
    }

    @Nested
    @DisplayName("전체 댓글 조회")
    class allCommentList {

        @Test
        @DisplayName("댓글 목록 조회에 성공한다")
        void getCommentListOrderByTime() {
            // when
            CustomCommentListResponse<CommentList> commentListResponse = commentListService.getCommentList(board.getId(), PAGE);

            // then
            assertThat(commentListResponse.commentList().size()).isLessThanOrEqualTo(PAGE_SIZE);
            assertThat(commentListResponse.commentList().size()).isEqualTo(PAGE_SIZE);

            assertAll(
                    () -> assertThat(commentListResponse.commentList().get(0).commentId()).isEqualTo(commentList[20].getId()),
                    () -> assertThat(commentListResponse.commentList().get(0).content()).isEqualTo(commentList[20].getContent()),
                    () -> assertThat(commentListResponse.commentList().get(0).voiceCommentUrl()).isEqualTo(commentList[20].getVoiceCommentUrl()),
                    () -> assertThat(commentListResponse.commentList().get(0).writerId()).isEqualTo(commentList[20].getWriter().getId()),
                    () -> assertThat(commentListResponse.commentList().get(0).writerNickname()).isEqualTo(commentList[20].getWriter().getNickname())
            );
        }
    }
}
