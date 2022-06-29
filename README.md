# refactoring-gogunbuntu
리팩토링 고군분투기

# 그렇게 짠 코드에는 다 이유가 있다.

주니어 개발자분과 코드를 훝어보며 리팩토링을 하던 도중 문득 그 동료분이 이런 질문을 한다.         

"제가 이렇게 하면 저렇게 나올것이다라고 생각했는데 하루 이틀을 고민해도 도저히 해결을 못한 코드가 있습니다.

결국에는 이것을 돌리고 돌려서 어떻게 해결하긴 했는데요. 좀 더 좋은 방법이 없을까요??"

갑자기 오래전 어느 동료분과 이 문제로 몇일을 고생했던 기억이 확 떠오른다.         

# 과거로부터 전해내려온 전설적인 이야기

솔루션을 가장한 SI회사에서 처음 신입시절 비전공개발자였던 나에게 가끔 '이거 왜 이렇게 써야 하나요?'라는 질문에 선배들은 대부분 이렇게 말을 전하였다.

"과거로부터 저명한 개발자가 이렇게 하라고 하니 의문을 갖지 말고 그냥 써!"

그렇다. 과거로부터 내려온 전설적인 코드는 그렇게 의문 없이 사용하게 되었다카더라는 전설이.....

# equals() 그리고 hashCode() 이건 먹는건가요?

java입문서나 관련 서적을 보면 equals와 hashcode에 대한 중요하게 생각하며 장황한 설명을 보게 된다.        

하지만 대부분 이부분은 희안하게 가볍게 건너띄는 경우가 많다. 이론적으로 '아 그렇구나'하고 그냥 넘어가는 경우도 많다.       

또는 간과하기 쉽다. 위에서처럼 우리는 아무 의심없이 전설적으로 내려오는 그 코드를 삽입하고 사용하기 때문이다.      

그리고 관습적으로 객체를 비교할 때는 '=='로 그리고 String 변수를 비교할 때는 'a.equals("어떤 값")'처럼 사용하는 것으로 그친다.

사실 나의 경우에도 마찬가지였다.

조슈아 블로크의 [이펙티브 자바]의 경우에도 한 챕터를 할애할 정도로 많은 이야기를 하지만 마지막 핵심정리는 다음처럼 귀결시킨다.

```
꼭 필요한 경우가 아니면 equals를 재정의하지 말자. 많은 경우에 Object의 equals가 여러분이 원하는 비교를 정확히 수행해준다. 
재정의해야 할 때는 그 클래스의 핵심 필드 모두를 빠짐없이, 다섯 가지 규약을 확실히 지켜가며 비교해야 한다.
```

추가적으로 구글의 autoValue를 홍보한다! 롬복이랑 비슷하면서도 뭔가 다른 그 어떤 라이브러리.

한번도 써 본적이 없어서 한번 설정해서 실제로 생성된 코드를 살펴보면 롬복과 크게 차이가 나지 않는다.

아마도 두개의 라이브러리는 정확하고 안정적인 규약을 통해서 생성하는 모양새다.

생성된 코드는 [이펙티브 자바]의 내용과 동일하게 구현되어 있다.

'꼭 필요한 경우가 아니면 equals를 재정의하지 말자.' 이것을 나는 순진하게 그대로 받아드리고 있던게 아니였을까?

하지만 반대로 얘기하면 그럼 '꼭 필요한 경우는 언제인가?'라는 의문이 든다.

물론 나는 순진한(자라 쓰고 실력없는이라고 읽는) 개발자니깐 조슈아 블로크같은 분이 저렇게 말했으니 이런 생각이 전혀 들지 않았다.

그리고 우리는 선배가 '과거로부터 전해내려온' 그 방식을 아무 의심없이 사용하기 시작한다.

그래서 이건 당하기 전까지는 그 전설에 대해서 의문을 갖지 않는다.

# JPA를 쓰면 재정의할 필요가 있던데?

정말인가? 하지만 영속성 컨텍스트와 관련해서 이것을 재정의하지 않아도 프록시를 통해 엔티티를 반환할때 1차 캐쉬에서 이미 있는지 파악하고 잘 작동한다.

그런데 왜 재정의할 필요가 있다는 말을 할까?

솔직히 스프링의 JPA를 쓰다보면 의도적으로 어떤 엔티티를 detach를 통해 준 영속성 상태를 만드는 경우를 못본 거 같다.

~~이론 설명할때 빼놓곤!~~

물론 엔티티를 가져와 어떤 비지니스 로직에 사용하고 dirty checking같은 것을 방지할려는 목적으로 사용할 수는 있겠다는 생각이 퍼득 든다.

하지만 차라리 dto로 반환해서 사용한다면 이것도 실상 쓸일이 거의 없어 보인다. 당연히 경험이 없으니 아는 만큼만 보이는 걸텐데... 시무륵

