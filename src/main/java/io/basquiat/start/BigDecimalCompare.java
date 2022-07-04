package io.basquiat.start;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;

import static io.basquiat.util.PreciseNumberUtils.toBigDecimal;

/**
 * 컴포지션으로 처리한 클래스
 * created by basquiat
 */
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public final class BigDecimalCompare {

    /** flag constant */
    private static final int ZERO = 0;

    private final BigDecimal bigDecimal;

    /**
     * BigDecimalCompare 로 감싸기 위한 정적 메소드
     * @param source
     * @return BigDecimalCompare
     */
    public static BigDecimalCompare bd(BigDecimal source) {
        return new BigDecimalCompare(source);
    }

    /**
     * this == value
     * @param value
     * @return boolean
     */
    public boolean eq(Object value) {
        return bigDecimal.compareTo(toBigDecimal(value)) == ZERO;
    }

    /**
     * Greater Than
     * this > value
     * @param value
     * @return boolean
     */
    public boolean gt(Object value) {
        return bigDecimal.compareTo(toBigDecimal(value)) > ZERO;
    }

    /**
     * Greater Than or Equal
     * this >= value
     * @param value
     * @return boolean
     */
    public boolean gte(Object value) {
        return bigDecimal.compareTo(toBigDecimal(value)) >= ZERO;
    }

    /**
     * Less Than
     * this < value
     * @param value
     * @return boolean
     */
    public boolean lt(Object value) {
        return bigDecimal.compareTo(toBigDecimal(value)) < ZERO;
    }

    /**
     * Less Than or Equal
     * this <= value
     * @param value
     * @return boolean
     */
    public boolean lte(Object value) {
        return bigDecimal.compareTo(toBigDecimal(value)) <= ZERO;
    }

}
