package io.basquiat.domain.entity;

import lombok.*;

/**
 * Map의 키값으로 사용하기 위한 dto
 */
@Getter
@ToString
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CardItemPaymentDto {

    private String card;

    private String itemCode;

    private String paymentDate;

    @Builder
    public CardItemPaymentDto(@NonNull String card, @NonNull String itemCode, @NonNull String paymentDate) {
        this.card = card;
        this.itemCode = itemCode;
        this.paymentDate = paymentDate;
    }

}
