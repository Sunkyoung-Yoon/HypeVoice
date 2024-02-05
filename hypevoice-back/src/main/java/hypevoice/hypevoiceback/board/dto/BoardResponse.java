package hypevoice.hypevoiceback.board.dto;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record BoardResponse(
        Long boardId,
        String title,
        String content,
        int view,
        String recordUrl,
        String category,
        LocalDateTime createdDate,
        Long writerId,
        String writerNickname
) {
}
