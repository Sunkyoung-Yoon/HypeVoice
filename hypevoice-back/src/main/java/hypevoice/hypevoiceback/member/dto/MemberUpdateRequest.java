package hypevoice.hypevoiceback.member.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record MemberUpdateRequest(
        @NotBlank(message = "닉네임은 필수입니다.")
        @Size(min = 1, message = "닉네임은 최소 1자 이상으로 작성해주세요.")
        @Size(max = 10, message = "닉네임은 10자 이내로 작성해주세요.")
        String nickname
) {
}
