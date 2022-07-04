package io.basquiat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.BigInteger;

import static io.basquiat.begin.BigDecimalCompare.bd;
import static io.basquiat.begin.BigIntegerCompare.bi;

/**
 * 처음 블록체인 회사에서 일 할 때 BigDecimal과 BigInteger의 비교가 너무 짜증나서 찾아보다가
 * 이 두개를 상속할 수 있다는 것을 알게 되면서 상속받아서 일종의 래퍼 클래스를 만들어 사용했다.
 */
class A_BeginTest {

    @Test
    @DisplayName("BigDecimal 비교: 태초에는 BigDecimal을 상속받아서 사용했다.")
    void BigDecimal_Compare() {
        BigDecimal before = BigDecimal.TEN;
        BigDecimal after = BigDecimal.TEN;

        System.out.println("======= 기존 방식 ========");
        System.out.println(before.compareTo(after) == 0); // before와 after가 같니?
        System.out.println(before.compareTo(after) > 0);  // before가 after보다 크니?
        System.out.println(before.compareTo(after) >= 0);  // before가 after보다 크거나 같니?
        System.out.println(before.compareTo(after) < 0);  // before가 after보다 작니?
        System.out.println(before.compareTo(after) <= 0);  // before가 after보다 작거나 같니?

        System.out.println("========= 새로운 방식 ============");
        System.out.println(bd(before).eq(after));
        System.out.println(bd(before).gt(after));
        System.out.println(bd(before).gte(after));
        System.out.println(bd(before).lt(after));
        System.out.println(bd(before).lte(after));

    }

    @Test
    @DisplayName("BigInteger 비교: 태초에는 BigInteger을 상속받아서 사용했다.")
    void BigInteger_Compare() {
        BigInteger before = BigInteger.TEN;
        BigInteger after = BigInteger.TEN;

        System.out.println("======= 기존 방식 ========");
        System.out.println(before.compareTo(after) == 0); // before와 after가 같니?
        System.out.println(before.compareTo(after) > 0);  // before가 after보다 크니?
        System.out.println(before.compareTo(after) >= 0);  // before가 after보다 크거나 같니?
        System.out.println(before.compareTo(after) < 0);  // before가 after보다 작니?
        System.out.println(before.compareTo(after) <= 0);  // before가 after보다 작거나 같니?

        System.out.println("========= 새로운 방식 ============");
        System.out.println(bi(before).eq(after));
        System.out.println(bi(before).gt(after));
        System.out.println(bi(before).gte(after));
        System.out.println(bi(before).lt(after));
        System.out.println(bi(before).lte(after));

    }

}
