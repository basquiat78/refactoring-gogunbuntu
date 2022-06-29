import domain.dto.PizzaDto;
import domain.dto.PizzaDtoTwo;
import domain.entity.Pizza;
import domain.entity.PizzaTwo;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static util.CommonUtils.toJson;

class SimpleTest {

    /**
     * 맨 처음 작성되었던 InvestorQualification를 한번 확인차 확인해 본다.
     * 뭐 잘 돌아갔으니 문제가 없었을 코드일 것이다.
     *
     */
    @Test
    @DisplayName("STEP1: 컨버트가 작동을 잘하는가??")
    void STEP1() {
        Pizza pizza = Pizza.builder()
                           .name("고구마피자")
                           .dough("흑미")
                           .price(10000000)
                           .maker("basquiat")
                           .build();
        System.out.println(pizza.toString());
        PizzaDto dto = PizzaDto.entityToDto(pizza);
        System.out.println(toJson(dto));
        Pizza again = dto.dtoToEntity();
        System.out.println(again.toString());

        PizzaTwo pizzaTwo = PizzaTwo.builder()
                                    .name("고구마피자")
                                    .dough("흑미")
                                    .price(10000000)
                                    .maker("basquiat")
                                    .build();
        System.out.println(pizzaTwo.toString());
        PizzaDtoTwo dtoTwo = PizzaDtoTwo.entityToDto(pizzaTwo);
        System.out.println(dtoTwo.toString());
        PizzaTwo againTwo = dtoTwo.dtoToEntity();
        System.out.println(againTwo.toString());
    }

}
