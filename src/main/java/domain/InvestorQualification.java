package domain;

import lombok.Getter;
import lombok.ToString;

import java.math.BigDecimal;

/**
 * 투자자의 등급에 따른 투자자 한도 정보를 담는 객체
 * 테스트를 위해 ToString를 달아놈
 */
@ToString(of = {"limit", "totalLimit", "remain"})
public class InvestorQualification {

    /** 투자자 한도 정보를 계산할 때 사용하는 어떤 임의의 값 */
    private static final BigDecimal SOME_FLAG = BigDecimal.valueOf(100);

    /**
     * 자격 enum 정의
     */
    private enum Qualification {
        BRONZE,
        SILVER,
        GOLD;
    }

    private final Qualification qualification;

    /** 건당 투자한도  금액 */
    @Getter
    private BigDecimal limit;
    /** 전체 투자 금액 */
    @Getter
    private BigDecimal totalLimit;
    /** 투자 가능한 남은 금액 */
    @Getter
    private BigDecimal remain;

    /**
     * 생성자 등급과 해당 투자자가 지금까지 투자한 금액 정보를 받는다.
     * @param qualification
     * @param current
     */
    public InvestorQualification(String qualification, BigDecimal current) {
        this.qualification = Qualification.valueOf(qualification.toUpperCase());
        this.calculate(current);
    }

    /**
     * 대충 이런 식으로 계산해서 세팅한다.
     */
    private void calculate(BigDecimal current) {
        switch(qualification) {
            case BRONZE:
                this.limit = SOME_FLAG.divide(BigDecimal.TEN).add(BigDecimal.valueOf(10_000));
                this.totalLimit = SOME_FLAG.divide(BigDecimal.TEN).add(BigDecimal.valueOf(100_000_000));
                this.remain = this.totalLimit.subtract(current);
                break;
            case SILVER:
                this.limit = SOME_FLAG.divide(BigDecimal.ONE).add(BigDecimal.valueOf(1_000_000));
                this.totalLimit = SOME_FLAG.divide(BigDecimal.ONE).add(BigDecimal.valueOf(10_000_000_000L));
                this.remain = this.totalLimit.subtract(current);
                break;
            case GOLD:
                this.limit = SOME_FLAG.multiply(BigDecimal.valueOf(100_000_000));
                this.totalLimit = SOME_FLAG.multiply(BigDecimal.valueOf(1_000_000_000_000L));
                this.remain = this.totalLimit.subtract(current);
                break;
            default:
                this.limit = BigDecimal.ZERO;
                this.totalLimit = BigDecimal.ZERO;
                this.remain = BigDecimal.ZERO;
        }

    }

}
