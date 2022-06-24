package converter;

import domain.ActiveType;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import static org.springframework.util.StringUtils.isEmpty;

/**
 * upper/lower case를 적용한다.
 */
@Converter
public class ActiveTypeConverter implements AttributeConverter<ActiveType, String> {

    /**
     * enum -> db
     * @param attribute
     * @return String
     */
    @Override
    public String convertToDatabaseColumn(ActiveType attribute) {
        // null이면 default 세팅해준다.
        if(attribute == null) {
            return ActiveType.ACTIVE.name().toLowerCase();
        }
        return attribute.name().toLowerCase();
    }

    /**
     * db -> enum
     * @param dbData
     * @return
     */
    @Override
    public ActiveType convertToEntityAttribute(String dbData) {
        if(isEmpty(dbData)) {
            return ActiveType.ACTIVE;
        }
        return ActiveType.valueOf(dbData.toUpperCase());
    }

}