package hypevoice.hypevoiceback.board.dto;

import com.querydsl.core.annotations.QueryProjection;
import hypevoice.hypevoiceback.board.domain.Category;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class BoardList {
    private final Long boardId;
    private final String title;
    private final int view;
    private final Category category;
    private final LocalDateTime createdDate;
    private final Long writerId;
    private final String writerNickname;

    @QueryProjection
    public BoardList(Long boardId, String title, int view, Category category,
                     LocalDateTime createdDate, Long writerId, String writerNickname) {
        this.boardId = boardId;
        this.title = title;
        this.view = view;
        this.category = category;
        this.createdDate = createdDate;
        this.writerId = writerId;
        this.writerNickname = writerNickname;
    }
}
