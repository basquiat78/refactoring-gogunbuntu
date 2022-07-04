# refactoring-gogunbuntu
리팩토링 고군분투기

# BigDecimal과 BigInteger를 만나다.

IT밥을 먹은 지 8년차가 되던 시점에 이직을 고려할 때 같이 일했던 선배님이 넌지시 연락을 했다.

'같이 블록체인 할래?'

~~같이 계곡 갈래?~~

|Primitive Type|Wrapper Class|
|:---:|:---:|
|byte|Byte|
|short|Short|
|int|Integer|
|long|Long|
|float|Float|
|double|Double|
|boolean|Boolean|
|char|Character|

지금까지는 원형 타입과 그와 관련된 래퍼 클래스만 알다가 이더리움과 비트코인 지갑 관련 작업들을 하면서 처음 만나게 된 두 녀석이 바로 저것들이다.

~~겁나게 큰 정수와 겁나게 큰 정수를 바탕으로 소수점까지 정확하게 처리하는 겁나게 큰 Decimal~~

지금 하고자 하는 내용은 몇가지 포괄적인 내용을 함께 담아내고 있다.

[이펙티브 자바]에서도 이와 관련 내용들을 확인할 수 있는데 이것을 요약해서 전달할 만큼의 능력이 안된다.

따라서 개인적으로 이와 관련된 챕터 정보를 아래에 적어 놓는다.

*** 5장 제너릭의 전반적인 내용들 ***

***아이템 14. Comparable을 구현할지 고려하라***

***아이템 17. 변경 가능성을 최소화하라***

***아이템 18. 상속보다는 컴포지션을 사용하라***

***아이템 19. 상속을 고려해 설계하고 문서화하라. 그러지 않았다면 상속을 금지하라***

그 중에 이런 내용이 있다.

```
이 책의 2판에서는 compareTo 메서드에서 정수 기본 타입 필드를 비교할 때는 관계 연산자인 <와 >를, 
실수 기본 타입 필드를 비교할 때는 정적 메서드인 Double.compare와 Float.compare를 사용하라고 권했다. 
그런데 자바 7부터는 상황이 변했다. 
박싱된 기본 타입 클래스들에 새로 추가된 정적 메서드인 compare를 이용하면 되는 것이다. 
compareTo 메서드에서 관계 연산자 <와 >를 사용하는 이전 방식은 거추장스럽고 오류를 유발하니, 이제는 추천하지 않는다.
```

사실 이대로 사용하고 있긴한가 하고 스스로 자문해 보지만...

그 중에 Float를 한번 따라가면

```java
public final class Float extends Number implements Comparable<Float> {
    // 뭔가가 엄청 긴 그 무엇들이   
}
```
위에서 언급했던 원형타입의 박싱된 형식의 래퍼 클래스를 따라가면 약간씩을 다를 수 있어도 공통적으로 compareTo라는 딸랑 하나의 메소드만 선언된 인터페이스인 Comparable을 구현하고 있다.

그리고 final이 붙어 있는 불변 클래스이다.

```
정확한 답이 필요하다면 float와 double은 피하라.
```

현재 작은 금융권 스타트업도 그렇고 블록체인의 경우 (특히 거래소)에도 사토시, 즉 소수점 8자리까지를 표현하는 방식에서도 이 작은 소수점이 해당 코인의 액수에 따라서 차이가 커진다.

즉 이런 정확한 계산을 위해서는 BigInteger나 BigDecimal을 사용하게 되는데 이 녀석들은 태생이 래퍼 클래스다.

```java
/**
 * @author  Josh Bloch
 * @author  Mike Cowlishaw
 * @author  Joseph D. Darcy
 * @author  Sergey V. Kuksenko
 * @since 1.1
 */
public class BigDecimal extends Number implements Comparable<BigDecimal> {
    // 니가 여기 왜 있니?
    private final BigInteger intVal;
}
```
BigInteger도 마찬가지인데 @author에 조슈아 브로크 옹님의 이름이 떡하니 보인다.

