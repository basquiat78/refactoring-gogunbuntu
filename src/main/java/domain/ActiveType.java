package domain;

import converter.EnumNullOperation;
import converter.LowerCaseEnumConverter;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * active type enum class
 */
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public enum ActiveType implements EnumNullOperation<ActiveType> {

    ACTIVE("활성"),
    INACTIVE("비활성"),
    BLACKLIST("블랙컨슈머"),
    DELETE("탈퇴");

    @Getter
    private String description;

    /**
     * null이라면 활성 상태의 enum을 반환한다.
     * @return ActiveType
     */
    @Override
    public ActiveType defaultIfNull() {
        return ActiveType.ACTIVE;
    }

    /**
     * Custom Converter for lower case
     */
    public static class LowerCaseConverter extends LowerCaseEnumConverter<ActiveType> {
        public LowerCaseConverter() {
            super(ActiveType.class);
        }
    }

}
