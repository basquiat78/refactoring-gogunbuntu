package io.basquiat.last;

import io.basquiat.last.common.NumberCompare;

import java.math.BigDecimal;

/**
 * BigDecimal 타입을 비교하기 위한 클래스
 * created by basquiat
 */
public final class BigDecimalCompare extends NumberCompare<BigDecimal> {

    /**
     * constructor
     * @param source
     * @return NumberCompare<T>
     */
    BigDecimalCompare(BigDecimal source) {
        super(source);
    }

    /**
     * BigDecimalCompare 로 감싸기 위한 정적 메소드
     * @param source
     * @return BigDecimalCompare
     */
    public static BigDecimalCompare bd(BigDecimal source) {
        return new BigDecimalCompare(source);
    }

}
