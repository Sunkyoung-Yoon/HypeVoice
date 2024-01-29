package hypevoice.hypevoiceback.comment.domain;

import hypevoice.hypevoiceback.board.domain.Board;
import hypevoice.hypevoiceback.global.BaseTimeEntity;
import hypevoice.hypevoiceback.member.domain.Member;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "comment")
public class Comment extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    private Long id;

    @Column(name = "content", nullable = false)
    private String content;

    private String voiceCommentUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "writer_id", referencedColumnName = "member_id")
    private Member writer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id", referencedColumnName = "board_id")
    private Board board;

    private Comment(Member writer, Board board, String content, String voiceCommentUrl) {
        this.writer = writer;
        this.board = board;
        this.content = content;
        this.voiceCommentUrl = voiceCommentUrl;
    }

    public static Comment createComment(Member writer, Board board, String content, String voiceCommentUrl) {
        return new Comment(writer, board, content, voiceCommentUrl);
    }
}
