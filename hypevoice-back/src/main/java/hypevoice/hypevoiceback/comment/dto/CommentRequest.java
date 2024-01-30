package hypevoice.hypevoiceback.comment.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CommentRequest(
        @NotBlank(message = "내용 작성은 필수입니다.")
        @Size(min = 1, message = "댓글은 최소 1자 이내로 작성해주세요.")
        @Size(max = 50, message = "댓글은 400자 이내로 작성해주세요.")
        String content,

        String voiceCommentUrl
) {
}
