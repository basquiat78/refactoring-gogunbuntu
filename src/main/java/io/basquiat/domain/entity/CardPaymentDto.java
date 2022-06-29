package io.basquiat.domain.entity;

import lombok.*;

import java.time.LocalDateTime;

/**
 * StatisticsCardPayment > dto
 * 이름이 길어서 줄여버림
 */
@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CardPaymentDto {

    private String card;

    private String itemCode;

    private String itemName;

    private long price;

    private LocalDateTime paymentDate;

    @Builder
    public CardPaymentDto(@NonNull String card, @NonNull String itemCode, @NonNull String itemName, long price, @NonNull LocalDateTime paymentDate) {
        this.card = card;
        this.itemCode = itemCode;
        this.itemName = itemName;
        this.price = price;
        this.paymentDate = paymentDate;
    }

    public static CardPaymentDto entityToDto(StatisticsCardPayment entity) {
        return CardPaymentDto.builder()
                             .card(entity.getCard())
                             .itemCode(entity.getItemCode())
                             .itemName(entity.getItemName())
                             .paymentDate(entity.getPaymentDate())
                             .build();
    }

}
