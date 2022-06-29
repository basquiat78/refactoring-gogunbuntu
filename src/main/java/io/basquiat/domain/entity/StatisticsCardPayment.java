package io.basquiat.domain.entity;

import lombok.*;

import java.time.LocalDateTime;

/**
 * 최대한 간결하게 일별로 상품에 대해 어떤 카드를 썼는지 담는 객체
 */
@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class StatisticsCardPayment {

    private String card;

    private String itemCode;

    private String itemName;

    private long price;

    private LocalDateTime paymentDate;

    @Builder
    public StatisticsCardPayment(@NonNull String card, @NonNull String itemCode, @NonNull String itemName, long price, @NonNull LocalDateTime paymentDate) {
        this.card = card;
        this.itemCode = itemCode;
        this.itemName = itemName;
        this.price = price;
        this.paymentDate = paymentDate;
    }

}
