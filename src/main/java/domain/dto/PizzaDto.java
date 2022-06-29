package domain.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import domain.entity.Pizza;
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
public class PizzaDto {

    /** 피자 이름 - 슈프림, 쉬림프, 페퍼로니 등등 */
    private String name;

    /** 도우 : 호밀, 일반, 흑미 등등 */
    private String dough;

    /** 가격 */
    private String price;

    /** 그 무언가가 될 정보 */
    private String doSome;

    @JsonProperty("formatted")
    public String formatted() {
        return "formatted : " + this.price;
    }
    /**
     * entity -> dto
     * @param entity
     * @return PizzaDto
     */
    public static PizzaDto entityToDto(Pizza entity) {
        return PizzaMapper.INSTANCE.entityToDto(entity);
    }

    /**
     * dto -> entity
     * @return Pizza
     */
    public Pizza dtoToEntity() {
        return PizzaMapper.INSTANCE.dtoToEntity(this);
    }

}


