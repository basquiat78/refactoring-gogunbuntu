package io.basquiat.last;

import io.basquiat.last.common.NumberCompare;

/**
 * Integer 타입을 비교하기 위한 클래스
 * created by basquiat
 */
public final class IntegerCompare extends NumberCompare<Integer> {

    /**
     * constructor
     * @param source
     * @return NumberCompare<T>
     */
    IntegerCompare(Integer source) {
        super(source);
    }

    /**
     * IntegerCompare 로 감싸기 위한 정적 메소드
     * @param source
     * @return IntegerCompare
     */
    public static IntegerCompare it(Integer source) {
        return new IntegerCompare(source);
    }

}
