package hypevoice.hypevoiceback.comment.controller;

import hypevoice.hypevoiceback.comment.dto.CommentList;
import hypevoice.hypevoiceback.comment.dto.CustomCommentListResponse;
import hypevoice.hypevoiceback.common.ControllerTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static hypevoice.hypevoiceback.fixture.CommentFixture.*;
import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("Comment [Controller Layer] -> CommentListApiController 테스트")
public class CommentListApiControllerTest extends ControllerTest {

    @Nested
    @DisplayName("댓글 목록 조회 API [GET /api/comments/{boardId}")
    class getBoardList {
        private static final String BASE_URL = "/api/comments/{boardId}";
        private static final int PAGE = 0;
        private static final Long BOARD_ID = 1L;

        @Test
        @DisplayName("댓글 목록 조회에 성공한다")
        void success() throws Exception{
            // given
            doReturn(getCustomCommentListResponse())
                    .when(commentListService)
                    .getCommentList(BOARD_ID, PAGE);

            // when
            MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                    .get(BASE_URL, BOARD_ID)
                    .param("page", String.valueOf(PAGE));

            // then
            mockMvc.perform(requestBuilder)
                    .andExpect(status().isOk());
        }
    }


    private List<CommentList> createCommentListResponses() {
        List<CommentList> commentLists = new ArrayList<>();
        commentLists.add(new CommentList(1L, COMMENT_1.getContent(), null, LocalDateTime.now(), 1L, "voice111"));
        commentLists.add(new CommentList(2L, COMMENT_2.getContent(), null, LocalDateTime.now(), 1L, "voice111"));
        commentLists.add(new CommentList(3L, COMMENT_3.getContent(), null, LocalDateTime.now(), 2L, "voice222"));
        commentLists.add(new CommentList(4L, COMMENT_4.getContent(), null, LocalDateTime.now(), 2L, "voice222"));
        return commentLists;
    }

    private CustomCommentListResponse.CustomPageable createCustomPageable() {
        return new CustomCommentListResponse.CustomPageable(1, 4, false, 4);
    }

    private CustomCommentListResponse getCustomCommentListResponse() {
        return new CustomCommentListResponse<>(createCustomPageable(), createCommentListResponses());
    }
}
