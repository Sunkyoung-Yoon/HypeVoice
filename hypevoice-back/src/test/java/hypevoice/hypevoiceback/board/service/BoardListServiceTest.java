package hypevoice.hypevoiceback.board.service;

import hypevoice.hypevoiceback.board.domain.Board;
import hypevoice.hypevoiceback.board.dto.BoardList;
import hypevoice.hypevoiceback.board.dto.CustomBoardListResponse;
import hypevoice.hypevoiceback.board.exception.BoardErrorCode;
import hypevoice.hypevoiceback.common.ServiceTest;
import hypevoice.hypevoiceback.global.exception.BaseException;
import hypevoice.hypevoiceback.member.domain.Member;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;

import static hypevoice.hypevoiceback.fixture.BoardFixture.*;
import static hypevoice.hypevoiceback.fixture.MemberFixture.SUNKYOUNG;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

public class BoardListServiceTest extends ServiceTest {
    @Autowired
    private BoardListService boardListService;

    private Member writer;
    private final Board[] boardList = new Board[15];

    private static final int PAGE = 0;
    private static final String SORT_BY = "조회순";
    private static final String INVALID_SORT = "가나다순";
    private static final String INVALID_SEARCH = "가나다";
    private static final String SEARCH_TYPE = "제목";
    private static final String SEARCH_WORD = "제목";
    private static final int PAGE_SIZE = 10;

    @BeforeEach
    void setUp() {
        //boardRepository.deleteAll();
        writer = memberRepository.save(SUNKYOUNG.toMember());

        boardList[0] = boardRepository.save(BOARD_0.toBoard(writer));
        boardList[1] = boardRepository.save(BOARD_1.toBoard(writer));
        boardList[2] = boardRepository.save(BOARD_2.toBoard(writer));
        boardList[3] = boardRepository.save(BOARD_3.toBoard(writer));
        boardList[4] = boardRepository.save(BOARD_4.toBoard(writer));
        boardList[5] = boardRepository.save(BOARD_5.toBoard(writer));
        boardList[6] = boardRepository.save(BOARD_6.toBoard(writer));
        boardList[7] = boardRepository.save(BOARD_7.toBoard(writer));
        boardList[8] = boardRepository.save(BOARD_8.toBoard(writer));
        boardList[9] = boardRepository.save(BOARD_9.toBoard(writer));
        boardList[10] = boardRepository.save(BOARD_10.toBoard(writer));
        boardList[11] = boardRepository.save(BOARD_11.toBoard(writer));
        boardList[12] = boardRepository.save(BOARD_12.toBoard(writer));
        boardList[13] = boardRepository.save(BOARD_13.toBoard(writer));
        boardList[14] = boardRepository.save(BOARD_14.toBoard(writer));
    }

    @Nested
    @DisplayName("전체 게시글 조회")
    class allBoardList {
        @Test
        @DisplayName("유효한 정렬 조건이 아니면 게시글 목록 조회에 실패한다")
        void throwNotFoundSortCondition() {
            // when - then
            assertThatThrownBy(() -> boardListService.getBoardList(PAGE, INVALID_SORT, SEARCH_TYPE, SEARCH_WORD))
                    .isInstanceOf(BaseException.class)
                    .hasMessage(BoardErrorCode.SORT_CONDITION_NOT_FOUND.getMessage());
        }

        @Test
        @DisplayName("유효한 검색 조건이 아니면 게시글 목록 검색에 실패한다")
        void throwNotFoundSearchType() {
            // when - then
            assertThatThrownBy(() -> boardListService.getBoardList(PAGE, SORT_BY, INVALID_SEARCH, SEARCH_WORD))
                    .isInstanceOf(BaseException.class)
                    .hasMessage(BoardErrorCode.SEARCH_TYPE_NOT_FOUND.getMessage());
        }

        @Test
        @DisplayName("정렬 기준과 검색에 따른 게시글 목록 조회에 성공한다")
        void getBoardListByView() {
            // when
            CustomBoardListResponse<BoardList> boardListResponse = boardListService.getBoardList(PAGE, SORT_BY, SEARCH_TYPE, SEARCH_WORD);

            // then
            assertThat(boardListResponse.boardList().size()).isLessThanOrEqualTo(PAGE_SIZE);
            assertThat(boardListResponse.boardList().size()).isEqualTo(PAGE_SIZE);

            assertAll(
                    () -> assertThat(boardListResponse.boardList().get(0).boardId()).isEqualTo(boardList[14].getId()),
                    () -> assertThat(boardListResponse.boardList().get(0).title()).isEqualTo(boardList[14].getTitle()),
                    () -> assertThat(boardListResponse.boardList().get(0).category()).isEqualTo(boardList[14].getCategory()),
                    () -> assertThat(boardListResponse.boardList().get(0).view()).isEqualTo(boardList[14].getView()),
                    () -> assertThat(boardListResponse.boardList().get(0).writerId()).isEqualTo(boardList[14].getWriter().getId()),
                    () -> assertThat(boardListResponse.boardList().get(0).writerNickname()).isEqualTo(boardList[14].getWriter().getNickname())
            );
        }
    }
}
