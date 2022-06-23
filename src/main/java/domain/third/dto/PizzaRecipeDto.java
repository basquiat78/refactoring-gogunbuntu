package domain.third.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.text.DecimalFormat;

import static util.CommonUtils.numberFormat;

/**
 * pizza Dto
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PizzaRecipeDto {

    /** 피자 이름 - 슈프림, 쉬림프, 페퍼로니 등등 */
    private String name;

    /** 도우 : 호밀, 일반, 흑미 등등 */
    private String dough;

    /** 가격 */
    private long price;

    /** 추가 업차지 */
    private long optionPrice;

    /** 파자 가격과 옵션이 있다면 합산한 전체 가격을 보여준다. */
    private long totalPrice;

    public String getTotalPrice() {
        //return new DecimalFormat("#,##0").format(this.price + this.optionPrice);
        // 생성한 유틸 클래스를 활용하자.
        return numberFormat(this.price + this.optionPrice);
    }

}


