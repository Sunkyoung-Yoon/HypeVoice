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

    @Column(name = "title", nullable = false, length = 50)
    private String title;

    @Column(name = "content", nullable = false, length = 5000)
    private String content;

    private int view;

    @Column(length = 500)
    private String recordUrl;

    @Convert(converter = Category.CategoryConverter.class)
    private Category category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "writer_id", referencedColumnName = "member_id")
    private Member writer;

    @OneToMany(mappedBy = "board", cascade = PERSIST, orphanRemoval = true)
    private List<Comment> commentList = new ArrayList<>();

    private Board(Member writer, String title, String content, String recordUrl, Category category) {
        this.writer = writer;
        this.title = title;
        this.content = content;
        this.recordUrl = recordUrl;
        this.view = 0;
        this.category = category;
    }

    public static Board createBoard(Member writer, String title, String content, String recordUrl, Category category) {
        return new Board(writer, title, content, recordUrl, category);
    }

    public void updateBoard(String title, String content, String recordUrl) {
        this.title = title;
        this.content = content;
        this.recordUrl = recordUrl;
    }

    public void addComment(Member writer, String content, String voice_comment_url) {
        commentList.add(Comment.createComment(writer, this, content, voice_comment_url));
    }

    public void updateView() {
        this.view = this.view + 1;
    }
}
