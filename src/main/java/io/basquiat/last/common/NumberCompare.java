package io.basquiat.last.common;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

/**
 * 제너릭하게 처리한 클래스
 * created by basquiat
 */
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class NumberCompare<T extends Number & Comparable<T>> {

    /** flag constant */
    private static final int ZERO = 0;

    private final T source;

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
