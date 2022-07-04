package io.basquiat.last;

import io.basquiat.last.common.NumberCompare;

/**
 * Float 타입을 비교하기 위한 클래스
 * created by basquiat
 */
public final class FloatCompare extends NumberCompare<Float> {

    /**
     * constructor
     * @param source
     * @return NumberCompare<T>
     */
    FloatCompare(Float source) {
        super(source);
    }

    /**
     * FloatCompare 로 감싸기 위한 정적 메소드
     * @param source
     * @return FloatCompare
     */
    public static FloatCompare fl(Float source) {
        return new FloatCompare(source);
    }

}
