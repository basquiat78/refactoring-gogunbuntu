package io.basquiat.start;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

/**
 * 제너릭하게 처리한 클래스
 * created by basquiat
 */
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public final class GenericNumberCompare<T extends Number & Comparable<T>> {

    /** flag constant */
    private static final int ZERO = 0;

    private final T source;

    /**
     * GenericNumberCompare 로 감싸기 위한 정적 메소드
     * @param source
     * @return GenericNumberCompare<T>
     */
    @SuppressWarnings("unchecked")
    public static <T extends Number & Comparable<T>> GenericNumberCompare<T> is(T source) {
        return new GenericNumberCompare(source);
    }

    /**
     * this == value
     * @param value
     * @return boolean
     */
    public boolean eq(T value) {
        return source.compareTo(value) == ZERO;
    }

    /**
     * Greater Than
     * this > value
     * @param value
     * @return boolean
     */
    public boolean gt(T value) {
        return source.compareTo(value) > ZERO;
    }

    /**
     * Greater Than or Equal
     * this >= value
     * @param value
     * @return boolean
     */
    public boolean gte(T value) {
        return source.compareTo(value) >= ZERO;
    }

    /**
     * Less Than
     * this < value
     * @param value
     * @return boolean
     */
    public boolean lt(T value) {
        return source.compareTo(value) < ZERO;
    }

    /**
     * Less Than or Equal
     * this <= value
     * @param value
     * @return boolean
     */
    public boolean lte(T value) {
        return source.compareTo(value) <= ZERO;
    }

}
