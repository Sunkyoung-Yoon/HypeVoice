package hypevoice.hypevoiceback.board.domain;

import hypevoice.hypevoiceback.global.BaseTimeEntity;
import hypevoice.hypevoiceback.member.domain.Member;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "board")
public class Board extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "board_id")
    private Long id;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "content", nullable = false)
    private String content;

    private int view;

    private Category category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "writer_id", referencedColumnName = "member_id")
    private Member writer;

    private Board(Member writer, String title, String content, Category category) {
        this.writer = writer;
        this.title = title;
        this.content = content;
        this.view = 0;
        this.category = category;
    }

    public static Board createBoard(Member writer, String title, String content, Category category) {
        return new Board(writer, title, content, category);
    }

    public void updateBoard(String title, String content) {
        this.title = title;
        this.content = content;
    }
}
