package hypevoice.hypevoiceback.fixture;

import hypevoice.hypevoiceback.board.domain.Board;
import hypevoice.hypevoiceback.board.domain.Category;
import hypevoice.hypevoiceback.member.domain.Member;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum BoardFixture {
    BOARD_0("제목0", "내용0", null, Category.FEEDBACK),
    BOARD_1("제목1", "내용1", null, Category.FEEDBACK),
    BOARD_2("제목2", "내용2", null, Category.FEEDBACK),
    BOARD_3("제목3", "내용3", null, Category.FEEDBACK),
    BOARD_4("제목4", "내용4", null, Category.FEEDBACK),
    BOARD_5("제목5", "내용5", null, Category.FEEDBACK),
    BOARD_6("제목6", "내용6", null, Category.FEEDBACK),
    BOARD_7("제목7", "내용7", null, Category.FEEDBACK),
    BOARD_8("제목8", "내용8", null, Category.FEEDBACK),
    BOARD_9("제목9", "내용9", null, Category.FEEDBACK),
    BOARD_10("제목10", "내용10", null, Category.FEEDBACK),
    BOARD_11("제목11", "내용11", null, Category.FEEDBACK),
    BOARD_12("제목12", "내용12", null, Category.FEEDBACK),
    BOARD_13("제목13", "내용13", null, Category.FEEDBACK),
    BOARD_14("제목14", "내용14", null, Category.FEEDBACK),
    ;

    private final String title;
    private final String content;
    private final String recordUrl;
    private final Category category;

    public Board toBoard(Member writer) {
        return Board.createBoard(writer, title, content, recordUrl, category);
    }
}
