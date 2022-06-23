package domain.third.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import static util.CommonUtils.numberFormat;

/**
 * pizza Dto
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PizzaViewDto {

    /** 피자 이름 - 슈프림, 쉬림프, 페퍼로니 등등 */
    private String name;

    /** 도우 : 호밀, 일반, 흑미 등등 */
    private String dough;

    /** 가격 */
    private long price;

    /** 이렇게 하면 스프링에서 응답 객체로 내려줄 때 변수가 없더라도 'formattedPrice'키로 정보를 내려준다. */
    @JsonProperty("formattedPrice")
    public String formattedPrice() {
        //return new DecimalFormat("#,##0").format(this.price);
        // 생성한 유틸 클래스를 활용하자.
        return numberFormat(this.price);
    }

    /**
     *
     * 이에 대한 다양한 방식은 다음과 같이도 가능하다.
     *
     * private String formattedPrice;
     *
     * public void setPrice(long price) {
     *     this.price = price;
     *     this.formattedPrice = new DecimalFormat("#,##0").format(price);
     * }
     *
     *
     */

}


