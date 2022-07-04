package io.basquiat.last;

import io.basquiat.last.common.NumberCompare;

/**
 * Double 타입을 비교하기 위한 클래스
 * created by basquiat
 */
public final class DoubleCompare extends NumberCompare<Double> {

    /**
     * constructor
     * @param source
     * @return NumberCompare<T>
     */
    DoubleCompare(Double source) {
        super(source);
    }

    /**
     * DoubleCompare 로 감싸기 위한 정적 메소드
     * @param source
     * @return DoubleCompare
     */
    public static DoubleCompare db(Double source) {
        return new DoubleCompare(source);
    }

}
