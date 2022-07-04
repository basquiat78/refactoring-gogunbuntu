package io.basquiat.last;

import io.basquiat.last.common.NumberCompare;

import java.math.BigInteger;

/**
 * BigInteger 타입을 비교하기 위한 클래스
 * created by basquiat
 */
public final class BigIntegerCompare extends NumberCompare<BigInteger> {

    /**
     * constructor
     * @param source
     * @return NumberCompare<T>
     */
    BigIntegerCompare(BigInteger source) {
        super(source);
    }

    /**
     * BigIntegerCompare 로 감싸기 위한 정적 메소드
     * @param source
     * @return BigIntegerCompare
     */
    public static BigIntegerCompare bi(BigInteger source) {
        return new BigIntegerCompare(source);
    }

}
