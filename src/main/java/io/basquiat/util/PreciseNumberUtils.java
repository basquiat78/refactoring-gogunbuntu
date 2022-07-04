package io.basquiat.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * BigDecimal or BigInteger Util
 * created by basquiat
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PreciseNumberUtils {

	/** flag constant */
	private static final int ZERO = 0;

	// 이렇게도 가능하다.
	public static <T extends Number & Comparable<T>> boolean eq(Comparable<T> source, T target) {
		return source.compareTo(target) == ZERO;
	}

	public static <T extends Number & Comparable<T>> boolean gt(Comparable<T> source, T target) {
		return source.compareTo(target) > ZERO;
	}

	public static <T extends Number & Comparable<T>> boolean gte(Comparable<T> source, T target) {
		return source.compareTo(target) >= ZERO;
	}

	public static <T extends Number & Comparable<T>> boolean lt(Comparable<T> source, T target) {
		return source.compareTo(target) < ZERO;
	}

	public static <T extends Number & Comparable<T>> boolean lte(Comparable<T> source, T target) {
		return source.compareTo(target) <= ZERO;
	}

	/**
	 * object로 받고 타입 체크해서 처리하자
	 * 내부적으로 정적 팩토리와 캐싱으로 처리하는 valueOf를 써야 하나
	 * BigInteger나 String으로 들어온 경우에는 고려를 해봐야 한다.
	 * 만일 스트링으로 "99999999999999999900000000"가 들어온다면 long이나 double로 캐스팅순간에 오류가 난다.
	 * @param value
	 * @return BigDecimal
	 */
	public static BigDecimal toBigDecimal(Object value) {
		if(value == null) {
			return BigDecimal.ZERO;
		}
		if(value instanceof BigInteger) {
			return new BigDecimal((BigInteger) value);
		}
		if(value instanceof Double) {
			return BigDecimal.valueOf((Double) value);
		}
		if(value instanceof Long) {
			return BigDecimal.valueOf((Long) value);
		}
		if(value instanceof Integer) {
			return BigDecimal.valueOf(((Integer) value).longValue());
		}
		return new BigDecimal(value.toString());
	}

	/**
	 * object로 받고 타입 체크해서 처리하자
	 * 스트링인 경우에는 BigDecimal처럼 고려해봐야 한다.
	 * @param value
	 * @return BigInteger
	 */
	public static BigInteger toBigInteger(Object value) {
		if(value == null) {
			return BigInteger.ZERO;
		}
		if(value instanceof BigDecimal) {
			return ((BigDecimal) value).toBigInteger();
		}
		if(value instanceof Long) {
			return BigInteger.valueOf((Long) value);
		}
		if(value instanceof Integer) {
			return BigInteger.valueOf(((Integer) value).longValue());
		}
		return new BigDecimal(value.toString()).toBigInteger();
	}

}