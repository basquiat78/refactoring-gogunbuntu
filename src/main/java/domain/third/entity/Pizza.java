package domain.third.entity;

import lombok.*;

import java.text.DecimalFormat;

/**
 * pizza entity
 */
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Pizza {

    /** 피자 이름 - 슈프림, 쉬림프, 페퍼로니 등등 */
    @Getter
    private String name;

    /** 도우 : 호밀, 일반, 흑미 등등 */
    @Getter
    private String dough;

    /** 가격 */
    @Getter
    private long price;

}
