package hypevoice.hypevoiceback.board.controller;

import hypevoice.hypevoiceback.board.dto.BoardList;
import hypevoice.hypevoiceback.board.dto.CustomBoardListResponse;
import hypevoice.hypevoiceback.board.exception.BoardErrorCode;
import hypevoice.hypevoiceback.common.ControllerTest;
import hypevoice.hypevoiceback.global.exception.BaseException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static hypevoice.hypevoiceback.fixture.BoardFixture.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@DisplayName("Board [Controller Layer] -> BoardListApiController 테스트")
class BoardListApiControllerTest extends ControllerTest {
    private static final String INVALID_SORT = "가나다순";
    private static final String INVALID_SEARCH = "가나다";
    private static final String SORT_BY_TIME = "최신순";
    private static final String SEARCH_TYPE = "제목";
    private static final String SEARCH_WORD = "피드백";

    @Nested
    @DisplayName("게시글 목록 조회 API [GET /api/boards]")
    class getBoardList {
        private static final String BASE_URL = "/api/boards";
        private static final int PAGE = 0;

        @Test
        @DisplayName("유효하지 않은 정렬 방식이라면 게시글 목록 조회에 실패한다")
        void throwExceptionByNotFoundSortCondition() throws Exception {
            // given
            doThrow(BaseException.type(BoardErrorCode.SORT_CONDITION_NOT_FOUND))
                    .when(boardListService)
                    .getBoardList(eq(PAGE), eq(INVALID_SORT), eq(SEARCH_TYPE), eq(SEARCH_WORD));

            // when
            MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                    .get(BASE_URL)
                    .param("page", String.valueOf(PAGE))
                    .param("sort", INVALID_SORT)
                    .param("search", SEARCH_TYPE)
                    .param("word", SEARCH_WORD);

            // then
            final BoardErrorCode expectedError = BoardErrorCode.SORT_CONDITION_NOT_FOUND;
            mockMvc.perform(requestBuilder)
                    .andExpectAll(
                            status().isNotFound(),
                            jsonPath("$.status").exists(),
                            jsonPath("$.status").value(expectedError.getStatus().value()),
                            jsonPath("$.errorCode").exists(),
                            jsonPath("$.errorCode").value(expectedError.getErrorCode()),
                            jsonPath("$.message").exists(),
                            jsonPath("$.message").value(expectedError.getMessage())
                    );
        }

        @Test
        @DisplayName("유효하지 않은 검색 조건이라면 게시글 목록 검색에 실패한다")
        void throwExceptionByNotFoundSearchType() throws Exception {
            // given
            doThrow(BaseException.type(BoardErrorCode.SEARCH_TYPE_NOT_FOUND))
                    .when(boardListService)
                    .getBoardList(eq(PAGE), eq(SORT_BY_TIME), eq(INVALID_SEARCH), eq(SEARCH_WORD));

            // when
            MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                    .get(BASE_URL)
                    .param("page", String.valueOf(PAGE))
                    .param("sort", SORT_BY_TIME)
                    .param("search", INVALID_SEARCH)
                    .param("word", SEARCH_WORD);

            // then
            final BoardErrorCode expectedError = BoardErrorCode.SEARCH_TYPE_NOT_FOUND;
            mockMvc.perform(requestBuilder)
                    .andExpectAll(
                            status().isNotFound(),
                            jsonPath("$.status").exists(),
                            jsonPath("$.status").value(expectedError.getStatus().value()),
                            jsonPath("$.errorCode").exists(),
                            jsonPath("$.errorCode").value(expectedError.getErrorCode()),
                            jsonPath("$.message").exists(),
                            jsonPath("$.message").value(expectedError.getMessage())
                    );
        }

        @Test
        @DisplayName("정렬 기준과 검색에 따른 게시글 목록 조회에 성공한다")
        void success() throws Exception{
            // given
            doReturn(getCustomBoardListResponse())
                    .when(boardListService)
                    .getBoardList(PAGE, SORT_BY_TIME, SEARCH_TYPE, SEARCH_WORD);

            // when
            MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                    .get(BASE_URL)
                    .param("page", String.valueOf(PAGE))
                    .param("sort", SORT_BY_TIME)
                    .param("search", SEARCH_TYPE)
                    .param("word", SEARCH_WORD);

            // then
            mockMvc.perform(requestBuilder)
                    .andExpect(status().isOk());
        }
    }


    private List<BoardList> createBoardListResponses() {
        List<BoardList> boardLists = new ArrayList<>();
        boardLists.add(new BoardList(1L, BOARD_0.getTitle(), 1, BOARD_0.getCategory(), LocalDateTime.now(), 1L, "voice111"));
        boardLists.add(new BoardList(2L, BOARD_1.getTitle(), 2, BOARD_1.getCategory(), LocalDateTime.now(), 1L, "voice111"));
        boardLists.add(new BoardList(3L, BOARD_2.getTitle(), 3, BOARD_1.getCategory(), LocalDateTime.now(), 2L, "voice111"));
        boardLists.add(new BoardList(4L, BOARD_3.getTitle(), 4, BOARD_1.getCategory(), LocalDateTime.now(), 2L, "voice111"));
        return boardLists;
    }

    private CustomBoardListResponse.CustomPageable createCustomPageable() {
        return new CustomBoardListResponse.CustomPageable(1, 4, false, 4);
    }

    private CustomBoardListResponse getCustomBoardListResponse() {
        return new CustomBoardListResponse<>(createCustomPageable(), createBoardListResponses());
    }
}
