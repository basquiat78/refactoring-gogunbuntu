# refactoring-gogunbuntu
리팩토링 고군분투기

# enum을 활용하는 것이 이득이라면 적극적으로 활용해라

## 목적
주니어 개발자의 고민과 고군분투를 코드로 보면서 시니어 개발자 자신이 가지고 있는 지식을 전파해야하는 중요성을 느낀다.

일단 이전 브랜치의 마지막이 enum을 적극 활용하라인데 이 브랜치는 이것을 좀더 확장하는 것이 주요 목적이다.

실제로 몇 년전 우아한형제 테크 블로그에서 functional interface를 통해서 enum을 얼마나 우아하게 사용했는지에 대한 블로그가 있다.

이게 5년전 글이네??? 시간 빠르다.

(Java Enum 활용기)(https://techblog.woowahan.com/2527/)

~~자바 8 이전에는 이 방식을 선배 개발자분들이 좀 힘들게 사용했던 것을 본 기억이 난다. 형 지금은 너무 편해졌어!~~

아무튼 초기 지금 스타트업 회사에 들어왔을 때는 이전 시니어 분이 어느정도 틀을 만들어 논 상태였다.

그때 myBatis를 썼는데 문제는 각 엔티티의 정보들이 enum으로 정의해서 사용할 필요성이 있던 코드가 다수 존재 했다.

mySql에서는 컬럼 타입을 enum으로 정의할 수 있다. 그래서 create의 경우에는 정의된 코드가 들어오지 않으면 제약 조건에 의해 에러가 발생한다.

jpa를 어느정도 공부했던 이 주니어 개발자는 myBatis에서 enum의 경우에는 매핑이 되지 않는다는 것을 알고 좀 독특하게 처리했는데 myBatis에서는 이것을 해결하기 위해 TypeHandler를 제공하기 때문에 사실 이것을 정의하고 myBatis관련 config.xml에 정의를 하면 끝난다.

jpa의 Conveter처럼 xml mapper의 sql에 명시해서 사용할 수 있고 - 예를 들면 암복호화를 처리해야하는 경우 - 위에서처럼 글로벌하게 처리도 가능하다.

어째든 그렇다보니 jpa처럼 사용할 수 없어서 주니어 개발자는 '왜 jpa같은게 없는거야?'라고 욕을 하면서도 나름 멋진 방법으로 이 문제를 해결한거 같다.

근데 그 해결 방식이 어디서 많이 본 것이라 '이걸 어디서 봤더라?' 하고 생각하던 찰나에 조슈아 블로크의 [이펙티브 자바]에서 본 것이다.

책을 읽으면서 경험하지 못한 또는 저렇게 코드를 작성하는게 불편해 보이기 때문에 사실 공감할 수 없었던 내용이지만 실제로 주니어 개발자 입장에서 얼마나 고민했을까 하는 생각이 들었다.

이런 경험을 실제로 하다보니 그제서야 책의 내용이 공감가기 시작하더라.

챕터는 **태그 달린 클래스보다는 클래스 계층구조를 활용하라. page. 142** 참조.

### 이렇게 한 시나리오

사실 더 복잡하긴 하지만 최대한 단순화해서 예를 들어볼까 한다.

투자자의 등급에 따라서 해당 투자자가 한 건당 투자할 수 있는 금액 한도와 전체 투자할 수 있는 금액이 결정된다.

enum을 직접 매핑할 수 없으니 String으로 값을 가져와서 InvestorQualification라는 객체에 넣어서 계산을 하고 해당 객체를 들고 다니면서 어떤 비지니스 로직을 수행하고 있었다.

```java
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
```

이 코드가 잘 작동하기는 하는건지 일단 확인해 보자.

```java
class SimpleTest {

    /**
     * 맨 처음 작성되었던 InvestorQualification를 한번 확인차 확인해 본다.
     * 뭐 잘 돌아갔으니 문제가 없었을 코드일 것이다.
     *
     */
    @Test
    @DisplayName("STEP1: dto에 매핑하기 위해서는 이런 불편함이 있다.")
    void STEP1() {
        BigDecimal current = BigDecimal.valueOf(10_000);
        InvestorQualification bronze = new InvestorQualification("bronze", current);
        System.out.println(bronze.toString());
        InvestorQualification silver = new InvestorQualification("silver", current);
        System.out.println(silver.toString());
        InvestorQualification gold = new InvestorQualification("gold", current);
        System.out.println(gold.toString());
    }

}
```

결과는?

```
InvestorQualification(limit=10010, totalLimit=100000010, remain=99990010)
InvestorQualification(limit=1000100, totalLimit=10000000100, remain=9999990100)
InvestorQualification(limit=10000000000, totalLimit=100000000000000, remain=99999999990000)
```

흠 일단 뭐 원하는 결과가 나오는 거 같다.

거두절미하고 [이펙티브 자바]의 내용대로 클래스 계층구조를 활용해 적용하기에는 좀 무리가 있다.       

어떤 타입에 따라 반환하는 것이 아니고 디비로부터 넘어온 어떤 값 (그 값이 무엇이 되었든)

결국 그 정보는 InvestorQualification 으로 귀결되는 코드이다.

차라리 이럴거면..... 그냥 UtilClass로 정의하는 것이 좋았을 거 같은데?

하지만 이렇게 객체를 만들어 놓고 여기저기서 사용하고 있었던 터라 나름 이유있는 방식이다.

그리고 사실.... 뭐 리팩터링 해도 그렇게 크게 이득될 거 같진 않았지만 myBatis에서도 이것을 jpa처럼 사용할 수 있다는 것과 더불어 mySQL에 enum으로 정의할 때 소문자로 정의하면서 별어지는 몇 몇 갭을 좀 해결하고자 리팩토링한 케이스이다.

최종적으로는 우아한형제의 테크 블로그의 enum활용처럼 functional interface와 람다를 활용해 enum 클래스로 녹여서 해결하게 되었다.

### 진행하기전 번외편: 과거부터 전설처럼 내려온 관습적인 규약을 지켜야 할까? 이것도 code smell이라 할수 있을지?

통상적으로 static final처럼 상수로 사용하거나 할때 여러분들은 변수 명을 대문자로 설정한다.

뭐 대소문자로 작성해도 상관없지만 sonar lint같은 lint툴을 사용하면 고치라고 한다.

enum의 경우도 이와 비슷한 열거 타입이기 때문에 enum의 경우도 소문자로 작성하면 이 메세지를 볼 수 있다.

근데 왜?????          

자바 독을 보면 Enum Types에 대해 설명중 이런 글귀가 있다.

'Because they are constants, the names of an enum type's fields are in uppercase letters.'

어찌보면 이런 규약을 통해 "당신이 지금 바라보고 있는 '그' 변수는 상수다"라고 가시적으로 표현하는 것이 아닌가 생각이 든다.

의문을 가지지 말자. 저렇게 쓴 데에는 다 이유가 있다.

물론 자신이 아웃라이어라면? 소문자로 써도 무방하지 않을까?

지금까지 이게 문제가 되었던 적은 없었응께....

# 이것은 나의 고군분투기

내가 신규모듈로 만들었던 어플리케이션들은 현재 jpa로 구성되어 있다.

나는 enum도 클래스다라고 얘기를 해왔는데 결국 이것은 인터페이스를 구현할 수 있다는 것이다.

사실 지금까지 인터페이스를 구현한 enum을 사용할 일이 없었는데 위에 언급했던 mySql의 상태 정보를 enum으로 설정했는데 그 값들이 소문자였다는 것이다.

예를 들면 member테이블의 active라는 컬럼은 enum('active', 'inactive', 'blacklist', 'delete')처럼 정의를 해놨다.

처음에는 관습적으로 다음과 같이 enum 클래스를 작성한다.


```java
/**
 * active type enum class
 */
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public enum ActiveType {

    ACTIVE("활성"),
    INACTIVE("비활성"),
    BLACKLIST("블랙컨슈머"),
    DELETE("탈퇴");

    @Getter
    private String description;

}
```

하지만 이것을 사용하는 순간 seelct문이나 insert문에서 에러가 발생한다.       

mySql은 옵션에 따라 테이블명/컬럼의 대소문자 구분을 무시할 수 있거나 엄격하게 구분할 수 있다.       

하지만 컬럼 생성시 만일 enum으로 설정할 경우 enum에 정의 된 값은 대소문자를 구분하기 때문에 이런 제약이 걸려 에러가 발생하는 것이다.          

일단 울며겨자먹기로 소문자로 바꾼다.


```java
/**
 * active type enum class
 */
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public enum ActiveType {

    active("활성"),
    inactive("비활성"),
    blacklist("블랙컨슈머"),
    delete("탈퇴");

    @Getter
    private String description;

}
```
솔직히 문제없이 잘 돌아간다. 다만 lint툴을 사용한다면 보게 된 메세지는 눈감고 넘어가야 한다.

하지만 나의 경우에는 그러고 싶지 않았다. 그래서 DBA분과 이 이야기를 했는데 이제는 어쩔 수 없다고 한다.

DBA의 눈빛에서 감지할 수 있었던 것은 '왜 바꿔야 하지? 바꿔서 어떤 이득을 얻을 수 있는지 내게 설명해줘.'다.

개발자 기준에서 이것을 위와 같이 대문자로 해야하는 이유로 설득할 자신이 없었다.

~~물론 그분도 설득당하지 않을 자신이 있어 보였다. 의문의 패배~~

게다가 디폴트 값이 없어 null로 세팅되어 있다. 실제로 null인 경우에는 기본값을 active로 하라는 말만 들었다.

물론 코드 레벨에서 디폴트 값을 세팅해서 해주면 되지만 다른 어플리케이션에서 이 테이블의 정보를 생성할 때 null로 들어간 데이터가 너무 많았다.

고민하다가 결국에는 이것을 처리해줄 컨버터를 만들기로 했다.

처음에는 단순하게 생각해서 다음과 같이 컨버터를 하나 만들었다.

```java
/**
 * upper/lower case를 적용한다.
 */
@Converter
public class ActiveTypeLowerCaseConverter implements AttributeConverter<ActiveType, String> {

    /**
     * enum -> db
     * @param attribute
     * @return String
     */
    @Override
    public String convertToDatabaseColumn(ActiveType attribute) {
        // null이면 default 세팅해준다.
        if(attribute == null) {
            return ActiveType.ACTIVE.name().toLowerCase();
        }
        return attribute.name().toLowerCase();
    }

    /**
     * db -> enum
     * @param dbData
     * @return
     */
    @Override
    public ActiveType convertToEntityAttribute(String dbData) {
        if(isEmpty(dbData)) {
            return ActiveType.ACTIVE;
        }
        return ActiveType.valueOf(dbData.toUpperCase());
    }

}
```
속으로

'음 깔끔해! null인 경우도 다 처리했어'

하지만 생각해 보니 이게 한두개가 아니네??? 같은 코드로 생성될 컨버터의.. 아! 물론 어림도 없다.

결국 제네릭하게 처리하는 방식으로 진행해야 한다.

그렇다면 무엇이 필요할까?

해답은 역시 조슈아 블로크의 [이펙티브 자바]에서도 찾을 수 있다.

챕터는 **확장할 수 있는 열거 타입이 필요하면 인터페이스를 사용하라. page. 232** 참조

처음에는 이런 생각을 했다.

'일단은 대문자를 소문자로 처리하기 위함이니 LowerCaseEnumConverter를 하나 만들기로 한다.


```java
@Converter
@RequiredArgsConstructor
public class LowerCaseEnumConverter<T extends Enum<T>> implements AttributeConverter<T, String> {

    private final Class<T> clazz;

    /**
     * enum 상수 값을 가져와서 lowerCase로 반환한다.
     * @param attribute
     * @return String
     */
    @Override
    public String convertToDatabaseColumn(T attribute) {
        return attribute.name().toLowerCase();
    }

    /**
     * 디비 정보는 lowerCase로 upperCase로 변환후 비교후 해당 enum객체를 반환하게 한다.
     * @param dbData
     * @return T
     */
    @Override
    public T convertToEntityAttribute(String dbData) {
        T[] enums = clazz.getEnumConstants();
        return Arrays.stream(enums)
                .filter(en -> en.name().equals(dbData.toUpperCase()))
                .findFirst()
                .get();
    }

}

```
근데 이렇게 만들고 보니 문제가 하나 있다. null처리를 못한다는 것이다.

제너릭하게 처리할 수 있는 기반을 마련했지만 null인 경우 default값을 세팅할 수가 없었다.

그렇다고 일일이 instanceof로 비교해가며 해당 enum을 찾아서 처리한다?

~~아 물론 그렇게 할까? 라고 잠시 고민을 했다.~~

하지만 이것은 좀 뻘짓이다.

가능이야 하겠지.

각 enum마다 null이면 기본값으로 반환할 메소드를 같은 이름으로 만들어 놓고 위에 언급한 무식한 방법으로도 가능 할것이다.

instanceof로 비교후 해당되는 enum으로 캐스팅! 하고 메소드 호출하면 끗!        

아마두? 사실 무서워서 해보진 않았다.

[이펙티브 자바]에서 위에 언급했던 챕터를 보면 처음보는 신기한 표현을 보게 된다.

```java
private static <T extends Enum<T> & Operation> void test(Class<T> opEnumType, double x, double y) {
        for (Operation op : opEnumType.getEnumConstants())
        System.out.printf("%f %s %f = %f%n", x, op, y, op.apply(x, y));
        }
```

이거 처음 봤을 때 "우와 '&'를 통해서 제너릭과 인터페이스를 multiple하게 처리할 수 있다니....."

써본적이 없으니 마냥 신기.        

찾아보니 Bounded Type Parameters라고 한다.

[Generics Lesson](https://web.archive.org/web/20081217034134/http://java.sun.com/docs/books/tutorial/java/generics/index.html)

이와 관련 [이펙티브 자바]에서 관련 설명이 존재한다. 사실 말이 좀 어렵긴 하지만 어떻게 작동하는지 알게 되었다.

제네릭이 이렇게 멋지구나. 아 물론 어렵기도 하다.

자. 이제는 interface를 하나 만들어 볼 생각이다.

Null을 체크하기 위한 인터페이스를 하나 만들어 본다.

```java
/**
 * 만일 attribute가 null인 경우
 * 이것을 구현한 enum class에서 default값을 반환하게 만든다. 
 */
public interface EnumNullOperation<T> {
    T defaultIfNull();
}

/**
 * active type enum class
 */
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public enum ActiveType implements EnumNullOperation<ActiveType> {

    ACTIVE("활성"),
    INACTIVE("비활성"),
    BLACKLIST("블랙컨슈머"),
    DELETE("탈퇴");

    @Getter
    private String description;

    /**
     * null이라면 활성 상태의 enum을 반환한다.
     * @return ActiveType
     */
    @Override
    public ActiveType defaultIfNull() {
        return ActiveType.ACTIVE;
    }

}

```
그렇다면 ActiveType은 위와 같이 변경될 것이다.

이렇게 한다면 각 enum마다 db에서 null이거나 엔티티에 세팅을 하지 않았을 경우 기본 값을 세팅하게 된다.

자 그럼 이걸 언제 쓰겠다는 것인가?

```java
@Converter
@RequiredArgsConstructor
public class LowerCaseEnumConverter<T extends Enum<T> & EnumNullOperation<T>> implements AttributeConverter<T, String> {

    private final Class<T> clazz;

    /**
     * enum 상수 값을 가져와서 lowerCase로 반환한다.
     * @param attribute
     * @return String
     */
    @Override
    public String convertToDatabaseColumn(T attribute) {
        if(attribute == null) {
            T[] enums = clazz.getEnumConstants();
            // EnumNullOperation을 구현한 enum에 정의된 defaultIfNull()을 통해서 해당 default enum정보를 반환한다.
            attribute = defaultEnum(enums);
        }
        return attribute.name().toLowerCase();
    }

    /**
     * 디비 정보는 lowerCase로 upperCase로 변환후 비교후 해당 enum객체를 반환하게 한다.
     * @param dbData
     * @return T
     */
    @Override
    public T convertToEntityAttribute(String dbData) {
        T[] enums = clazz.getEnumConstants();
        try {
            return Arrays.stream(enums)
                         // dbData가 null이면 catch로 넘어가서 T의 defaultIfNull을 반환할 것이다.
                         .filter(en -> en.name().equals(dbData.toUpperCase()))
                         // orElseThrow가 발생한다면 이건 디비쪽에 컬럼의 enum이 확장되었을 가능성이 있다.
                         .findFirst().orElseThrow(NoSuchElementException::new);
        } catch(NullPointerException e) {
            return defaultEnum(enums);
        }
    }

    /**
     * 반복되는 공통 코드 줄이자
     * @param enums
     * @return T
     */
    private T defaultEnum(T[] enums) {
        return Arrays.stream(enums)
                     .filter(en -> en == en.defaultIfNull())
                     .findFirst().orElseThrow(NoSuchElementException::new);
    }

}

```

하지만 이것을 그대로 사용할려고 엔티티에 다음과 같이

```java
public class Member {

    // do some columns

    /** 고객의 상태 */
    @Convert(converter = LowCaseEnumConverter<ActivityType>.class)
    private ActivityType status;

}
```

가능할까? 아마도 'Cannot select from parameterized type'라고 ide에서 오류를 보여줄 것이다.

결국에는 자바의 특징을 최대한 활용해서 해당 Enum에 그 enum에 맞게 convert를 선언해서 가져와 사용하는 방식으로 변경하자.

그렇다면 기존의 LowerCaseEnumConverter는 추상 클래스로 만들자.

인터페이스가 default 메소드를 통해 구현된 메소드를 가지게 되면서 추상 클래스와 인터페이스의 차이가 자바 8이후 살짝 미묘해진 경향이 있지만 추상 클래스는 내부에 변수, 생성자, private 메소드를 가질 수 있다.

애초에 그냥 클래스였던 녀석이기 때문에 추상 클래스로 만들자.

```java
/**
 * lower case처리를 위한 추상 클래스
 * created by basquiat
 */
@Converter
@RequiredArgsConstructor
public abstract class LowerCaseEnumConverter<T extends Enum<T> & EnumNullOperation<T>> implements AttributeConverter<T, String> {

    private final Class<T> clazz;

    /**
     * enum 상수 값을 가져와서 lowerCase로 반환한다.
     * @param attribute
     * @return String
     */
    @Override
    public String convertToDatabaseColumn(T attribute) {
        if(attribute == null) {
            T[] enums = clazz.getEnumConstants();
            // enum에 정의된 ofNull()을 통해서 해당 default enum정보를 반환한다.
            attribute = defaultEnum(enums);
        }
        return attribute.name().toLowerCase();
    }

    /**
     * 디비 정보는 lowerCase로 upperCase로 변환후 비교후 해당 enum객체를 반환하게 한다.
     * @param dbData
     * @return T
     */
    @Override
    public T convertToEntityAttribute(String dbData) {
        T[] enums = clazz.getEnumConstants();
        try {
            return Arrays.stream(enums)
                         // dbData가 null이면 catch로 넘어가서 T의 ofNull을 반환할 것이다.
                         .filter(en -> en.name().equals(dbData.toUpperCase()))
                         // orElseThrow가 발생한다면 이건 디비쪽에서 컬럼의 enum이 확장되서 상태값이 늘었을 가능성이 아주 높다.
                         .findFirst().orElseThrow(NoSuchElementException::new);
        } catch(NullPointerException e) {
            // dbData가 null이면 toUpperCase()때 npe가 발생할 것이다.     
            // try - catch가 귀찮다면 애초에 null체크해서 defaultEnum을 반환해도 좋다.
            return defaultEnum(enums);
        }
    }

    /**
     * 반복되는 공통 코드 줄이자
     * @param enums
     * @return T
     */
    private T defaultEnum(T[] enums) {
        return Arrays.stream(enums)
                     .filter(en -> en == en.defaultIfNull())
                     .findFirst().orElseThrow(NoSuchElementException::new);
    }

}
```

그렇다면 이제 ActivityType도 최종 버전으로 향하자.

```java
/**
 * active type enum class
 */
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public enum ActiveType implements EnumNullOperation<ActiveType> {

    ACTIVE("활성"),
    INACTIVE("비활성"),
    BLACKLIST("블랙컨슈머"),
    DELETE("탈퇴");

    @Getter
    private String description;

    /**
     * null이라면 활성 상태의 enum을 기존 enum으로 반환한다.
     * @return ActiveType
     */
    @Override
    public ActiveType defaultIfNull() {
        return ActiveType.ACTIVE;
    }

    /**
     * Custom Converter for lower case
     */
    public static class LowerCaseConverter extends LowerCaseEnumConverter<ActiveType> {
        public LowerCaseConverter() {
            super(ActiveType.class);
        }
    }

}
```
이와 같이 LowerCaseConverter를 제공하자.

단점이라면 그냥 단순한 enum 클래스에 무언가가 복잡하게 들어간다. 하지만 어쩔 수 없는 경우라면 이렇게 확장해서 사용하는 것도 좋다.

```java
public class Member {

    // do some columns

    /** 고객의 상태 */
    @Convert(converter = ActivityType.LowerCaseConverter.class)
    private ActivityType status;

}
```

