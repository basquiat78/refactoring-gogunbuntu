package converter;

/**
 * 만일 attribute가 null인 경우
 * 이것을 구현한 enum class에서 default값을 반환하게 만든다.
 */
public interface EnumNullOperation<T> {
    T defaultIfNull();
}
