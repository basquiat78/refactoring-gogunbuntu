package io.basquiat.domain.entity;

import lombok.*;

import java.util.Collections;
import java.util.List;

/**
 * 맵의 키와 밸류를 담는 일종의 이름을 막지은 분석 결과 객체
 */
@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AnalysisResult {

    private CardItemPaymentDto cardItemPayment;

    private List<CardPaymentDto> cardPayments;

    @Builder
    public AnalysisResult(@NonNull CardItemPaymentDto cardItemPayment, List<CardPaymentDto> cardPayments) {
        this.cardItemPayment = cardItemPayment;
        this.cardPayments = cardPayments;
    }

    public boolean wantToFind(@NonNull String card, @NonNull String itemCode, @NonNull String paymentDate) {
        if(card.equals(this.cardItemPayment.getCard()) && itemCode.equals(this.cardItemPayment.getItemCode()) && paymentDate.equals(this.cardItemPayment.getPaymentDate())) {
            return true;
        }
        return false;
    }

}
