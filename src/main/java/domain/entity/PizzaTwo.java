package domain.entity;

import lombok.*;

/**
 * pizza entity
 */
@Getter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PizzaTwo {

    /** 피자 이름 - 슈프림, 쉬림프, 페퍼로니 등등 */
    private String name;

    /** 도우 : 호밀, 일반, 흑미 등등 */
    private String dough;

    /** 가격 */
    private long price;

    /** 만든 사람 */
    private String maker;

}
