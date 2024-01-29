package hypevoice.hypevoiceback.comment.domain;

import hypevoice.hypevoiceback.board.domain.Board;
import hypevoice.hypevoiceback.board.domain.Category;
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

    private String voice_comment_url;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "writer_id", referencedColumnName = "member_id")
    private Member writer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id", referencedColumnName = "board_id")
    private Board board;

    private Comment(Member writer, Board board, String content, String voice_comment_url) {
        this.writer = writer;
        this.board = board;
        this.content = content;
        this.voice_comment_url = voice_comment_url;
    }

    public static Comment createComment(Member writer, Board board, String content, String voice_comment_url) {
        return new Comment(writer, board, content, voice_comment_url);
    }
}
