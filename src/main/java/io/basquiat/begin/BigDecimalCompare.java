package io.basquiat.begin;

import java.math.BigDecimal;

import static io.basquiat.util.PreciseNumberUtils.toBigDecimal;

/**
 * BigDecimal을 상속한 클래스
 * created by basquiat
 */
public final class BigDecimalCompare extends BigDecimal {

    /** flag constant */
    private static final int ZERO = 0;

    BigDecimalCompare(String val) {
        super(val);
    }

    /**
     * BigDecimalCompare 로 감싸기 위한 정적 메소드
     * @param source
     * @return BigDecimalCompare
     */
    public static BigDecimalCompare bd(BigDecimal source) {
        return new BigDecimalCompare(source.toString());
    }

    /**
     * this == value
     * @param value
     * @return boolean
     */
    public boolean eq(Object value) {
        return this.compareTo(toBigDecimal(value)) == ZERO;
    }

    /**
     * greater than
     * this > value
     * @param value
     * @return boolean
     */
    public boolean gt(Object value) {
        return this.compareTo(toBigDecimal(value)) > ZERO;
    }

    /**
     * greater than or equal
     * this >= value
     * @param value
     * @return boolean
     */
    public boolean gte(Object value) {
        return this.compareTo(toBigDecimal(value)) >= ZERO;
    }

    /**
     * less than
     * this < value
     * @param value
     * @return boolean
     */
    public boolean lt(Object value) {
        return this.compareTo(toBigDecimal(value)) < ZERO;
    }

    /**
     * less than or equal
     * this <= value
     * @param value
     * @return boolean
     */
    public boolean lte(Object value) {
        return this.compareTo(toBigDecimal(value)) <= ZERO;
    }

}
