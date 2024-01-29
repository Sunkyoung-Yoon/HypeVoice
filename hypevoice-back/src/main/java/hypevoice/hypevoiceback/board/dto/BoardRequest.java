package hypevoice.hypevoiceback.board.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record BoardRequest(
        @NotBlank(message = "제목 작성은 필수입니다.")
        @Size(min = 1, message = "제목은 최소 1자 이내로 작성해주세요.")
        @Size(max = 50, message = "제목은 50자 이내로 작성해주세요.")
        String title,

        @NotBlank(message = "내용 작성은 필수입니다.")
        @Size(min = 1, message = "내용은 최소 1자 이상으로 작성해주세요.")
        @Size(max = 5000, message = "내용은 5000자 이내로 작성해주세요.")
        String content,

        @NotBlank(message = "카테고리 선택은 필수입니다.")
        String category
) {
}
