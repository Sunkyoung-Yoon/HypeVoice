package hypevoice.hypevoiceback.board.controller;


import hypevoice.hypevoiceback.auth.exception.AuthErrorCode;
import hypevoice.hypevoiceback.board.dto.BoardRequest;
import hypevoice.hypevoiceback.board.dto.BoardResponse;
import hypevoice.hypevoiceback.board.exception.BoardErrorCode;
import hypevoice.hypevoiceback.common.ControllerTest;
import hypevoice.hypevoiceback.global.exception.BaseException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.request.RequestPostProcessor;

import java.nio.charset.StandardCharsets;
import java.time.LocalDate;

import static hypevoice.hypevoiceback.fixture.BoardFixture.BOARD_0;
import static hypevoice.hypevoiceback.fixture.TokenFixture.ACCESS_TOKEN;
import static hypevoice.hypevoiceback.fixture.TokenFixture.BEARER_TOKEN;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@DisplayName("Board [Controller Layer] -> BoardApiController 테스트")
public class BoardApiControllerTest extends ControllerTest {
    @Nested
    @DisplayName("게시글 등록 API [POST /api/boards]")
    class createBoard {
        private static final String BASE_URL = "/api/boards";

        @Test
        @DisplayName("Authorization Header에 AccessToken이 없으면 게시글 등록에 실패한다")
        void withoutAccessToken() throws Exception {
            // when
            final BoardRequest request = createBoardRequest();
            MockMultipartFile file = new MockMultipartFile("file", null,
                    "multipart/form-data", new byte[]{});
            MockMultipartFile mockRequest = new MockMultipartFile("request", null,
                    "application/json", objectMapper.writeValueAsString(request).getBytes(StandardCharsets.UTF_8));


            MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                    .multipart(BASE_URL)
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
        @DisplayName("게시글 등록에 성공한다")
        void success() throws Exception {
            // given
            doReturn(1L)
                    .when(boardService)
                    .create(anyLong(), any(), any(), any(), any());

            // when
            final BoardRequest request = createBoardRequest();
            MockMultipartFile file = new MockMultipartFile("file", null,
                    "multipart/form-data", new byte[]{});
            MockMultipartFile mockRequest = new MockMultipartFile("request", null,
                    "application/json", objectMapper.writeValueAsString(request).getBytes(StandardCharsets.UTF_8));

            MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                    .multipart(BASE_URL)
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
    @DisplayName("게시글 수정 API [PATCH /api/boards/{boardId}]")
    class updateBoard {
        private static final String BASE_URL = "/api/boards/{boardId}";
        private static final Long BOARD_ID = 1L;

        @Test
        @DisplayName("Authorization Header에 AccessToken이 없으면 게시글 수정에 실패한다")
        void withoutAccessToken() throws Exception {
            // when
            final BoardRequest request = createBoardRequest();
            MockMultipartFile file = new MockMultipartFile("file", null,
                    "multipart/form-data", new byte[]{});
            MockMultipartFile mockRequest = new MockMultipartFile("request", null,
                    "application/json", objectMapper.writeValueAsString(request).getBytes(StandardCharsets.UTF_8));

            MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                    .multipart(BASE_URL, BOARD_ID)
                    .file(file)
                    .file(mockRequest)
                    .accept(APPLICATION_JSON)
                    .with(request1 -> {
                        request1.setMethod("PATCH");
                        return request1;
                    });

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
        @DisplayName("다른 사람의 게시글은 수정할 수 없다")
        void throwExceptionByUserIsNotBoardWriter() throws Exception {
            // given
            doThrow(BaseException.type(BoardErrorCode.USER_IS_NOT_BOARD_WRITER))
                    .when(boardService)
                    .update(anyLong(), anyLong(), any(), any(), any());

            // when
            final BoardRequest request = createBoardRequest();
            MockMultipartFile file = new MockMultipartFile("file", null,
                    "multipart/form-data", new byte[]{});
            MockMultipartFile mockRequest = new MockMultipartFile("request", null,
                    "application/json", objectMapper.writeValueAsString(request).getBytes(StandardCharsets.UTF_8));

            MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                    .multipart(BASE_URL, BOARD_ID)
                    .file(file)
                    .file(mockRequest)
                    .accept(APPLICATION_JSON)
                    .with(request1 -> {
                        request1.setMethod("PATCH");
                        return request1;
                    })
                    .header(AUTHORIZATION, BEARER_TOKEN + ACCESS_TOKEN);

            // then
            final BoardErrorCode expectedError = BoardErrorCode.USER_IS_NOT_BOARD_WRITER;
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
        @DisplayName("게시글 수정에 성공한다")
        void success() throws Exception {
            // given
            doNothing()
                    .when(boardService)
                    .update(anyLong(), anyLong(), any(), any(), any());

            // when
            final BoardRequest request = createBoardRequest();
            MockMultipartFile file = new MockMultipartFile("file", null,
                    "multipart/form-data", new byte[]{});
            MockMultipartFile mockRequest = new MockMultipartFile("request", null,
                    "application/json", objectMapper.writeValueAsString(request).getBytes(StandardCharsets.UTF_8));

            MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                    .multipart(BASE_URL, BOARD_ID)
                    .file(file)
                    .file(mockRequest)
                    .accept(APPLICATION_JSON)
                    .with(request1 -> {
                        request1.setMethod("PATCH");
                        return request1;
                    })
                    .header(AUTHORIZATION, BEARER_TOKEN + ACCESS_TOKEN);

            // then
            mockMvc.perform(requestBuilder)
                    .andExpect(status().isOk());
        }
    }

    @Nested
    @DisplayName("게시글 삭제 API [DELETE /api/boards/{boardId}]")
    class deleteBoard {
        private static final String BASE_URL = "/api/boards/{boardId}";
        private static final Long BOARD_ID = 1L;

        @Test
        @DisplayName("Authorization Header에 AccessToken이 없으면 게시글 삭제에 실패한다")
        void withoutAccessToken() throws Exception {
            // when
            final BoardRequest request = createBoardRequest();
            MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                    .delete(BASE_URL, BOARD_ID)
                    .contentType(APPLICATION_JSON)
                    .content(convertObjectToJson(request));

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
        @DisplayName("다른 사람의 게시글은 삭제할 수 없다")
        void throwExceptionByUserIsNotBoardWriter() throws Exception {
            // given
            doThrow(BaseException.type(BoardErrorCode.USER_IS_NOT_BOARD_WRITER))
                    .when(boardService)
                    .delete(anyLong(), anyLong());

            // when
            final BoardRequest request = createBoardRequest();
            MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                    .delete(BASE_URL, BOARD_ID)
                    .header(AUTHORIZATION, BEARER_TOKEN + ACCESS_TOKEN)
                    .contentType(APPLICATION_JSON)
                    .content(convertObjectToJson(request));

            // then
            final BoardErrorCode expectedError = BoardErrorCode.USER_IS_NOT_BOARD_WRITER;
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
        @DisplayName("게시글 삭제에 성공한다")
        void success() throws Exception {
            // given
            doNothing()
                    .when(boardService)
                    .delete(anyLong(), anyLong());

            // when
            final BoardRequest request = createBoardRequest();
            MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                    .delete(BASE_URL, BOARD_ID)
                    .header(AUTHORIZATION, BEARER_TOKEN + ACCESS_TOKEN)
                    .contentType(APPLICATION_JSON)
                    .content(convertObjectToJson(request));

            // then
            mockMvc.perform(requestBuilder)
                    .andExpect(status().isOk());
        }
    }

    @Nested
    @DisplayName("게시글 상세조회 API [GET /api/boards/{boardId}]")
    class getDetailBoard {
        private static final String BASE_URL = "/api/boards/{boardId}";
        private static final Long BOARD_ID = 2L;

        @Test
        @DisplayName("게시글 상세조회에 성공한다")
        void success() throws Exception {
            // given
            doReturn(readBoardResponse())
                    .when(boardService)
                    .read(anyLong());

            // when
            MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                    .get(BASE_URL, BOARD_ID);

            // then
            mockMvc.perform(requestBuilder)
                    .andExpectAll(status().isOk());
        }
    }

    private BoardRequest createBoardRequest() {
        return new BoardRequest(BOARD_0.getTitle(), BOARD_0.getContent(), "feedback");
    }

    private BoardResponse readBoardResponse() {
        return new BoardResponse(2L, BOARD_0.getTitle(), BOARD_0.getContent(), 0, null,"feedback",
                LocalDate.of(2024, 1, 30).atTime(1, 1), 1L, "voice123");
    }
}