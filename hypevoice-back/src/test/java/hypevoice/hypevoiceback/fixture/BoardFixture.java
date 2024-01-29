package hypevoice.hypevoiceback.fixture;

import hypevoice.hypevoiceback.board.domain.Board;
import hypevoice.hypevoiceback.board.domain.Category;
import hypevoice.hypevoiceback.member.domain.Member;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum BoardFixture {
    BOARD_0("제목0", "내용0", Category.FEEDBACK),
    BOARD_1("제목1", "내용1", Category.FEEDBACK),
    BOARD_2("제목2", "내용2", Category.FEEDBACK)
    ;

    private final String title;
    private final String content;
    private final Category category;

    public Board toBoard(Member writer) {
        return Board.createBoard(writer, title, content, category);
    }
}