물론 OSIV의 설정을 true가 아닌 false로 설정했을 경우 서비스 계층이 아닌 view template이나 컨트롤러 계층에서 비교할일이 발생할지는 모르겠지만 이유야 어찌되었든 진짜 '꼭 필요한 경우가 아니면 equals를 재정의하지 말자.'라는 문구가 그대로 실무에서도 이뤄진다.

하지만 주의할 것은 'JPA를 쓰면 재정의할 필요가 있던데?'이리는 말에서 힌트를 얻을 수 있다.

## JPA에서 연관관계시 List Vs Set??

'JPA를 쓰면 재정의할 필요가 있던데?'라는 질문은 @OneToMany나 또는 @Embeddable을 이용한 불변 객체와 엔티티를 @ElementCollection와 @CollectionTable을 이용해 값 타입 콜렉션으로 쓸 경우를 말하는 경우이다.

예를 들면 컬렉션의 경우 List 또는 중복을 허용하지 않기 위한 Set을 사용하는 경우인데 @ElementCollection와 @CollectionTable의 경우에는 사용하지 않는게 좋다.

얘초에 저건 한번 날려보면 N에 해당하는 테이블의 데이터를 무조건 싹 다 지우고 다시 인서트하는 어마무시한 일을 보게 될것이기 때문이다.

이유는 JPA관련 책이나 블로그를 검색해 보면 알게 된다.

그렇다는 것은 @OneToMany의 경우이다.

하지만 이 때는 equals 와 hashcode를 재정의할 때 주의를 요한다.

다음 엔티티를 간단하게 정의해 보자.


```java
@Getter
@Entity
@Table(name = "member")
@ToString(exclude = {"favoriteAddresses"})
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member {

  @Builder
  public Member(String id, String name) {
    this.id = id;
    this.name = name;
  }

  /** 사용자 아이디 */
  @Id
  private String id;

  /** 사용자 이름 */
  private String name;

  @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
  @JoinColumn(name = "member_id")
  private Set<FavoriteAddress> favoriteAddresses = new HashSet<>();

}

@Getter
@Entity
@EqualsAndHashCode
@Table(name = "favorite_address")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FavoriteAddress {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Builder
  public FavoriteAddress(String city, String street, String zipcode) {
    this.city = city;
    this.street = street;
    this.zipcode = zipcode;
  }

  /** 시 */
  @Column(name = "member_city")
  private String city;

  /** 동 */
  @Column(name = "member_street")
  private String street;

  /** 우편 번호 */
  @Column(name = "member_zipcode")
  private String zipcode;

  /** 전체 주소 가져오 */
  public String totalAddress() {
    return city + " " + street + ", " + zipcode;
  }

}

```
Set으로 중복을 방지한다 해도 equals와 hashcode를 재정의하지 않으면 의미가 없다.

게다가 지금같은 경우에는 설령 FavoriteAddress에 그냥 단순하게 롬복을 통해 equals와 hashcode를 재정의해도 중복으로 데이터가 들어간다.

왜냐하면 지금의 엔티티 구조로 볼때 favorite_address테이블의 primary key에 해당하는 id는 변경되기 때문이다.

그래서 아이디를 제외한 다른 항목들이 설령 전부 같다 해도 Set 입장에서는 다른 객체로 본다.

그래서 롬복을 이용해서 재정의하거나 코드로 직접 재정의할 경우에는 id를 제외해야 한다.       

```java
@EqualsAndHashCode(exclude = {"id"})
```

초창기에 이걸로 고생한거 생각하면 눙물이 앞을 가린다.             

~~아니 중복 방지를 한다며????? 근데 왜 중복 데이터가 생기지????????~~

하지만 favorite_address에 만일 배송지에 대한 이름을 정의할 수 있게 된다면 이때는 이것을 다시 고려해 봐야 한다.               

이름까지 제외해서 중복 처리를 할 것이냐 아니면 나머지는 같고 이름이 다르다고 할때 이것을 고객의 의도인지 아닌지 알 방법이 없다.        

그렇다고 중복으로 처리하게 되면 이로 인한 고객의 컴플레인 - 분명 작성을 했는데 저장이 안되는데요? 몇번을 작성하게 하는 건지 같은??? - 이 접수된다면 어떻게 할 것인가?         

따라서 List로 할 것인지 Set으로 할것인지 그리고 Set으로 할경우 equals와 hashcode를 어떻게 재정의 할것인지 판단해야 한다.      

하지만 지금 언급한 내용은 JPA에만 국한된 내용이 아니다.      

단지 이것은 java의 전반적인 내용이다.       

**JPA와 관련 예제 테스트 코드를 남겨둔다.**


# 당해보기 전까지는 모른다~~

