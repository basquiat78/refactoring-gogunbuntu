package io.basquiat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static io.basquiat.last.BigDecimalCompare.bd;
import static io.basquiat.last.LongCompare.ll;

/**
 * 내가 생각 한건 바로 이전 방식까지였다.
 *
 * 하지만 이번 회사에서 주니어분들의 생각이 어디까지 그리고 개발자로서 더 편한 것이 있는지 관찰하고 생각을 확장하는 것을 보고 오히려 배움
 *
 * 결국 제너릭을 통해서 [이펙티브 자바]에서 소개했던 이부분의 컴포지션과 재사용가능한 전달 클래스로 교체.
 *
 * 따라서 BigDecimalCompare와 BigIntegerCompare에 반복되는 코드가 싹 빠져버림.
 *
 * 또한 다른 래퍼 클래스의 비교기를 만들기도 수월해 짐.
 *
 * 다만 쓴다 해도 float과 double이외에는 거의 쓸일이 없을듯.
 */
class C_LastTest {

    @Test
    @DisplayName("컴포지션과 재사용 가능한 전달 클래스로 더 좋게 작성")
    void BigDecimal_Compare() {

        System.out.println("========= BigDecimal ============");
        BigDecimal before = BigDecimal.TEN;
        BigDecimal after = BigDecimal.TEN;
        System.out.println(bd(before).eq(after));
        System.out.println(bd(before).gt(after));
        System.out.println(bd(before).gte(after));
        System.out.println(bd(before).lt(after));
        System.out.println(bd(before).lte(after));

        System.out.println("========= long auto-unboxing/auto-boxing ============");
        long first = 1000;
        Long target = 1000L;
        System.out.println(target == first);
        System.out.println(ll(first).eq(target));
        System.out.println(ll(first).gt(target));
        System.out.println(ll(first).gte(target));
        System.out.println(ll(first).lt(target));
        System.out.println(ll(first).lte(target));
        Long a = 1000L;
        Long b = 1000L;
        System.out.println(a == b); // true? but false => two boxed instances having the same primitive Value but not the same reference so is result is false
        System.out.println(ll(a).eq(b));
    }


}
