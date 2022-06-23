import domain.first.dto.PizzaDto;
import domain.first.dto.PizzaRecipeDto;
import domain.first.dto.PizzaViewDto;
import domain.first.entity.Pizza;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static util.CommonUtils.toJson;

class EntityDtoTest {

    /**
     * 단순하게 dto를 통해 데이터를 전달하는 데 쓸데 없는 코드가 비지니스 로직에서 존재할 수가 있다.
     * 게다가 원하는 값을 얻기 위해서 가독성이 떨어지는 코드들이 눈에 띈다.
     *
     */
    @Test
    @DisplayName("STEP1: dto에 매핑하기 위해서는 이런 불편함이 있다.")
    void STEP1() {

        Pizza pizza = Pizza.builder()
                           .name("쉬림프 피자")
                           .dough("흑미")
                           .price(10000)
                           .build();

        /** 첫번째 DTO는 view로 응답을 주기 위한 객체이다. */
        PizzaViewDto pizzaViewDto = PizzaViewDto.builder()
                                                .name(pizza.getName())
                                                .dough(pizza.getDough())
                                                .price(pizza.getPrice())
                                                .build();

        System.out.println(pizzaViewDto.toString());

        long optionPrice = 500;
        // 하지만 ide에서 에러를 보여준다. 당연히 타입이 맞지 않기 때문이다.
        //long totalPrice = pizza.getPrice() + optionPrice;

        // 꼭 이렇게 해야하는건가?
        long totalPrice = Long.parseLong(pizza.getPrice().replace(",", "")) + optionPrice;

        /** 고객에게 최종 주문 확인서 정보를 보여주기 위한 dto */
        PizzaRecipeDto pizzaRecipeDto = PizzaRecipeDto.builder()
                                                      .name(pizza.getName())
                                                      .dough(pizza.getDough())
                                                      .totalPrice(Long.toString(totalPrice))
                                                      .build();
        System.out.println(pizzaRecipeDto.toString());

        /** 그냥 일반적인 dto */
        PizzaDto pizzaDto = PizzaDto.builder()
                                    .name(pizza.getName())
                                    .dough(pizza.getDough())
                                    .price(Long.parseLong(pizza.getPrice().replace(",", "")))
                                    .build();
        System.out.println(pizzaDto.toString());

    }

    /**
     * 기존의 원본 데이터는 유지하고 메소드를 하나 추가하자.
     * 개선된 것은 특별히 값을 변환하기 위한 쓸데없는 코드가 줄었다.
     * 다만 하나의 dto를 위해서 메소드를 만드는 것이 무언가 불필요하다고 생각이 든다.
     *
     * 하지만 여전히 몇가지가 걸린다.
     * 예를 들면 통화 표시라든가 달러의 경우 넘버 포맷을 #.00처럼 센트를 표시하기 위한 처리를 해야한다면??
     *
     */
    @Test
    @DisplayName("STEP2: 그렇다면 엔티티에 추가 메소드를 넣어두자.")
    void STEP_2() {
        domain.second.entity.Pizza pizza = domain.second.entity.Pizza.builder()
                                                                     .name("쉬림프 피자")
                                                                     .dough("흑미")
                                                                     .price(10000)
                                                                     .build();

        /** 첫번째 DTO는 view로 응답을 주기 위한 객체이다. */
        domain.second.dto.PizzaViewDto pizzaViewDto = domain.second.dto.PizzaViewDto.builder()
                                                                                    .name(pizza.getName())
                                                                                    .dough(pizza.getDough())
                                                                                    .price(pizza.getFormattedPrice())
                                                                                    .build();

        System.out.println(pizzaViewDto.toString());

        long optionPrice = 500;
        long totalPrice = pizza.getPrice() + optionPrice;

        /** 고객에게 최종 주문 확인서 정보를 보여주기 위한 dto */
        domain.second.dto.PizzaRecipeDto pizzaRecipeDto = domain.second.dto.PizzaRecipeDto.builder()
                                                                                          .name(pizza.getName())
                                                                                          .dough(pizza.getDough())
                                                                                          .totalPrice(Long.toString(totalPrice))
                                                                                          .build();
        System.out.println(pizzaRecipeDto.toString());

        /** 그냥 일반적인 dto */
        domain.second.dto.PizzaDto pizzaDto = domain.second.dto.PizzaDto.builder()
                                                                        .name(pizza.getName())
                                                                        .dough(pizza.getDough())
                                                                        .price(pizza.getPrice())
                                                                        .build();
        System.out.println(pizzaDto.toString());

    }

    /**
     * 엔티티에 추가한 메소드는 삭제하자.
     * dto에서 전달할 데이터 정보의 형태를 담당하게 함으로써 특별히 어떤 작업을 할 필요가 없어졌다.
     * 요청사항이 발생한다면 side-effect를 고려할 필요도 없다.
     * 해당 dto에 대해서만 수정을 하면 되기 때문이다.
     */
    @Test
    @DisplayName("STEP3: entity는 그냥 원본으로 놔두고 dto에 해당 기능을 위임하자.")
    void STEP_3() {
        domain.third.entity.Pizza pizza = domain.third.entity.Pizza.builder()
                                                                   .name("쉬림프 피자")
                                                                   .dough("흑미")
                                                                   .price(10000)
                                                                   .build();

        /** 첫번째 DTO는 view로 응답을 주기 위한 객체이다. */
        domain.third.dto.PizzaViewDto pizzaViewDto = domain.third.dto.PizzaViewDto.builder()
                                                                                  .name(pizza.getName())
                                                                                  .dough(pizza.getDough())
                                                                                  .price(pizza.getPrice())
                                                                                  .build();

        System.out.println(pizzaViewDto.toString());
        // 결과를 보면 실제로 formattedPrice가 찍히지 않는다 하지만 다음과 같이 해보자.
        /*
        String jsonString;
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            jsonString = objectMapper.writeValueAsString(pizzaViewDto);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e.getMessage());
        }
        System.out.println(jsonString);
        */

        System.out.println(toJson(pizzaViewDto));

        long optionPrice = 500;
        /** 고객에게 최종 주문 확인서 정보를 보여주기 위한 dto */
        domain.third.dto.PizzaRecipeDto pizzaRecipeDto = domain.third.dto.PizzaRecipeDto.builder()
                                                                                        .name(pizza.getName())
                                                                                        .dough(pizza.getDough())
                                                                                        .price(pizza.getPrice())
                                                                                        .optionPrice(optionPrice)
                                                                                        .build();
        System.out.println(pizzaRecipeDto.toString());

        /** 그냥 일반적인 dto */
        domain.third.dto.PizzaDto pizzaDto = domain.third.dto.PizzaDto.builder()
                                                                      .name(pizza.getName())
                                                                      .dough(pizza.getDough())
                                                                      .price(pizza.getPrice())
                                                                      .build();
        System.out.println(pizzaDto.toString());

    }

}
