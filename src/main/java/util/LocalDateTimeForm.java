package util;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

import static util.DateUtils.localDateTimeToStringWithPattern;

/**
 * LocalDateTimeForm pattern
 * created by basquiat
 */
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public enum LocalDateTimeForm {

    STANDARD("yyyy-MM-dd HH:mm:ssS"),

    YMD("yyyyMMdd"),
    YMD_WITH_H_M("yyyyMMdd HH:mm"),
    YMD_WITH_STAMP("yyyyMMdd HH:mm:ss"),

    YMD_WITH_DOT("yyyy.MM.dd"),
    YMD_WITH_DOT_H_M("yyyy.MM.dd HH:mm"),
    YMD_WITH_DOT_STAMP("yyyy.MM.dd HH:mm:ss"),

    YMD_WITH_SPLASH("yyyy/MM/dd"),

    YYYY_M_KO("yyyy년 M월"),
    YYYY_MM_KO("yyyy년 MM월"),

    AMPM_MARK("a yyyy년 M월 d일 K:mm:ss");

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
