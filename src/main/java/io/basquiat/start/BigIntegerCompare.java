package io.basquiat.start;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

import java.math.BigInteger;

import static io.basquiat.util.PreciseNumberUtils.toBigInteger;

/**
 * 컴포지션으로 처리한 클래스
 * created by basquiat
 */
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public final class BigIntegerCompare {

    /** flag constant */
    private static final int ZERO = 0;

    private final BigInteger bigInteger;

    /**
     * BigIntegerCompare 로 감싸기 위한 정적 메소드
     * @param source
     * @return BigIntegerCompare
     */
    public static BigIntegerCompare bi(BigInteger source) {
        return new BigIntegerCompare(source);
    }

    /**
     * this == value
     * @param value
     * @return boolean
     */
    public boolean eq(Object value) {
        return bigInteger.compareTo(toBigInteger(value)) == ZERO;
    }

    /**
     * Greater Than
     * this > value
     * @param value
     * @return boolean
     */
    public boolean gt(Object value) {
        return bigInteger.compareTo(toBigInteger(value)) > ZERO;
    }

    /**
     * Greater Than or Equal
     * this >= value
     * @param value
     * @return boolean
     */
    public boolean gte(Object value) {
        return bigInteger.compareTo(toBigInteger(value)) >= ZERO;
    }

    /**
     * less than
     * this < value
     * @param value
     * @return boolean
     */
    public boolean lt(Object value) {
        return bigInteger.compareTo(toBigInteger(value)) < ZERO;
    }

    /**
     * less than or equal
     * this <= value
     * @param value
     * @return boolean
     */
    public boolean lte(Object value) {
        return bigInteger.compareTo(toBigInteger(value)) <= ZERO;
    }

}
