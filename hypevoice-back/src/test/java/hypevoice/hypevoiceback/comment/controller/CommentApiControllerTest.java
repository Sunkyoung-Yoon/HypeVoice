package hypevoice.hypevoiceback.comment.controller;

import hypevoice.hypevoiceback.auth.exception.AuthErrorCode;
import hypevoice.hypevoiceback.comment.dto.CommentRequest;
import hypevoice.hypevoiceback.comment.exception.CommentErrorCode;
import hypevoice.hypevoiceback.common.ControllerTest;
import hypevoice.hypevoiceback.global.exception.BaseException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.nio.charset.StandardCharsets;

import static hypevoice.hypevoiceback.fixture.CommentFixture.COMMENT_0;
import static hypevoice.hypevoiceback.fixture.TokenFixture.ACCESS_TOKEN;
import static hypevoice.hypevoiceback.fixture.TokenFixture.BEARER_TOKEN;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("Comment [Controller Layer] -> CommentApiController 테스트")
public class CommentApiControllerTest extends ControllerTest {
    @Nested
    @DisplayName("댓글 등록 API [POST /api/comments/{boardId}]")
    class createBoard {
        private static final String BASE_URL = "/api/comments/{boardId}";

        private static final Long BOARD_ID = 1L;

        @Test
        @DisplayName("Authorization Header에 AccessToken이 없으면 댓글 등록에 실패한다")
        void withoutAccessToken() throws Exception {
            // when
            final CommentRequest request = createCommentRequest();
            MockMultipartFile file = new MockMultipartFile("file", null,
                    "multipart/form-data", new byte[]{});
            MockMultipartFile mockRequest = new MockMultipartFile("request", null,
                    "application/json", convertObjectToJson(request).getBytes(StandardCharsets.UTF_8));

            MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                    .multipart(BASE_URL, BOARD_ID)
                    .file(file)
                    .file(mockRequest)
                    .accept(APPLICATION_JSON);

            // then
            final AuthErrorCode expectedError = AuthErrorCode.INVALID_PERMISSION;
            mockMvc.perform(requestBuilder)
                    .andExpectAll(
                            status().isForbidden(),
                            jsonPath("$.status").exists(),
                            jsonPath("$.status").value(expectedError.getStatus().value()),
                            jsonPath("$.errorCode").exists(),
                            jsonPath("$.errorCode").value(expectedError.getErrorCode()),
                            jsonPath("$.message").exists(),
                            jsonPath("$.message").value(expectedError.getMessage())
                    );
        }

        @Test
        @DisplayName("댓글 등록에 성공한다")
        void success() throws Exception {
            // given
            doNothing()
                    .when(commentService)
                    .create(anyLong(), anyLong(), any(), any());

            // when
            final CommentRequest request = createCommentRequest();
            MockMultipartFile file = new MockMultipartFile("file", null,
                    "multipart/form-data", new byte[]{});
            MockMultipartFile mockRequest = new MockMultipartFile("request", null,
                    "application/json", convertObjectToJson(request).getBytes(StandardCharsets.UTF_8));

            MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                    .multipart(BASE_URL, BOARD_ID)
                    .file(file)
                    .file(mockRequest)
                    .accept(APPLICATION_JSON)
                    .header(AUTHORIZATION, BEARER_TOKEN + ACCESS_TOKEN);

            // then
            mockMvc.perform(requestBuilder)
                    .andExpect(status().isOk());
        }
    }


    @Nested
    @DisplayName(" 댓글 삭제 API [DELETE /api/comments/{commentId}]")
    class deleteBoard {
        private static final String BASE_URL = "/api/comments/{commentId}";
        private static final Long COMMENT_ID = 1L;

        @Test
        @DisplayName("Authorization Header에 AccessToken이 없으면 댓글 삭제에 실패한다")
        void withoutAccessToken() throws Exception {
            // when
            MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                    .delete(BASE_URL, COMMENT_ID);

            // then
            final AuthErrorCode expectedError = AuthErrorCode.INVALID_PERMISSION;
            mockMvc.perform(requestBuilder)
                    .andExpectAll(
                            status().isForbidden(),
                            jsonPath("$.status").exists(),
                            jsonPath("$.status").value(expectedError.getStatus().value()),
                            jsonPath("$.errorCode").exists(),
                            jsonPath("$.errorCode").value(expectedError.getErrorCode()),
                            jsonPath("$.message").exists(),
                            jsonPath("$.message").value(expectedError.getMessage())
                    );
        }

        @Test
        @DisplayName("다른 사람의 댓글은 삭제할 수 없다")
        void throwExceptionByUserIsNotCommentWriter() throws Exception {
            // given
            doThrow(BaseException.type(CommentErrorCode.USER_IS_NOT_COMMENT_WRITER))
                    .when(commentService)
                    .delete(anyLong(), anyLong());

            // when
            MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                    .delete(BASE_URL, COMMENT_ID)
                    .header(AUTHORIZATION, BEARER_TOKEN + ACCESS_TOKEN);

            // then
            final CommentErrorCode expectedError = CommentErrorCode.USER_IS_NOT_COMMENT_WRITER;
            mockMvc.perform(requestBuilder)
                    .andExpectAll(
                            status().isBadRequest(),
                            jsonPath("$.status").exists(),
                            jsonPath("$.status").value(expectedError.getStatus().value()),
                            jsonPath("$.errorCode").exists(),
                            jsonPath("$.errorCode").value(expectedError.getErrorCode()),
                            jsonPath("$.message").exists(),
                            jsonPath("$.message").value(expectedError.getMessage())
                    );
        }

        @Test
        @DisplayName("댓글 삭제에 성공한다")
        void success() throws Exception {
            // given
            doNothing()
                    .when(commentService)
                    .delete(anyLong(), anyLong());

            // when
            MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                    .delete(BASE_URL, COMMENT_ID)
                    .header(AUTHORIZATION, BEARER_TOKEN + ACCESS_TOKEN);

            // then
            mockMvc.perform(requestBuilder)
                    .andExpect(status().isOk());
        }
    }

    private CommentRequest createCommentRequest() {
        return new CommentRequest(COMMENT_0.getContent());
    }
}