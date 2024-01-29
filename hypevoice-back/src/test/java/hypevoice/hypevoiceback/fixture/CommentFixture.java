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
    COMMENT_1("내용1", "음성댓글url1"),
    COMMENT_2("내용2", "음성댓글url2")
    ;

    private final String content;
    private final String voiceCommentUrl;

    public Comment toComment(Member writer, Board board) {
        return Comment.createComment(writer, board, content, voiceCommentUrl);
    }
}
