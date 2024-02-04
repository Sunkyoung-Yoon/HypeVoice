package hypevoice.hypevoiceback.board.query;

import hypevoice.hypevoiceback.board.domain.Board;
import hypevoice.hypevoiceback.board.domain.BoardRepository;
import hypevoice.hypevoiceback.board.domain.BoardSearchType;
import hypevoice.hypevoiceback.board.dto.BoardList;
import hypevoice.hypevoiceback.board.dto.CustomBoardListResponse;
import hypevoice.hypevoiceback.common.RepositoryTest;
import hypevoice.hypevoiceback.member.domain.Member;
import hypevoice.hypevoiceback.member.domain.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static hypevoice.hypevoiceback.fixture.BoardFixture.*;
import static hypevoice.hypevoiceback.fixture.MemberFixture.SUNKYOUNG;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayName("Board [Repository Layer] -> BoardListQueryRepository 테스트")
class BoardListQueryRepositoryImplTest extends RepositoryTest {
    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private BoardRepository boardRepository;

    private Board[] boardList = new Board[15];

    private Member writer;

    private static final BoardSearchType SEARCH_TYPE_TITLE = BoardSearchType.TITLE;
    private static final BoardSearchType SEARCH_TYPE_CONTENT = BoardSearchType.CONTENT;
    private static final String SEARCH_TITLE = "제목";
    private static final String SEARCH_CONTENT = "내용1";
    private static final int PAGE = 0;

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

    @Test
    @DisplayName("검색 결과에 따른 게시글을 최신순으로 조회한다")
    void getBoardListOrderByTime() {
        // when
        CustomBoardListResponse<BoardList> boardListOrderByTime = boardRepository.getBoardListOrderByTime(SEARCH_TYPE_CONTENT, SEARCH_CONTENT, PAGE);

        // then
        assertAll(
                () -> assertThat(boardListOrderByTime.boardList().size()).isEqualTo(6),
                () -> assertThat(boardListOrderByTime.boardList().get(0).boardId()).isEqualTo(boardList[14].getId()),
                () -> assertThat(boardListOrderByTime.boardList().get(0).title()).isEqualTo(boardList[14].getTitle()),
                () -> assertThat(boardListOrderByTime.boardList().get(0).view()).isEqualTo(boardList[14].getView()),
                () -> assertThat(boardListOrderByTime.boardList().get(0).writerId()).isEqualTo(boardList[14].getWriter().getId()),
                () -> assertThat(boardListOrderByTime.boardList().get(0).writerNickname()).isEqualTo(boardList[14].getWriter().getNickname())
        );
    }

    @Test
    @DisplayName("검색 결과에 따른 게시글을 조회순으로 조회한다")
    void getBoardListOrderByHit() {
        // when
        CustomBoardListResponse<BoardList> boardListOrderByHit = boardRepository.getBoardListOrderByHit(SEARCH_TYPE_TITLE, SEARCH_TITLE, PAGE);

        // then
        assertAll(
                () -> assertThat(boardListOrderByHit.boardList().size()).isEqualTo(10),
                () -> assertThat(boardListOrderByHit.boardList().get(0).boardId()).isEqualTo(boardList[14].getId()),
                () -> assertThat(boardListOrderByHit.boardList().get(0).title()).isEqualTo(boardList[14].getTitle()),
                () -> assertThat(boardListOrderByHit.boardList().get(0).view()).isEqualTo(boardList[14].getView()),
                () -> assertThat(boardListOrderByHit.boardList().get(0).writerId()).isEqualTo(boardList[14].getWriter().getId()),
                () -> assertThat(boardListOrderByHit.boardList().get(0).writerNickname()).isEqualTo(boardList[14].getWriter().getNickname())
        );
    }
}
