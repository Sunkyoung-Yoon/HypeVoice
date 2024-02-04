package hypevoice.hypevoiceback.board.dto;

import com.querydsl.core.annotations.QueryProjection;
import hypevoice.hypevoiceback.board.domain.Category;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record BoardList (
        Long boardId,
        String title,
        int view,
        Category category,
        LocalDateTime createdDate,
        Long writerId,
        String writerNickname
)
{
    @QueryProjection
    public BoardList {
    }
}