암튼 BigInteger도 int와 long의 확장판 같은 느낌을 주는 녀석이고 그것을 이용해 또 확장한 것 같은 넘이 BigDecimal이다.

어라 근데 이넘들은 final이 아니네???

위에 언급한 4개의 아이템중 ***아이템 17. 변경 가능성을 최소화하라*** 에서 이 내용을 확인할 수 있다.

다른 것들은 final이 붙었기 때문에 불변클래스를 의미한다.

이 말은 상속이 불가능한데 이 두개는 이유야 어찌되었든 상속이 가능하다는 의미이다.

어째든 여러분들이 이 둘 중 하나를 사용하게 된다면 코딩에 상당한 불편함을 느끼게 된다.

```java
class BasquiatTest {

    @Test
    @DisplayName("BigDecimal 비교: BigDecimal 비교를 좀 더 우아하게")
    void BigDecimal_Compare() {
        BigDecimal before = BigDecimal.TEN;
        BigDecimal after = BigDecimal.TEN;
        System.out.println(before.compareTo(after) == 0); // before와 after가 같니?
        System.out.println(before.compareTo(after) > 0);  // before가 after보다 크니?
        System.out.println(before.compareTo(after) >= 0);  // before가 after보다 크거나 같니?
        System.out.println(before.compareTo(after) < 0);  // before가 after보다 작니?
        System.out.println(before.compareTo(after) <= 0);  // before가 after보다 작거나 같니?
    }
}
```
비교가 많았던 당시에는 코드가 길어지는건 그렇다쳐도 저 형태가 눈에 바로 확 들어오지 않는다는 점인데 비교가 0이나 아니냐로 따져야 하다 보니 가독성이 확 떨어진다.

생각해 보면 float과 double같은 실수 자료형도 비슷하리라 생각하지만 이 두넘은 신기하게 기억속에서 많이 사용한 기억이 없다.

그런데 이런 형식도 오래 작업하다 보면 어느정도 익숙해지긴 한다....

그냥 저렇게 써도 이젠 딱히 불편한 걸 못느낄 때쯔음에 근데 이걸 상속할 수 있다는 게 뭔가 매력적이라는 생각을 하게 된다.

조슈아 블로크옹이 ***아이템 17. 변경 가능성을 최소화하라*** 에서 BigInteer와 BigDecimal에서 final을 붙이지 않았던 이유에 대해 설명한 이유가 있지만 호기심으로 만들어 보는 거다.

아마도 나같은 넘들이 상속이 가능하니 막 상속하고 다니다 보니 상속에 대한 오해가 엄청 많아진게 아닐까?

# 래퍼 클래스를 따라해보자.
레퍼 클래스를 사용하는 이유는 다양한다.

예를 들면 원형 타입의 경우 객체화 시켜서 메소드등을 통해 API를 제공하고 좀 더 유연하고 다양한 방식을 지원하도록 하는게 가능하기 때문이다.

단지 원형 타입의 경우가 아니더라도 이런 방식을 통해서 풍성한 방식을 제공한다는 것이 매력적이기 때문이다.

[이펙티브 자바]에서도 이와 관련 언급하는 내용이 있다.

# 상속을 통한 구현으로

그렇다면 이넘을 어떻게 상속해서 비교하는 메소드를 작성했는지 한번 코드로 바로 확인해 보자.

```java
/**
 * BigDecimal or BigInteger Util
 * created by basquiat
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PreciseNumberUtils {

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
```
일단 이런 녀석을 하나 만들어 보자.

Object로 받아서 타입을 체크하고 그에 맞춰 형변환을 한 이후에 BigDecimal나 BigInteger로 변환해 주는 단순한 녀석이다.

그리고 우리는 이제 BigDecimal을 금단의 상속을 통해 특정 목적을 달성하기 위한 클래스를 만들어 볼까 한다.

