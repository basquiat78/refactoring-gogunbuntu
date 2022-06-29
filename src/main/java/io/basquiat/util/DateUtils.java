package io.basquiat.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

/**
 * date utils
 * created by basquiat
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class DateUtils {

    /**
     * pattern에 맞춰 LocalDateTime -> String 형식으로 변환
     * @param localDateTime
     * @param pattern
     * @return
     */
    public static String localDateTimeToStringWithPattern(LocalDateTime localDateTime, String pattern) {
        try {
            return localDateTime.format(DateTimeFormatter.ofPattern(pattern).withLocale(new Locale("ko")));
        } catch (Exception e) {
            return null;
        }
    }

}