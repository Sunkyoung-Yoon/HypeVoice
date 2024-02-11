package hypevoice.hypevoiceback.studio.dto;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.*;
import org.springframework.format.annotation.NumberFormat;

public record StudioRequest(
        @NotBlank(message = "제목 작성은 필수 입니다.")
        @Size(min = 2, message = "제목은 최소 2자 이상으로 작성해주세요.")
        @Size(max = 20, message = "제목은 최대 20자 이내로 작성해주세요.")
        String title,
        @Size(max = 50, message = "소개는 최대 50자 이내로 작성해주세요.")
        String intro,

        @Max(value = 6, message = "최대 6인까지 설정 가능합니다.")
        @Min(value = 1, message = "최소 1인부터 설정 가능합니다.")
        int limitNumber,
        @PositiveOrZero(message = "0 이상의 수만 설정 가능합니다.")
        @Max(value = 1, message = "1 이하의 값만 설정 가능합니다.")
        int isPublic,
        @Size(max = 4, message = "비밀번호는 4자로 작성해주세요.")
        @Size(min = 4, message = "비밀번호는 4자로 작성해주세요.")
        @NumberFormat
        @Nullable
        String password
) {

}