```java
/**
 * BigDecimal을 상속한 클래스
 * created by basquiat
 */
public final class BigDecimalCompare extends BigDecimal {

    /** flag constant */
    private static final int ZERO = 0;

    BigDecimalCompare(String val) {
        super(val);
    }

    /**
     * BigDecimalCompare 로 감싸기 위한 정적 메소드
     * @param source
     * @return BigDecimalCompare
     */
    public static BigDecimalCompare bd(BigDecimal source) {
        return new BigDecimalCompare(source.toString());
    }

    /**
     * this == value
     * @param value
     * @return boolean
     */
    public boolean eq(Object value) {
        return this.compareTo(toBigDecimal(value)) == ZERO;
    }

    /**
     * greater than
     * this > value
     * @param value
     * @return boolean
     */
    public boolean gt(Object value) {
        return this.compareTo(toBigDecimal(value)) > ZERO;
    }

    /**
     * greater than or equal
     * this >= value
     * @param value
     * @return boolean
     */
    public boolean gte(Object value) {
        return this.compareTo(toBigDecimal(value)) >= ZERO;
    }

    /**
     * less than
     * this < value
     * @param value
     * @return boolean
     */
    public boolean lt(Object value) {
        return this.compareTo(toBigDecimal(value)) < ZERO;
    }

    /**
     * less than or equal
     * this <= value
     * @param value
     * @return boolean
     */
    public boolean lte(Object value) {
        return this.compareTo(toBigDecimal(value)) <= ZERO;
    }

}
```
너무 간단하다.

사실 myBatis가 xml을 사용하다보니 '<'나 '>'가 파싱할때 문제를 일으켜 gte, lte같이 쓸수 있게 제공하는데 그 영향을 받은 메소드 명이다.

queryDSL에서는 goe, loe로 많이 사용한다.

gte, lte도 상관없잖아? 상황에 맞춰서 goe, loe로 바꿔 사용해도 의미만 전달된다면야 넌 is 뭔들?

Greater Than or Equals나 Greater Or Equals나 의미 전달에 충분하다고 본다.

그럼 이걸 이제 한번 사용해 보자.

```java
class BasquiatTest {

    @Test
    @DisplayName("BigDecimal 비교: 태초에는 BigDecimal을 상속받아서 사용했다.")
    void BigDecimal_Compare() {
        BigDecimal before = BigDecimal.TEN;
        BigDecimal after = BigDecimal.TEN;

        System.out.println("======= 기존 방식 ========");
        System.out.println(before.compareTo(after) == 0); // before와 after가 같니?
        System.out.println(before.compareTo(after) > 0);  // before가 after보다 크니?
        System.out.println(before.compareTo(after) >= 0);  // before가 after보다 크거나 같니?
        System.out.println(before.compareTo(after) < 0);  // before가 after보다 작니?
        System.out.println(before.compareTo(after) <= 0);  // before가 after보다 작거나 같니?

        System.out.println("========= 새로운 방식 ============");
        System.out.println(bd(before).eq(after));
        System.out.println(bd(before).gt(after));
        System.out.println(bd(before).gte(after));
        System.out.println(bd(before).lt(after));
        System.out.println(bd(before).lte(after));

    }

}
```
뭔가 그럴싸 해보인다.

차라리 저런 걸 API로 제공해주는게 차라리 낫지 않나????? 아무리 생각해도 이게 맞는거 같은데?

아...아닌가?

아무튼 [이펙티브 자바]에서는 이와 관련해 정말 장황한 설명을 한다.

```
상속은 반드시 하위 클래스가 상위 클래스의 ‘진짜’ 하위 타입인 상황에서만 쓰여야 한다. 
다르게 말하면, 클래스 B가 클래스 A와 is-a 관계일 때만 클래스 A를 상속해야 한다. 
클래스 A를 상속하는 클래스 B를 작성하려 한다면 “B가 정말 A인가?”라고 자문해보자. 
“그렇다”고 확신할 수 없다면 B는 A를 상속해서는 안 된다. 
대답이 “아니다”라면 A를 private 인스턴스로 두고, A와는 다른 API를 제공해야 하는 상황이 대다수다. 
즉, A는 B의 필수 구성요소가 아니라 구현하는 방법 중 하나일 뿐이다.
```

