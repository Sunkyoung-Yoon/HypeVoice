package hypevoice.hypevoiceback.board.domain;

import hypevoice.hypevoiceback.comment.domain.Comment;
import hypevoice.hypevoiceback.global.BaseTimeEntity;
import hypevoice.hypevoiceback.member.domain.Member;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.CascadeType.PERSIST;

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

    @Convert(converter = Category.CategoryConverter.class)
    private Category category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "writer_id", referencedColumnName = "member_id")
    private Member writer;

    @OneToMany(mappedBy = "board", cascade = PERSIST, orphanRemoval = true)
    private List<Comment> commentList = new ArrayList<>();

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

    public void addComment(Member writer, String content, String voice_comment_url) {
        commentList.add(Comment.createComment(writer, this, content, voice_comment_url));
    }

    public void updateView() {
        this.view = this.view + 1;
    }
}
