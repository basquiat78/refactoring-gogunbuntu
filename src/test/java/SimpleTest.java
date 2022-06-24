import domain.InvestorQualification;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

class SimpleTest {

    /**
     * 맨 처음 작성되었던 InvestorQualification를 한번 확인차 확인해 본다.
     * 뭐 잘 돌아갔으니 문제가 없었을 코드일 것이다.
     *
     */
    @Test
    @DisplayName("STEP1: 이렇게도 가능하다. 주니어 개발자분의 고민이 엿보이는 코드 구성")
    void STEP1() {
        BigDecimal current = BigDecimal.valueOf(10_000);
        InvestorQualification bronze = new InvestorQualification("bronze", current);
        System.out.println(bronze.toString());
        InvestorQualification silver = new InvestorQualification("silver", current);
        System.out.println(silver.toString());
        InvestorQualification gold = new InvestorQualification("gold", current);
        System.out.println(gold.toString());
    }

}
