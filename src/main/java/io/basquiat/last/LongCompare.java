package io.basquiat.last;

import io.basquiat.last.common.NumberCompare;

/**
 * Long 타입을 비교하기 위한 클래스
 * created by basquiat
 */
public final class LongCompare extends NumberCompare<Long> {

    /**
     * constructor
     * @param source
     * @return NumberCompare<T>
     */
    LongCompare(Long source) {
        super(source);
    }

    /**
     * LongCompare 로 감싸기 위한 정적 메소드
     * @param source
     * @return LongCompare
     */
    public static LongCompare ll(Long source) {
        return new LongCompare(source);
    }

}