오래전 어느 날 동료 한 분이 나에게 도움을 요청했다.       

"이 코드좀 봐줘봐! 귀신을 만난 거 같아! 내 코드에 잘못된 부분이 있는건지."       

여러분들은 Map을 사용할 때 어떤 특정 객체를 키값으로 사용해 본적이 있는지 궁금하다.

에를 들면

```java
public class Test {


    public Map<Musician, Genre> createMap() {
        Map<Musician, Genre> result = new HashMap<>();
        result.put(new Musician("John Coltrane"), new Genre("Jazz"));
        result.put(new Musician("ESENSE"), new Genre("Hiphop"));
        // so something
        return result;
    }


}

```
처럼 사용해 본 일이 있는가 이다.

물론 자바 콜렉션 프레임워크의 Map인터페이스를 살펴보면 Map<K, V>처럼 제너릭타입을 받는 것을 볼 수 있다.

하지만 나는 그떄 '오잉? 저런 객체를 키값으로 설정할 수 있다고?'라는 생각이 먼저 들었다.     

그리고 '왜??'라는 의문으로 들어간다. 굳이 힘들게 객체를 키로 설정하는 이유가 무엇인지 도통 알수 없었다.             

그냥 스트링이나 primitive type중 넘버계열의 키만 설정해 사용했던 나로써는 나름 신기했던 것이다.        

하긴 뭐든 가능하긴 하다.       

어째든 궁금해서 Map을 따라가다보면 - Set > HashSet을 따라가도 상관없다. -

```java
public interface Map<K, V> {
    
    // do something

    V get(Object key);
    
    // do something
}

```

이것을 구현한 것들 중 가장 많이 사용되는 HashMap을 따라가면

```java
public class HashMap<K,V> extends AbstractMap<K,V>
        implements Map<K,V>, Cloneable, Serializable {

    public V get(Object key) {
        Node<K,V> e;
        return (e = getNode(hash(key), key)) == null ? null : e.value;
    }

    final Node<K,V> getNode(int hash, Object key) {
        Node<K,V>[] tab; Node<K,V> first, e; int n; K k;
        if ((tab = table) != null && (n = tab.length) > 0 &&
                (first = tab[(n - 1) & hash]) != null) {
            if (first.hash == hash && // always check first node
                    ((k = first.key) == key || (key != null && key.equals(k))))
                return first;
            if ((e = first.next) != null) {
                if (first instanceof TreeNode)
                    return ((TreeNode<K,V>)first).getTreeNode(hash, key);
                do {
                    if (e.hash == hash &&
                            ((k = e.key) == key || (key != null && key.equals(k))))
                        return e;
                } while ((e = e.next) != null);
            }
        }
        return null;
    }
   
    static final int hash(Object key) {
        int h;
        return (key == null) ? 0 : (h = key.hashCode()) ^ (h >>> 16);
    }

}
```
오호라? 실제 내부를 처음 봤을때는 신기방기했다.

결국 value를 가져오기 위해서 Object key의 equals와 hashCode를 사용하는 것을 발견하게 된다.

어느 책을 봐도 equals를 재정의하면 hashcode도 같이 재정의하라는 이야기를 쌍으로 꼭 보게 되는데 거기에는 이런 이유가 있기 때문이다.

[이펙티브 자바]의 내용중 하나를 발췌해 보면

- equals 비교에 사용되는 정보가 변경되지 않았다면, 애플리케이션이 실행되는 동안 그 객체의 hashCode 메서드는 몇 번을 호출해도 일관되게 항상 같은 값을 반환해야 한다.     
  단, 애플리케이션을 다시 실행한다면 이 값이 달라져도 상관없다.
- equals(Object)가 두 객체를 같다고 판단했다면, 두 객체의 hashCode는 똑같은 값을 반환해야 한다.
- equals(Object)가 두 객체를 다르다고 판단했더라도, 두 객체의 hashCode가 서로 다 른 값을 반환할 필요는 없다.     
  단, 다른 객체에 대해서는 다른 값을 반환해야 해시테이블 의 성능이 좋아진다.


***hashCode 재정의를 잘못했을 때 크게 문제가 되는 조항은 두 번째다. 즉, 논리적으로 같은 객체는 같은 해시코드를 반환해야 한다.***

결국 이 이야기는 JPA에서만 국한되는 이야기가 아니다.      

그 동료분은 다음과 같은 시나리오를 토대로 코드를 작성했다. 최대한 당시의 흐릿하지만 기억력을 쥐어짜서 비슷하게 구성해 봤다.        

'일별 카드별로 결제한 정보를 모은다.'         

당시 일별 카드별로 결제한 정보중 어느 특정 카드로 어떤 특정 상품을 구입한 경우에는 포인트로 페이백을 주는 이벤트가 있었기 때문이다.     

