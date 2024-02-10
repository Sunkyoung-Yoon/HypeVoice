package hypevoice.hypevoiceback.fixture;

import hypevoice.hypevoiceback.board.domain.Board;
import hypevoice.hypevoiceback.comment.domain.Comment;
import hypevoice.hypevoiceback.member.domain.Member;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CommentFixture {
    COMMENT_0("내용0", null),
    COMMENT_1("내용1", null),
    COMMENT_2("내용2", null),
    COMMENT_3("내용3", null),
    COMMENT_4("내용4", null),
    COMMENT_5("내용5", null),
    COMMENT_6("내용6", null),
    COMMENT_7("내용7", null),
    COMMENT_8("내용8", null),
    COMMENT_9("내용9", null),
    COMMENT_10("내용10", null),
    COMMENT_11("내용11", null),
    COMMENT_12("내용12", null),
    COMMENT_13("내용13", null),
    COMMENT_14("내용14", null),
    COMMENT_15("내용15", null),
    COMMENT_16("내용16", null),
    COMMENT_17("내용17", null),
    COMMENT_18("내용18", null),
    COMMENT_19("내용19", null),
    COMMENT_20("내용20", null),
    ;

    private final String content;
    private final String voiceCommentUrl;

    public Comment toComment(Member writer, Board board) {
        return Comment.createComment(writer, board, content, voiceCommentUrl);
    }
}
