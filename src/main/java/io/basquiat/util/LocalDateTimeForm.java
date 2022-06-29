package io.basquiat.util;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

import static io.basquiat.util.DateUtils.localDateTimeToStringWithPattern;

/**
 * LocalDateTimeForm pattern
 * created by basquiat
 */
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public enum LocalDateTimeForm {

    SIMPLE_YMD("yyyy-MM-dd");

    @Getter
    private String pattern;

    /**
     * enum의 패턴에 맞춰서 LocalDateTime을 스트링으로 변환한다.
     * @param localDateTime
     * @return String
     */
    public String transform(LocalDateTime localDateTime) {
        return localDateTimeToStringWithPattern(localDateTime, this.pattern);
    }

}