지금 작업을 한 목적을 생각해보면 어떤 생각이 드는가?

지금 우리는 원래 의도는 단순하게 compare하는 부분이 가독성이 떨어져 만들게 된 케이스인데 다음부분을 다시 살펴보면

```
대답이 “아니다”라면 A를 private 인스턴스로 두고, A와는 다른 API를 제공해야 하는 상황이 대다수다. 
즉, A는 B의 필수 구성요소가 아니라 구현하는 방법 중 하나일 뿐이다.
```

결국 지금 만든 클래스의 진짜 의도는 저 위에 조슈아 블로크 옹이 첫 번쨰 라인에 말씀하시는 의도와 일맥상통해 보이지 않나?

사실 이 클래스는 위에서 언급했든 부모 클래스의 메소드를 재정의해서 구현하지 않았기 때문에 책에서 장황하게 설명한 상황과는 벗어나 있다.

하지만 누군가가 이 래퍼 클래스에서 그 누군가의 판단으로 특정 메소드를 재정의해야 할것 같다고 해서 재정의를 했다고 생각해보자.

해당 내용은 ***아이템 18. 상속보다는 컴포지션을 사용하라*** 에서 잘 설명되어 있다.

결국 이 클래스를 사용하는 다른 개발자는 원래 목적과는 다른 방향으로 사용할 요소가 너무나 다분하다.

~~그런데 그 일이 실제로 일어났습니다.~~

이렇게 만들어 논 이 클래스 중 BigIntegerWrapper의 경우에는 이더리움의 wei와 gwei를 계산하기 위한 온갖 메소드들이 난무하게 된다.

상위 클래스의 add와 substract같은 녀석들을 재정의하고 사용하기 시작했다.

원래 의도했던 비교에 대한 것만 담당하길 바랬던 나와 선배의 바램과는 달라졌다.

그저 단순한 비교를 쉽게 처리하기 위한 클래스인데 여기서 뜬금없이 이더리움의 수수료와 dApp의 가스비등등을 계산하고 처리하는 로직을 만들어 사용한다고????

그럴거면 차라리 utilClass로 만드는게 더 나을 거 같은데???

그럼에도 난 이 방식이 맘에 들었다. 문제 될건 없다고 생각하기 때문이다.

# 그렇다면 그 이후에 어떻게 했니?

이미 그 회사에서는 리팩토링하기전에 나왔을 뿐더러 손을 대기에는 여기저기 퍼져있어서 결국 놔두게 된 슬픈 이야기가 전해진다.

사요나라~

# 현 회사에서 이와 관련 주니어 개발자분들과 하나씩 작성해 나갔다.

리팩토링도중 이런 코드가 너무 많아서

'만들어 논게 있어! 이걸 사용하라고!'

하고 찾아봤더니 어랏? 없네????

알고 봤더니 내가 만든 어플리케이션에서 나만 줄기차게 사용하고 있었다....

***아이템 18. 상속보다는 컴포지션을 사용하라***

책에서는 컴포지션과 재사용 가능한 전달 클래스를 통한 예제가 있지만 예전에는 나의 생각이 거기까지 미치진 못했다.

내가 생각한 방법은 그냥 컴포지션을 활용해서 상속을 없애고 해당 기능에만 집중하게 만든 케이스이다.

다만 주니어 개발자분들과 커뮤니케이션을 하면서 자바 지식도 체크해 볼겸 상속을 버리고 이 방법으로 함께 진행하는 과정을 가졌다.

그렇다면 기존의 상속했던 것들을 없애고 내부적으로 해당 인스턴스를 선언하면 끝난다.

