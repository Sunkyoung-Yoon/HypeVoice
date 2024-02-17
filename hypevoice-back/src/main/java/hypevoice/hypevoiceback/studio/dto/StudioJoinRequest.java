package hypevoice.hypevoiceback.studio.dto;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.Size;
import org.springframework.format.annotation.NumberFormat;

public record StudioJoinRequest(
        @Size(max = 4, message = "비밀번호는 4자로 작성해주세요.")
        @Size(min = 4, message = "비밀번호는 4자로 작성해주세요.")
        @NumberFormat
        @Nullable
        String password
) {
}
