package hypevoice.hypevoiceback.comment.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record CommentList(
        Long commentId,
        String content,
        String voiceCommentUrl,
        LocalDateTime createdDate,
        Long writerId,
        String writerNickname
) {
    @QueryProjection
    public CommentList {
    }
}
