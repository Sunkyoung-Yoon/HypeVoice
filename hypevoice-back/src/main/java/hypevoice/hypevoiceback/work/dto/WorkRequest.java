package hypevoice.hypevoiceback.work.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
public record WorkRequest(
        @NotBlank(message = "제목 작성은 필수입니다.")
        @Size(min = 1, message = "제목은 최소 1자 이상으로 작성해주세요.")
        @Size(max = 50, message = "제목은 최대 20자 이내로 작성해주세요.")
        String title,
        String videoLink,
        @Size(max = 2000, message = "소개는 최대 2000자 이내로 작성해주세요.")
        String info,
        int isRep
) {

}
