package hypevoice.hypevoiceback.member.domain;

import hypevoice.hypevoiceback.global.utils.EnumConverter;
import hypevoice.hypevoiceback.global.utils.EnumStandard;
import lombok.Getter;
import lombok.RequiredArgsConstructor;


@Getter
@RequiredArgsConstructor
public enum SocialType implements EnumStandard {
    NAVER("NAVER", "네이버"),
    KAKAO("KAKAO", "카카오");

    private final String socialType;
    private final String socialTitle;

    @Override
    public String getValue() {
        return socialType;
    }

    @jakarta.persistence.Converter
    public static class SocialTypeConverter extends EnumConverter<SocialType> {
        public SocialTypeConverter() {
            super(SocialType.class);
        }
    }
}