이 당시에는 myBatis였는데 jpa든 myBatis를 사용하든 JdbcTemplate를 사용하든 그 어떤 형식이든 상관없다. 데이터베이스로부터 어떤 정보를 가져왔다고 한다면

```java
/**
 * 최대한 간결하게 일별로 상품에 대해 어떤 카드를 썼는지 담는 객체
 */
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class StatisticsCardPayment {

    private String card;

    private String itemCode;

    private String itemName;

    private long price;

    private LocalDateTime paymentDate;

    @Builder
    public StatisticsCardPayment(@NonNull String card, @NonNull String itemCode, @NonNull String itemName, long price, @NonNull LocalDateTime paymentDate) {
        this.card = card;
        this.itemCode = itemCode;
        this.itemName = itemName;
        this.price = price;
        this.paymentDate = paymentDate;
    }

}
```

귀찮아서 5개만....

```java
    final String[] cards = {"국민", "신한", "농협"};

    LocalDateTime now = now();
    
    List<StatisticsCardPayment> statisticsCardPayments = new ArrayList<>();
    StatisticsCardPayment statisticsCardPayment1 = StatisticsCardPayment.builder()
                                                                        .card(cards[0])
                                                                        .itemCode("ic_0001")
                                                                        .itemName("상품명[ic_0001]")
                                                                        .price(100000)
                                                                        .paymentDate(now.minusDays(1))
                                                                        .build();
    statisticsCardPayments.add(statisticsCardPayment1);
    StatisticsCardPayment statisticsCardPayment2 = StatisticsCardPayment.builder()
                                                                        .card(cards[1])
                                                                        .itemCode("ic_0002")
                                                                        .itemName("상품명[ic_0002]")
                                                                        .price(120000)
                                                                        .paymentDate(now.minusDays(1))
                                                                        .build();
    statisticsCardPayments.add(statisticsCardPayment2);
    StatisticsCardPayment statisticsCardPayment3 = StatisticsCardPayment.builder()
                                                                        .card(cards[0])
                                                                        .itemCode("ic_0001")
                                                                        .itemName("특사 상품명[ic_0001]")
                                                                        .price(80000)
                                                                        .paymentDate(now.minusDays(3))
                                                                        .build();
    statisticsCardPayments.add(statisticsCardPayment3);
    StatisticsCardPayment statisticsCardPayment4 = StatisticsCardPayment.builder()
                                                                        .card(cards[2])
                                                                        .itemCode("ic_0003")
                                                                        .itemName("상품명[ic_0003]")
                                                                        .price(120000)
                                                                        .paymentDate(now.minusDays(4))
                                                                        .build();
    statisticsCardPayments.add(statisticsCardPayment4);
    StatisticsCardPayment statisticsCardPayment5 = StatisticsCardPayment.builder()
                                                                        .card(cards[2])
                                                                        .itemCode("ic_0001")
                                                                        .itemName("상품명[ic_0001]")
                                                                        .price(100000)
                                                                        .paymentDate(now.minusDays(4))
                                                                        .build();
    statisticsCardPayments.add(statisticsCardPayment5);
```

그리고 이런 dto하나가 있었다.

작명이 나름 설득력있다. card/item/paymentdate!

```java
/**
 * Map의 키값으로 사용하기 위한 dto
 */
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CardItemPaymentDto {

    private String card;

    private String itemCode;

    private String paymentDate;

    @Builder
    public CardItemPaymentDto(@NonNull String card, @NonNull String itemCode, @NonNull String paymentDate) {
        this.card = card;
        this.itemCode = itemCode;
        this.paymentDate = paymentDate;
    }

}

```
아이템의 이름과 가격은 상황에 따라 다를 수가 있다. 상품 재고 관리를 하는 방식에 따라 같은 아이템이라도 시기에 따라 상품명에 특가라는 단어가 붙기도 하고 그에 따른 가격 책정이 있기 때문에 키 값으로 딱 맞는건 고유한 정보일 것이다.     

그래서 card와 고유한 상품 코드, 그리고 일별 체크를 위한 부분으로 paymentDate를 갖는 키값으로 사용할 객체를 만든다.

여기까지 내가 생각했던 것은 왜 이것을 키값으로 사용하려 했을까였는데 다음 코드를 보기 시작하면서 깨닫게 된다.


