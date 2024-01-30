package hypevoice.hypevoiceback.board.service;

import hypevoice.hypevoiceback.board.domain.Board;
import hypevoice.hypevoiceback.board.dto.BoardResponse;
import hypevoice.hypevoiceback.board.exception.BoardErrorCode;
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
import static hypevoice.hypevoiceback.fixture.MemberFixture.GABIN;
import static hypevoice.hypevoiceback.fixture.MemberFixture.SUNKYOUNG;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayName("Board [Service Layer] -> BoardService 테스트")
public class BoardServiceTest extends ServiceTest {
    @Autowired
    private BoardService boardService;

    @Autowired
    private BoardFindService boardFindService;

    private Member writer;
    private Member notWriter;
    private Board board;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    @BeforeEach
    void setup() {
        writer = memberRepository.save(SUNKYOUNG.toMember());
        notWriter = memberRepository.save(GABIN.toMember());
        board = boardRepository.save(BOARD_0.toBoard(writer));
    }

    @Test
    @DisplayName("게시글 등록에 성공한다")
    void success() {
        // when
        Long boardId = boardService.create(writer.getId(), "제목", "내용", "feedback");

        // then
        Board findBoard = boardRepository.findById(boardId).orElseThrow();
        assertAll(
                () -> assertThat(findBoard.getWriter().getId()).isEqualTo(writer.getId()),
                () -> assertThat(findBoard.getTitle()).isEqualTo("제목"),
                () -> assertThat(findBoard.getContent()).isEqualTo("내용"),
                () -> assertThat(findBoard.getView()).isEqualTo(0),
                () -> assertThat(findBoard.getCategory()).isEqualTo(board.getCategory()),
                () -> assertThat(findBoard.getCreatedDate().format(formatter)).isEqualTo(LocalDateTime.now().format(formatter)),
                () -> assertThat(findBoard.getModifiedDate().format(formatter)).isEqualTo(LocalDateTime.now().format(formatter))
        );
    }

    @Nested
    @DisplayName("게시글 수정")
    class update {
        @Test
        @DisplayName("다른 사람의 게시글은 수정할 수 없다")
        void throwExceptionByUserNotBoardWriter() {
            // when - then
            assertThatThrownBy(() -> boardService.update(notWriter.getId(),board.getId(), "제목2", "내용2"))
                    .isInstanceOf(BaseException.class)
                    .hasMessage(BoardErrorCode.USER_IS_NOT_BOARD_WRITER.getMessage());
        }

        @Test
        @DisplayName("게시글 수정에 성공한다")
        void success() {
            // given
            boardService.update(writer.getId(), board.getId(), "제목2", "내용2");

            // when
            Board findBoard = boardFindService.findById(board.getId());

            // then
            assertAll(
                    () -> assertThat(findBoard.getTitle()).isEqualTo("제목2"),
                    () -> assertThat(findBoard.getContent()).isEqualTo("내용2"),
                    () -> assertThat(findBoard.getModifiedDate().format(formatter)).isEqualTo(LocalDateTime.now().format(formatter))
            );
        }
    }

    @Nested
    @DisplayName("게시글 삭제")
    class delete {
        @Test
        @DisplayName("다른 사람의 게시글은 삭제할 수 없다")
        void throwExceptionByUserNotBoardWriter() {
            // when - then
            assertThatThrownBy(() -> boardService.delete(notWriter.getId(),board.getId()))
                    .isInstanceOf(BaseException.class)
                    .hasMessage(BoardErrorCode.USER_IS_NOT_BOARD_WRITER.getMessage());
        }

        @Test
        @DisplayName("게시글 삭제에 성공한다")
        void success() {
            // given
            boardService.delete(writer.getId(), board.getId());

            // when - then
            assertThatThrownBy(() -> boardFindService.findById(board.getId()))
                    .isInstanceOf(BaseException.class)
                    .hasMessage(BoardErrorCode.BOARD_NOT_FOUND.getMessage());
        }
    }

    @Nested
    @DisplayName("게시글 상세 조회")
    class read {
        @Test
        @DisplayName("게시글 상세 조회에 성공한다")
        void success() {
            // when
            BoardResponse boardResponse = boardService.read(board.getId());

            // then
            assertAll(
                    () -> assertThat(boardResponse.boardId()).isEqualTo(board.getId()),
                    () -> assertThat(boardResponse.title()).isEqualTo(board.getTitle()),
                    () -> assertThat(boardResponse.content()).isEqualTo(board.getContent()),
                    () -> assertThat(boardResponse.view()).isEqualTo(1),
                    () -> assertThat(boardResponse.category()).isEqualTo(board.getCategory().getValue()),
                    () -> assertThat(boardResponse.createdDate()).isEqualTo(board.getCreatedDate()),
                    () -> assertThat(boardResponse.writerId()).isEqualTo(board.getWriter().getId()),
                    () -> assertThat(boardResponse.writerNickname()).isEqualTo(board.getWriter().getNickname())
            );
        }
    }
}
