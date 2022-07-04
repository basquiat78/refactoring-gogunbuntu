package io.basquiat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.BigInteger;

import static io.basquiat.start.BigDecimalCompare.bd;
import static io.basquiat.start.BigIntegerCompare.bi;
import static io.basquiat.start.GenericNumberCompare.is;
import static io.basquiat.util.PreciseNumberUtils.*;

/**
 * 이커머스에 들어가면서도 이 두개를 사용할 일이 많았는데 이전에 상속받아 사용했던 그 방식을 버리고 나름 컴포지션 방식으로 바꿔서 사용하기 시작.
 *
 * 이 방식이 좋은 이유는 부모 클래스로부터 편의를 위해 부모 클래스의 메소드를 재정의할 수 없다는 점과 원하는 목적에 맞는 클래스로서의 역할로 제한할 수 있다는 점.
 *
 */
class B_StartTest {

    @Test
    @DisplayName("BigDecimal 비교: BigDecimal 비교를 좀 더 우아하게")
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
    @DisplayName("BigInteger 비교: BigInteger 비교를 좀 더 우아하게")
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

    @Test
    @DisplayName("제너릭할 수 있을까?")
    void GENERIC_TEST() {

        //BigDecimal before = BigDecimal.ONE;
        //BigDecimal after = BigDecimal.TEN.negate();

        BigInteger before = BigInteger.valueOf(100);
        BigInteger after = BigInteger.valueOf(98);

        //int before = 1;
        //int after = -10;

        //Integer before = 10;
        //Integer after = 10;

        //long before = 1;
        //long after = -10;

        //Long before = 10L;
        //Long after = 10L;

        //double before = 100; // 10
        //double after = 100;

        //String before = "1";
        //String after = "-10";

        System.out.println("========= UtilClass에 ============");
        System.out.println(eq(before, after));
        System.out.println(gt(before, after));
        System.out.println(gte(before, after));
        System.out.println(lt(before, after));
        System.out.println(lte(before, after));

        System.out.println("========= 제너릭하게 ============");
        System.out.println(is(before).eq(after));
        System.out.println(is(before).gt(after));
        System.out.println(is(before).gte(after));
        System.out.println(is(before).lt(after));
        System.out.println(is(before).lte(after));

    }

}