```java
final String[] cards = {"국민", "신한", "농협"};

LocalDateTime now = now();

List<StatisticsCardPayment> statisticsCardPayments = new ArrayList<>();
StatisticsCardPayment statisticsCardPayment1 = StatisticsCardPayment.builder()
                                                                    .card(cards[0])
                                                                    .itemCode("ic_0001")
                                                                    .itemName("상품명[ic_0001]")
                                                                    .price(100000)
                                                                    .paymentDate(now)
                                                                    .build();
statisticsCardPayments.add(statisticsCardPayment1);
StatisticsCardPayment statisticsCardPayment2 = StatisticsCardPayment.builder()
                                                                    .card(cards[1])
                                                                    .itemCode("ic_0002")
                                                                    .itemName("상품명[ic_0002]")
                                                                    .price(120000)
                                                                    .paymentDate(now.minusDays(1))
                                                                    .build();
statisticsCardPayments.add(statisticsCardPayment2);
StatisticsCardPayment statisticsCardPayment3 = StatisticsCardPayment.builder()
                                                                    .card(cards[0])
                                                                    .itemCode("ic_0001")
                                                                    .itemName("특사 상품명[ic_0001]")
                                                                    .price(80000)
                                                                    .paymentDate(now)
                                                                    .build();
statisticsCardPayments.add(statisticsCardPayment3);
StatisticsCardPayment statisticsCardPayment4 = StatisticsCardPayment.builder()
                                                                    .card(cards[2])
                                                                    .itemCode("ic_0003")
                                                                    .itemName("상품명[ic_0003]")
                                                                    .price(120000)
                                                                    .paymentDate(now.minusDays(4))
                                                                    .build();
statisticsCardPayments.add(statisticsCardPayment4);
StatisticsCardPayment statisticsCardPayment5 = StatisticsCardPayment.builder()
                                                                    .card(cards[2])
                                                                    .itemCode("ic_0001")
                                                                    .itemName("상품명[ic_0001]")
                                                                    .price(100000)
                                                                    .paymentDate(now.minusDays(4))
                                                                    .build();
statisticsCardPayments.add(statisticsCardPayment5);

Map<CardItemPaymentDto, List<StatisticsCardPayment>> result = new HashMap<>();

```
아마도 예상할 수 있지 않을까?

일단 어떤 card/itemCode/paymentDate로부터 정보를 가져와 키로 사용할 CardItemPaymentDto를 생성하고 해당 StatisticsCardPayment를 리스트 형식으로 가져온다.       

그리고 쭉 돌면서 해당 키로 있는지 없는지 확인하고 없으면 put, 있으면 가져와서 해당 리스트에 추가.      

```java
final String[] cards = {"국민", "신한", "농협"};

LocalDateTime now = now();

List<StatisticsCardPayment> statisticsCardPayments = new ArrayList<>();
StatisticsCardPayment statisticsCardPayment1 = StatisticsCardPayment.builder()
                                                                    .card(cards[0])
                                                                    .itemCode("ic_0001")
                                                                    .itemName("상품명[ic_0001]")
                                                                    .price(100000)
                                                                    .paymentDate(now)
                                                                    .build();
statisticsCardPayments.add(statisticsCardPayment1);
StatisticsCardPayment statisticsCardPayment2 = StatisticsCardPayment.builder()
                                                                    .card(cards[1])
                                                                    .itemCode("ic_0002")
                                                                    .itemName("상품명[ic_0002]")
                                                                    .price(120000)
                                                                    .paymentDate(now.minusDays(1))
                                                                    .build();
statisticsCardPayments.add(statisticsCardPayment2);
StatisticsCardPayment statisticsCardPayment3 = StatisticsCardPayment.builder()
                                                                    .card(cards[0])
                                                                    .itemCode("ic_0001")
                                                                    .itemName("특사 상품명[ic_0001]")
                                                                    .price(80000)
                                                                    .paymentDate(now)
                                                                    .build();
statisticsCardPayments.add(statisticsCardPayment3);
StatisticsCardPayment statisticsCardPayment4 = StatisticsCardPayment.builder()
                                                                    .card(cards[2])
                                                                    .itemCode("ic_0003")
                                                                    .itemName("상품명[ic_0003]")
                                                                    .price(120000)
                                                                    .paymentDate(now.minusDays(4))
                                                                    .build();
statisticsCardPayments.add(statisticsCardPayment4);
StatisticsCardPayment statisticsCardPayment5 = StatisticsCardPayment.builder()
                                                                    .card(cards[2])
                                                                    .itemCode("ic_0001")
                                                                    .itemName("상품명[ic_0001]")
                                                                    .price(100000)
                                                                    .paymentDate(now.minusDays(4))
                                                                    .build();
statisticsCardPayments.add(statisticsCardPayment5);

final Map<CardItemPaymentDto, List<StatisticsCardPayment>> resultMap = new HashMap<>();
        
statisticsCardPayments.stream()
                      .forEach(entity -> {
                            CardItemPaymentDto key = CardItemPaymentDto.builder()
                                                                       .card(entity.getCard())
                                                                       .itemCode(entity.getItemCode())
                                                                       .paymentDate(LocalDateTimeForm.SIMPLE_YMD.transform(entity.getPaymentDate()))
                                                                       .build();
                            List<StatisticsCardPayment> values = resultMap.get(key);
                            if(values == null) {
                                // 없으면 리스트로 넣자.
                                resultMap.put(key, new ArrayList<>(Collections.singletonList(entity)));
                            } else {
                                // 있으면 기존의 리스트에 추가
                                values.add(entity);
                            }
                      });

System.out.print(resultMap.toString());

```
의도적으로 국민/ic_0001/now가 같은 녀석이 두개가 존재하기 때문에 이 키에 맞춰 2개의 리스트가 생성되야 한다.

