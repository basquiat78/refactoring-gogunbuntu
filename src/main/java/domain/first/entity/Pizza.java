package domain.first.entity;

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
    private long price;

    /** 가독성을 높여주기 위해 콤마를 찍은 스트링으로 반환한다. */
    public String getPrice() {
        return new DecimalFormat("#,##0").format(this.price);
    }

}
