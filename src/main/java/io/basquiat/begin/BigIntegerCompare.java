package io.basquiat.begin;

import java.math.BigInteger;

import static io.basquiat.util.PreciseNumberUtils.toBigInteger;

/**
 * BigInteger을 상속한 클래스
 * created by basquiat
 */
public final class BigIntegerCompare extends BigInteger {

    /** flag constant */
    private static final int ZERO = 0;

    BigIntegerCompare(String val) {
        super(val);
    }

    /**
     * BigIntegerCompare 로 감싸기 위한 정적 메소드
     * @param source
     * @return BigIntegerCompare
     */
    public static BigIntegerCompare bi(BigInteger source) {
        return new BigIntegerCompare(source.toString());
    }

    /**
     * this == value
     * @param value
     * @return boolean
     */
    public boolean eq(Object value) {
        return this.compareTo(toBigInteger(value)) == ZERO;
    }

    /**
     * greater than
     * this > value
     * @param value
     * @return boolean
     */
    public boolean gt(Object value) {
        return this.compareTo(toBigInteger(value)) > ZERO;
    }

    /**
     * greater than or equal
     * this >= value
     * @param value
     * @return boolean
     */
    public boolean gte(Object value) {
        return this.compareTo(toBigInteger(value)) >= ZERO;
    }

    /**
     * less than
     * this < value
     * @param value
     * @return boolean
     */
    public boolean lt(Object value) {
        return this.compareTo(toBigInteger(value)) < ZERO;
    }

    /**
     * less than or equal
     * this <= value
     * @param value
     * @return boolean
     */
    public boolean lte(Object value) {
        return this.compareTo(toBigInteger(value)) <= ZERO;
    }

}


