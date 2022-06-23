import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import util.LocalDateTimeForm;

import java.time.LocalDateTime;

import static java.time.LocalDateTime.now;

class UtilClassTest {

    @Test
    @DisplayName("LocalDateTimeForm을 테스트 해보자.")
    void STEP1() {

        /** LocalDateTimeForm 이름이 길다면 DateTimeForm이나 간지나는 이름이 있다면 추천 부탁드린다! */
        System.out.println(LocalDateTimeForm.STANDARD.transform(now()));
        System.out.println(LocalDateTimeForm.YMD.transform(now()));
        System.out.println(LocalDateTimeForm.YMD_WITH_DOT.transform(now()));
        System.out.println(LocalDateTimeForm.YYYY_MM_KO.transform(now()));
        // 오전 오후 표기
        System.out.println(LocalDateTimeForm.AMPM_MARK.transform(now()));


    }


}