하지만 결과는 전혀 다르게 나온다.

```
real result:
{
    CardItemPaymentDto(card=국민, itemCode=ic_0001, paymentDate=2022-07-03)=[StatisticsCardPayment(card=국민, itemCode=ic_0001, itemName=상품명[ic_0001], price=100000, paymentDate=2022-07-03T19:24:51.820872)], 
    CardItemPaymentDto(card=국민, itemCode=ic_0001, paymentDate=2022-07-03)=[StatisticsCardPayment(card=국민, itemCode=ic_0001, itemName=특사 상품명[ic_0001], price=80000, paymentDate=2022-07-03T19:24:51.820872)], 
    CardItemPaymentDto(card=농협, itemCode=ic_0003, paymentDate=2022-06-30)=[StatisticsCardPayment(card=농협, itemCode=ic_0003, itemName=상품명[ic_0003], price=120000, paymentDate=2022-06-30T19:24:51.820872)], 
    CardItemPaymentDto(card=농협, itemCode=ic_0001, paymentDate=2022-06-30)=[StatisticsCardPayment(card=농협, itemCode=ic_0001, itemName=상품명[ic_0001], price=100000, paymentDate=2022-06-30T19:24:51.820872)], 
    CardItemPaymentDto(card=신한, itemCode=ic_0002, paymentDate=2022-07-03)=[StatisticsCardPayment(card=신한, itemCode=ic_0002, itemName=상품명[ic_0002], price=120000, paymentDate=2022-07-03T19:24:51.820872)]
}
```
아마도 원래 의도했던 결과는 다음과 같을 것이다.

```
expected result:
{
    CardItemPaymentDto(card=국민, itemCode=ic_0001, paymentDate=2022-07-03)=[
        StatisticsCardPayment(card=국민, itemCode=ic_0001, itemName=상품명[ic_0001], price=100000, paymentDate=2022-07-03T19:24:51.820872), 
        StatisticsCardPayment(card=국민, itemCode=ic_0001, itemName=특사 상품명[ic_0001], price=80000, paymentDate=2022-07-03T19:24:51.820872)
    ], 
    CardItemPaymentDto(card=농협, itemCode=ic_0003, paymentDate=2022-06-30)=[StatisticsCardPayment(card=농협, itemCode=ic_0003, itemName=상품명[ic_0003], price=120000, paymentDate=2022-06-30T19:24:51.820872)], 
    CardItemPaymentDto(card=농협, itemCode=ic_0001, paymentDate=2022-06-30)=[StatisticsCardPayment(card=농협, itemCode=ic_0001, itemName=상품명[ic_0001], price=100000, paymentDate=2022-06-30T19:24:51.820872)], 
    CardItemPaymentDto(card=신한, itemCode=ic_0002, paymentDate=2022-07-03)=[StatisticsCardPayment(card=신한, itemCode=ic_0002, itemName=상품명[ic_0002], price=120000, paymentDate=2022-07-03T19:24:51.820872)]
}
```
[이펙티브 자바]에서는 이것을 다음과 같이 설명한다.

```
PhoneNumber 클래스는 hashCode 를 재정의하지 않았기 때문에 논리적 동치인 두 객체가 서로 다른 해시코드를 반환하여 두 번째 규약을 지키지 못한다.      
그 결과 get 메서드는 엉뚱한 해시 버킷에 가서 객체를 찾으려 한 것이다. 
설사 두 인스턴스를 같은 버킷에 담았더라도 get 메서드는 여전히 null을 반환하는데, HashMap은 해시코드가 다른 엔트리끼리는 동치성 비교를 시도조차 하지 않도록 최적화되어 있기 때문이다.
```

위에서 우리가 Map을 따라가면서 만난