```java
/**
 * 컴포지션으로 처리한 래퍼 클래스
 * created by basquiat
 */
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public final class BigDecimalCompare {

    /** flag constant */
    private static final int ZERO = 0;

    private final BigDecimal bigDecimal;

    /**
     * BigDecimalCompare객체를 제공하기 위한 정적 메소드
     * @param source
     * @return BigDecimalCompare
     */
    public static BigDecimalCompare bd(BigDecimal source) {
        return new BigDecimalCompare(source);
    }

    /**
     * this == value
     * @param value
     * @return boolean
     */
    public boolean eq(Object value) {
        return bigDecimal.compareTo(toBigDecimal(value)) == ZERO;
    }

    /**
     * Greater Than
     * this > value
     * @param value
     * @return boolean
     */
    public boolean gt(Object value) {
        return bigDecimal.compareTo(toBigDecimal(value)) > ZERO;
    }

    /**
     * Greater Than or Equal
     * this >= value
     * @param value
     * @return boolean
     */
    public boolean gte(Object value) {
        return bigDecimal.compareTo(toBigDecimal(value)) >= ZERO;
    }

    /**
     * Less Than
     * this < value
     * @param value
     * @return boolean
     */
    public boolean lt(Object value) {
        return bigDecimal.compareTo(toBigDecimal(value)) < ZERO;
    }

    /**
     * Less Than or Equal
     * this <= value
     * @param value
     * @return boolean
     */
    public boolean lte(Object value) {
        return bigDecimal.compareTo(toBigDecimal(value)) <= ZERO;
    }

}
```
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)를 통해서 무분별한 객체 생성을 방지한다.

그리고 bd 또는 bi처럼 정적 메소드를 통해서 접근하도록한다.

그 이유는 현재 사용되는 래퍼가 BigDecimal인지 BigInteger인지 뭔가 어색해보이지만 심플한 메소드명으로 파악이 가능하기 때문이다.

기존의 경우에는 BigDecimal과 BigInteger를 상속했기 때문에 '내 자신'으로 비교할 수 있었다면 이제는 내부에 선언한 인스턴스를 비교하게만 하면 기존에 잘 돌아가는 방식에 손을 댈 필요없이 원하는 동작을 수행한다.

어째든 딱 우리가 원하는 기능에 충실한 클래스를 만들었다.

# Generic으로 어떻게 안될까요 ????

```
선배님 반복되는 코드가 두 객체에 들어가는게 좀 그래요.... 차라리 유틸 클래스로 제네릭하게 사용할 수 없을까요?      
지금 보니까 공통적으로 Number를 상속하고 Comparable을 구현하고 있는데 이걸 사용할 수 없는건가요?
```

라고 의견을 제시했다.

원형 타입의 래퍼 클래스중 Number를 상속하고 Comparable를 구현한

- Long
- Integer
- Short
- Byte
- Double
- Float
- BigInteger
- BigDecimal

의 compareTo 구현체를 보면 공통점이 있는데 그것은 0을 기준으로 같으면 0, left > right면 1, left < right는 -1를 반환한다.

생각해 보니 그럴 수 있겠다 싶어서 다음과 같이

```java
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

    // other method

}

```

'<T extends Number & Comparable<T>>' 이렇게 함으로서 Integer, Long, Float, Double, SHort, Byte, BigInter, BigDecimal에 대한 커버가 가능해진다.

또한 저렇게 한정함으로써 위에 언급한 녀석들 외에 Comparable를 구현한 녀석들이 있더라도 저 타입외에는 허용하지 않는다.

차라리 이게 나을수도 있겠다고 생각한 순간?

```
그래도 queryDSL처럼 표현하는 방식이 눈에 더 잘 들어와요.      
그걸 좀 더 다르게 작업할 수 없을까요? 반복되는 코드도 없앨 수 있을거 같은데 다른 방법이 있을까요?
```

'뭔 요구가 이리 많아!!!!!! 음 근데 가능한가? 한번도 생각해 본 적이 없는데?'

결국 이넘들은 같은 방식으로 처리된다는 것을 확인했으니 GenericNumberCompare로 공통으로 쓸 수 있을까라는 생각에 닿기 시작한다.

```java
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
```
오호라 이렇게 했더니 잘 돌아간다! 만들어 놓고 다들 놀램!

```
오!!!!! 잘 되요!!!
```

