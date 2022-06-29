package domain.dto;

import domain.entity.PizzaTwo;
import domain.mapper.PizzaMapper;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * pizza Dto
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PizzaDtoTwo {

    /** 피자 이름 - 슈프림, 쉬림프, 페퍼로니 등등 */
    private String name;

    /** 도우 : 호밀, 일반, 흑미 등등 */
    private String dough;

    /** 가격 */
    private String price;

    /**
     * entity -> dto
     * @param entity
     * @return PizzaDto
     */
    public static PizzaDtoTwo entityToDto(PizzaTwo entity) {
        return PizzaMapper.INSTANCE.entityToDto(entity);
    }

    /**
     * dto -> entity
     * @return Pizza
     */
    public PizzaTwo dtoToEntity() {
        return PizzaMapper.INSTANCE.dtoToEntity(this);
    }

}