```java
public class HashMap<K,V> extends AbstractMap<K,V>
        implements Map<K,V>, Cloneable, Serializable {

    public V get(Object key) {
        Node<K,V> e;
        return (e = getNode(hash(key), key)) == null ? null : e.value;
    }

    final Node<K,V> getNode(int hash, Object key) {
        Node<K,V>[] tab; Node<K,V> first, e; int n; K k;
        if ((tab = table) != null && (n = tab.length) > 0 &&
                (first = tab[(n - 1) & hash]) != null) {
            if (first.hash == hash && // always check first node
                    ((k = first.key) == key || (key != null && key.equals(k))))
                return first;
            if ((e = first.next) != null) {
                if (first instanceof TreeNode)
                    return ((TreeNode<K,V>)first).getTreeNode(hash, key);
                do {
                    if (e.hash == hash &&
                            ((k = e.key) == key || (key != null && key.equals(k))))
                        return e;
                } while ((e = e.next) != null);
            }
        }
        return null;
    }
   
    static final int hash(Object key) {
        int h;
        return (key == null) ? 0 : (h = key.hashCode()) ^ (h >>> 16);
    }

}
```
이 코드를 보면 getNode를 타기 전에 hashCode를 먼저 비교하는 hash메소드를 호출하는 것을 볼 수 있다.

결국 ***HashMap은 해시코드가 다른 엔트리끼리는 동치성 비교를 시도조차 하지 않도록 최적화되어 있기 때문이다.*** 여기서 힌트를 얻게된다.

이것을 원하는 결과로 얻기 위해서는 조슈아 블로크 옹께서 홍보하는 autoValue를 이용하거나 롬복의 어노테이션을 이용하면 끝난다.

```java
/**
 * Map의 키값으로 사용하기 위한 dto
 */
@Getter
@ToString
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CardItemPaymentDto {

    private String card;

    private String itemCode;

    private String paymentDate;

    @Builder
    public CardItemPaymentDto(@NonNull String card, @NonNull String itemCode, @NonNull String paymentDate) {
        this.card = card;
        this.itemCode = itemCode;
        this.paymentDate = paymentDate;
    }

}

```
원하는 결과를 얻었을 것이다.

사실 무언가를 할 때 map 자체를 가지고 비지니스 로직을 처리하는 것이 어려울 때가 있다.

최종적으로는 StatisticsCardPayment에 대한 dto를 만들고 그것을 전체적으로 감싸서 사용할 객체를 만들어 보자.

```java
/**
 * StatisticsCardPayment > dto
 * 이름이 길어서 줄여버림
 */
@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CardPaymentDto {

    private String card;

    private String itemCode;

    private String itemName;

    private long price;

    private LocalDateTime paymentDate;

    @Builder
    public CardPaymentDto(@NonNull String card, @NonNull String itemCode, @NonNull String itemName, long price, @NonNull LocalDateTime paymentDate) {
        this.card = card;
        this.itemCode = itemCode;
        this.itemName = itemName;
        this.price = price;
        this.paymentDate = paymentDate;
    }

    public static CardPaymentDto entityToDto(StatisticsCardPayment entity) {
        return CardPaymentDto.builder()
                             .card(entity.getCard())
                             .itemCode(entity.getItemCode())
                             .itemName(entity.getItemName())
                             .paymentDate(entity.getPaymentDate())
                             .build();
    }

}

/**
 * 맵의 키와 밸류를 담는 일종의 이름을 막지은 분석 결과 객체 
 */
@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AnalysisResult {

    private CardItemPaymentDto cardItemPayment;

    private List<CardPaymentDto> cardPayments;

    @Builder
    public AnalysisResult(@NonNull CardItemPaymentDto cardItemPayment, List<CardPaymentDto> cardPayments) {
        this.cardItemPayment = cardItemPayment;
        this.cardPayments = cardPayments;
    }

    public boolean wantToFind(@NonNull String card, @NonNull String itemCode, @NonNull String paymentDate) {
        if(card.equals(this.cardItemPayment.getCard()) && itemCode.equals(this.cardItemPayment.getItemCode()) && paymentDate.equals(this.cardItemPayment.getPaymentDate())) {
            return true;
        }
        return false;
    }

}

```

뭔가 좀 오버 페이스 하는거 같아 보이긴 하지만 이렇게도 할 수 있다는 것을 한번 보여주고 싶었다.


