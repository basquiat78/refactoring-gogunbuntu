package converter;

import lombok.RequiredArgsConstructor;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.Arrays;
import java.util.NoSuchElementException;

/**
 * lower case처리를 위한 추상 클래스
 * created by basquiat
 */
@Converter
@RequiredArgsConstructor
public abstract class LowerCaseEnumConverter<T extends Enum<T> & EnumNullOperation<T>> implements AttributeConverter<T, String> {

    private final Class<T> clazz;

    /**
     * enum 상수 값을 가져와서 lowerCase로 반환한다.
     * @param attribute
     * @return String
     */
    @Override
    public String convertToDatabaseColumn(T attribute) {
        if(attribute == null) {
            T[] enums = clazz.getEnumConstants();
            // EnumNullOperation을 구현한 enum에 정의된 defaultIfNull()을 통해서 해당 default enum정보를 반환한다.
            attribute = defaultEnum(enums);
        }
        return attribute.name().toLowerCase();
    }

    /**
     * 디비 정보는 lowerCase로 upperCase로 변환후 비교후 해당 enum객체를 반환하게 한다.
     * @param dbData
     * @return T
     */
    @Override
    public T convertToEntityAttribute(String dbData) {
        T[] enums = clazz.getEnumConstants();
        try {
            return Arrays.stream(enums)
                         // dbData가 null이면 catch로 넘어가서 T의 defaultIfNull을 반환할 것이다.
                         .filter(en -> en.name().equals(dbData.toUpperCase()))
                         // orElseThrow가 발생한다면 이건 디비쪽에서 컬럼의 enum이 확장되서 상태값이 늘었을 가능성이 아주 높다.
                         .findFirst().orElseThrow(NoSuchElementException::new);
        } catch(NullPointerException e) {
            // dbData가 null이면 toUpperCase()때 npe가 발생할 것이다.
            // 아니면 try - catch가 귀찮다면 애초에 null체크해서 defaultEnum을 반환해도 좋다.
            return defaultEnum(enums);
        }
    }

    /**
     * 반복되는 공통 코드 줄이자
     * @param enums
     * @return T
     */
    private T defaultEnum(T[] enums) {
        return Arrays.stream(enums)
                     .filter(en -> en == en.defaultIfNull())
                     .findFirst().orElseThrow(NoSuchElementException::new);
    }

}
