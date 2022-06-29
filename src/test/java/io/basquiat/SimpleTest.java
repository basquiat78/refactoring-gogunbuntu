package io.basquiat;

import io.basquiat.domain.entity.AnalysisResult;
import io.basquiat.domain.entity.CardItemPaymentDto;
import io.basquiat.domain.entity.CardPaymentDto;
import io.basquiat.domain.entity.StatisticsCardPayment;
import io.basquiat.util.LocalDateTimeForm;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Predicate;

import static java.time.LocalDateTime.now;
import static java.util.stream.Collectors.toList;

class SimpleTest {

    @Test
    @DisplayName("JUST_DO_CODING: 데이터 생성하고 테스트 해보기")
    void JUST_DO_CODING() {

        final String[] cards = {"국민", "신한", "농협"};

        LocalDateTime now = now();

        List<StatisticsCardPayment> statisticsCardPayments = new ArrayList<>();
        StatisticsCardPayment statisticsCardPayment1 = StatisticsCardPayment.builder()
                                                                            .card(cards[0])
                                                                            .itemCode("ic_0001")
                                                                            .itemName("상품명[ic_0001]")
                                                                            .price(100000)
                                                                            .paymentDate(now)
                                                                            .build();
        statisticsCardPayments.add(statisticsCardPayment1);
        StatisticsCardPayment statisticsCardPayment2 = StatisticsCardPayment.builder()
                                                                            .card(cards[1])
                                                                            .itemCode("ic_0002")
                                                                            .itemName("상품명[ic_0002]")
                                                                            .price(120000)
                                                                            .paymentDate(now.minusDays(1))
                                                                            .build();
        statisticsCardPayments.add(statisticsCardPayment2);
        StatisticsCardPayment statisticsCardPayment3 = StatisticsCardPayment.builder()
                                                                            .card(cards[0])
                                                                            .itemCode("ic_0001")
                                                                            .itemName("특사 상품명[ic_0001]")
                                                                            .price(80000)
                                                                            .paymentDate(now)
                                                                            .build();
        statisticsCardPayments.add(statisticsCardPayment3);
        StatisticsCardPayment statisticsCardPayment4 = StatisticsCardPayment.builder()
                                                                            .card(cards[2])
                                                                            .itemCode("ic_0003")
                                                                            .itemName("상품명[ic_0003]")
                                                                            .price(120000)
                                                                            .paymentDate(now.minusDays(4))
                                                                            .build();
        statisticsCardPayments.add(statisticsCardPayment4);
        StatisticsCardPayment statisticsCardPayment5 = StatisticsCardPayment.builder()
                                                                            .card(cards[2])
                                                                            .itemCode("ic_0001")
                                                                            .itemName("상품명[ic_0001]")
                                                                            .price(100000)
                                                                            .paymentDate(now.minusDays(4))
                                                                            .build();
        statisticsCardPayments.add(statisticsCardPayment5);

        final Map<CardItemPaymentDto, List<StatisticsCardPayment>> resultMap = new HashMap<>();

        statisticsCardPayments.stream()
                              .forEach(entity -> {
                                  CardItemPaymentDto key = CardItemPaymentDto.builder()
                                                                             .card(entity.getCard())
                                                                             .itemCode(entity.getItemCode())
                                                                             .paymentDate(LocalDateTimeForm.SIMPLE_YMD.transform(entity.getPaymentDate()))
                                                                             .build();
                                  List<StatisticsCardPayment> values = resultMap.get(key);
                                  if(values == null) {
                                      // 없으면 리스트로 넣자.
                                      resultMap.put(key, new ArrayList<>(Collections.singletonList(entity)));
                                  } else {
                                      // 있으면 기존의 리스트에 추가
                                      values.add(entity);
                                  }
                              });

        System.out.print(resultMap.toString());

        List<AnalysisResult> results = resultMap.entrySet()
                                                .stream()
                                                .map(entry -> AnalysisResult.builder()
                                                                            .cardItemPayment(entry.getKey())
                                                                            .cardPayments(entry.getValue()
                                                                                               .stream()
                                                                                               .map(CardPaymentDto::entityToDto)
                                                                                               .collect(toList()))
                                                                            .build())
                                                .collect(toList());
        System.out.println(results.toString());

        // 특정 조건으로 조회하기
        String targetCard = cards[0];
        String targetItemCode = "ic_0001";
        Predicate<AnalysisResult> wantToFind = analysisResult -> analysisResult.wantToFind(targetCard, targetItemCode, LocalDateTimeForm.SIMPLE_YMD.transform(now));

        AnalysisResult analysisResult = results.stream()
                                               .filter(wantToFind::test)
                                               .findFirst()
                                               .orElseGet(null); // 없으면 null 반환

        System.out.println(analysisResult.toString());
        System.out.println(analysisResult.getCardPayments().toString());

        // 스트림에서 바로 원하는 조건의 cardPayments list를 가져오자.
        List<CardPaymentDto> cardPayments = results.stream()
                                                   .filter(wantToFind::test)
                                                   // 그냥 한줄로 처리
                                                   //.map(AnalysisResult::getCardPayments)
                                                   //.flatMap(cardPaymentDtos -> cardPaymentDtos.stream())
                                                   .flatMap(dto -> dto.getCardPayments().stream())
                                                   .collect(toList());

        System.out.println(cardPayments.toString());
    }

}