```java
class SimpleTest {

  @Test
  @DisplayName("JUST_DO_CODING: 데이터 생성하고 테스트 해보기")
  void JUST_DO_CODING() {

    final String[] cards = {"국민", "신한", "농협"};

    LocalDateTime now = now();

    List<StatisticsCardPayment> statisticsCardPayments = new ArrayList<>();
    StatisticsCardPayment statisticsCardPayment1 = StatisticsCardPayment.builder()
                                                                        .card(cards[0])
                                                                        .itemCode("ic_0001")
                                                                        .itemName("상품명[ic_0001]")
                                                                        .price(100000)
                                                                        .paymentDate(now)
                                                                        .build();
    statisticsCardPayments.add(statisticsCardPayment1);
    
    StatisticsCardPayment statisticsCardPayment2 = StatisticsCardPayment.builder()
                                                                        .card(cards[1])
                                                                        .itemCode("ic_0002")
                                                                        .itemName("상품명[ic_0002]")
                                                                        .price(120000)
                                                                        .paymentDate(now.minusDays(1))
                                                                        .build();
    statisticsCardPayments.add(statisticsCardPayment2);
    
    StatisticsCardPayment statisticsCardPayment3 = StatisticsCardPayment.builder()
                                                                        .card(cards[0])
                                                                        .itemCode("ic_0001")
                                                                        .itemName("특사 상품명[ic_0001]")
                                                                        .price(80000)
                                                                        .paymentDate(now)
                                                                        .build();
    statisticsCardPayments.add(statisticsCardPayment3);
    
    StatisticsCardPayment statisticsCardPayment4 = StatisticsCardPayment.builder()
                                                                        .card(cards[2])
                                                                        .itemCode("ic_0003")
                                                                        .itemName("상품명[ic_0003]")
                                                                        .price(120000)
                                                                        .paymentDate(now.minusDays(4))
                                                                        .build();
    statisticsCardPayments.add(statisticsCardPayment4);
    
    StatisticsCardPayment statisticsCardPayment5 = StatisticsCardPayment.builder()
                                                                        .card(cards[2])
                                                                        .itemCode("ic_0001")
                                                                        .itemName("상품명[ic_0001]")
                                                                        .price(100000)
                                                                        .paymentDate(now.minusDays(4))
                                                                        .build();
    statisticsCardPayments.add(statisticsCardPayment5);

    final Map<CardItemPaymentDto, List<StatisticsCardPayment>> resultMap = new HashMap<>();

    statisticsCardPayments.stream()
                          .forEach(entity -> {
                              CardItemPaymentDto key = CardItemPaymentDto.builder()
                                                                         .card(entity.getCard())
                                                                         .itemCode(entity.getItemCode())
                                                                         .paymentDate(LocalDateTimeForm.SIMPLE_YMD.transform(entity.getPaymentDate()))
                                                                         .build();
                              List<StatisticsCardPayment> values = resultMap.get(key);
                              if(values == null) {
                                // 없으면 리스트로 넣자.
                                resultMap.put(key, new ArrayList<>(Collections.singletonList(entity)));
                              } else {
                                // 있으면 기존의 리스트에 추가
                                values.add(entity);
                              }
                          });

    System.out.print(resultMap.toString());

    List<AnalysisResult> results = resultMap.entrySet()
                                            .stream()
                                            .map(entry -> AnalysisResult.builder()
                                                                        .cardItemPayment(entry.getKey())
                                                                        .cardPayments(entry.getValue()
                                                                                           .stream()
                                                                                           .map(CardPaymentDto::entityToDto)
                                                                                           .collect(toList()))
                                                                        .build())
                                            .collect(toList());
    System.out.println(results.toString());

    // 특정 조건으로 조회하기
    String targetCard = cards[0];
    String targetItemCode = "ic_0001";
    Predicate<AnalysisResult> wantToFind = analysisResult -> analysisResult.wantToFind(targetCard, targetItemCode, LocalDateTimeForm.SIMPLE_YMD.transform(now));

    AnalysisResult analysisResult = results.stream()
                                           .filter(wantToFind::test)
                                           .findFirst()
                                           .orElseGet(null); // 없으면 null 반환

    System.out.println(analysisResult.toString());
    System.out.println(analysisResult.getCardPayments().toString());

    // 스트림에서 바로 원하는 조건의 cardPayments list를 가져오자.
    List<CardPaymentDto> cardPayments = results.stream()
                                               .filter(wantToFind::test)
                                               // 그냥 한줄로 처리
                                               //.map(AnalysisResult::getCardPayments)
                                               //.flatMap(cardPaymentDtos -> cardPaymentDtos.stream())
                                               .flatMap(dto -> dto.getCardPayments().stream())
                                               .collect(toList());

    System.out.println(cardPayments.toString());
  }

}
```
솔직히 이 때 나도 귀신을 만난 것 같은 버그때문에 그 동료분과 진짜 오랫동안 디버깅을 하며 찾아봤다.       

하지만 그 날 결국 왜 이런 현상이 발생하는지에 대해서 전혀 감을 잡지 못했다가 몇일 지나서야 다른 분의 도움으로 알게 된 케이스이다.             

그 몇일 동안 이 생각때문에 머리가 얼마나 복잡했는지 모른다. 당연히 되야 한다고 생각했던게 안되니깐 답답함이 장난아니였다.            

이런 걸 당해 본 적이 있었어야지.....

이 때까지 전설로만 내려오던 또는 통념적으로만 의례 '그렇구나'라고 짚고 넘어갔던 이 equals와 hashcode에 대해서 다시 한번 생각해 본 경우다.                