이 클래스 하나로 전부 커버할 수 있겠구나라는 생각이 들었다.

하지만 이건 좀 위험하다는 생각이 들었다.

물론 테스트 결과 충분히 사용할 만한 가치가 있다고 생각이 들지만 오히려 타입에 대해서 확실하게 가져가는게 좋다는 생각이 들었다.

예를 들면 저 코드를 사용하게 되면

```java
public class Test {

    public vlid doSomething() {
        //
        if(is(source).eq(target)) {
            // do some
        }
        //
    }
}

```
같은 방식으로 사용할 텐데 당연히 코드를 보면 알게 되겠지만 비교대상의 타입이 뭔지 불분명해진다.

그래서 이름을 주는 것이 중요하다.

결국은 저 녀석을 [이펙티브 자바]같은 전달 클래스의 형식과 똑같진 않지만 비슷하게 재사용가능한 전달 클래스처럼 만들어서 사용하고자 한다.

```java
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

```
해당 클래스는 abstract로 클래스를 변경해서 사용해도 가능하다.

그리고 다음과 같이


```java
/**
 * Double 타입을 비교하기 위한 클래스
 * created by basquiat
 */
public final class DoubleCompare extends NumberCompare<Double> {

    /**
     * constructor
     * @param source
     * @return NumberCompare<T>
     */
    DoubleCompare(Double source) {
        super(source);
    }

    /**
     * DoubleCompare 로 감싸기 위한 정적 메소드
     * @param source
     * @return DoubleCompare
     */
    public static DoubleCompare db(Double source) {
        return new DoubleCompare(source);
    }

}

/**
 * BigDecimal 타입을 비교하기 위한 클래스
 * created by basquiat
 */
public final class BigDecimalCompare extends NumberCompare<BigDecimal> {

    /**
     * constructor
     * @param source
     * @return NumberCompare<T>
     */
    BigDecimalCompare(BigDecimal source) {
        super(source);
    }

    /**
     * BigDecimalCompare 로 감싸기 위한 정적 메소드
     * @param source
     * @return BigDecimalCompare
     */
    public static BigDecimalCompare bd(BigDecimal source) {
        return new BigDecimalCompare(source);
    }

}

```
처럼 필요한 경우에 작성해 놓은 전달 클래스를 상속해서 구현하면 된다.

last 패키지에 몇가지 Compare 객체를 만들어 놨다.

이로써 반복되는 코드는 뒤로 싹 다 숨겨버리고 간략하게 확장해서 사용이 가능하게 만들었다.

이는 auto-boxing/auto-unboxing이 가능하다.

또한 실수로

```java
Long source = 1000L;
Long target = 1000L;

if(source == target) {
    // 아니 왜 false가???
}

```
같은 실수를 방지할 수 있다.

```java
Long source = 1000L;
Long target = 1000L;

if(ll(source).eq(target)) {
    // true
}

```

# At a Glance도

아마도 주니어 개발자분들과 이 시간을 갖지 않았다면 나 조차 패키지상으로 start패키지에 있는 내용에서만 그쳤을 것이다.

오히려 게으르고 싶은 주니어 개발자분들의 생각과 그것을 확장해 나가고 싶은 욕망이 좀 더 좋은 코드를 생산해 낸다는 것을 배운 시간이다.

'아니 이런 생각을 해? 이게 될까??????'

게다가 [이펙티브 자바]는 다들 가지고 있는 책인데 아마 비슷한 생각을 했던거 같다.

'빌드 패턴이나 try-with-resource같은 몇개 아이템들 빼곤 와닿지 않고 실무에 어떻게 적용할까?'

라는 의구심을 가지고 있었던 듯 싶다. 실제로 이 내용을 중심으로 이렇게 리팩토링 하는걸 보고 다들 신기해 한듯.

사실 지금 이 내용이 그렇게 대단한 내용이 아니지만 그보다는 생각을 어디까지 확장하고 코드를 리팩토링 하는지에 대한 방식이 더 귀한 시간이었으리라.