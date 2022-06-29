package domain.mapper;

import domain.dto.PizzaDto;
import domain.dto.PizzaDtoTwo;
import domain.entity.Pizza;
import domain.entity.PizzaTwo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PizzaMapper {

    PizzaMapper INSTANCE = Mappers.getMapper(PizzaMapper.class);

    @Mapping(target = "name", expression = "java(formatEntityName(dto.getName()))")
    @Mapping(target = "dough", expression = "java(formatEntityDough(dto.getDough()))")
    @Mapping(target = "price", expression = "java( priceToLong(dto.getPrice()) )")
    Pizza dtoToEntity(PizzaDto dto);

    @Mapping(target = "name", expression = "java(formatEntityName(dto.getName()))")
    @Mapping(target = "dough", expression = "java(formatEntityDough(dto.getDough()))")
    @Mapping(target = "price", expression = "java( priceToLong(dto.getPrice()) )")
    PizzaTwo dtoToEntity(PizzaDtoTwo dto);

    /**
     * 변경이 잘되고 있는지 확인하기 위한 임의 메소드
     * @param name
     * @return String
     */
    default String formatEntityName(String name) {
        return "dto -> entity : " + name;
    }

    /**
     * 변경이 잘되고 있는지 확인하기 위한 임의 메소드
     * @param dough
     * @return String
     */
    default String formatEntityDough(String dough) {
        return "dto -> entity : " + dough;
    }

    @Mapping(target = "name", expression = "java(formatDtoName(entity.getName()))")
    @Mapping(target = "dough", expression = "java(formatDtoDough(entity.getDough()))")
    @Mapping(target = "price", numberFormat = "#,##0")
    PizzaDto entityToDto(Pizza entity);

    @Mapping(target = "name", expression = "java(formatDtoName(entity.getName()))")
    @Mapping(target = "dough", expression = "java(formatDtoDough(entity.getDough()))")
    @Mapping(target = "price", numberFormat = "#,##0")
    PizzaDtoTwo entityToDto(PizzaTwo entity);

    /**
     * 변경이 잘되고 있는지 확인하기 위한 임의 메소드
     * @param name
     * @return String
     */
    default String formatDtoName(String name) {
        return "entity -> dto : " + name;
    }

    /**
     * 변경이 잘되고 있는지 확인하기 위한 임의 메소드
     * @param dough
     * @return String
     */
    default String formatDtoDough(String dough) {
        return "entity -> dto : " + dough;
    }

    /**
     * long에서 string으로 numberFormat적용시 구분자 ','가 들어가기 때문에 다음과 제거후 롱으로 반환한다.
     * @param value
     * @return Long
     */
    default Long priceToLong(String value) {
        return Long.valueOf(value.replace(",", ""));
    }

}
