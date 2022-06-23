# refactoring-gogunbuntu
리팩토링 고군분투기

# 공통관심사를 잘 묶어라

## 목적
나는 비전공 개발자다. 그래서 학원을 통해서 개발자로 입문한 케이스이다.

지금은 어떤지 모르겠지만 당시에는 자바에 대한 공부는 진짜 기본적인 것만 알려주고 바로 프레임워크를 공부했다.

~~그때는 strut가 유행했다.~~

SQL이라든가 웹이라든가. 당연히 학원 입장에서는 취업가능한 수료생으로 포장하기 위해서일 것이다.

당연히 깊이가 없었다. 그래서 초기에는 그냥 작동만 하면 된다는 식의 코드 ~~라 쓰고 똥이라 읽는다.~~를 양산했다.

이후 어느 정도 짬이 차고 책을 읽기 시작했는데 그때 만난 책중 하나가 조슈아 블로크의 [이펙티브 자바]였다.

이 책만 3권을 샀다. 1판 2판 그리고 자바8이후 나왔던 최신 개정판.

근데 이 책을 읽으면서도 이걸 어떻게 실무에 적용하지라는 고민을 하면서도 실상 실무에서 제대로 적용해 본 적이 없다.

또한 실무를 하다보면 그 내용은 '먹는 건가요'로 끝나고 만다.      

왜냐하면 대부분 스프링 프레임워크를 사용한 개발이 주류이기 때문이다.      

그리고 한글이 이렇게 어렵구나라는 것을 다시 한번 느끼게 하는 책이기도 하다.

이번 이야기는 리팩토링을 하면서 이 내용을 어느정도 적용해 보는 시간이 되기도 했다. 어설프나마....

브랜치 명이 follow the rule로 지었지만 딱히 뭐라 지을 방법이 없어서 포골적인 이름으로 지었다.

내용이 약간 길수 있어서 지루할 수 있지만 한번 읽어보면 나름 쓸만하지 않을까 생각한다.

여기에 공개된 내용은 이미 실무에서 적용하는 부분일 수 있다.       

그래도 관련 내용에 대한 추가적인 설명을 담은 테스트 코드를 소스로 남긴다.


## Entity와 DTO의 기능을 명확하게 분리하라.

뭔가 명령조이다. 하지만 이 이야기를 하는 이유는 실제로 코드를 분석하고 리팩토링하는 과정에서 느낀 무언가가 있어서다.

'설마 이렇게 사용하겠어?'

하지만 여지없이 그렇게 사용된 코드를 봤다.!!! ~~심봤다.!!~~

개발자마다 다른 관점으로 바라볼 수 있는 내용인데 나의 경우에는

'entity는 정말 필요한 경우가 아니면 원본 데이터를 유지하라'

이 룰을 지킨다.

예를 들면 JPA를 쓰든 myBatis를 쓰든 다음과 같은 엔티티와 dto가 있다.

```Java
/**
 * pizza entity
 */
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Pizza {

    @Id
    private String id;

    /** 피자 이름 - 슈프림, 쉬림프, 페퍼로니 등등 */
    private String name;

    /** 도우 : 호밀, 일반, 흑미 등등 */
    private String dough;

    /** 가격 */
    private long price;

    private String maker;

}


/**
 * pizza Dto
 */
@Setter
@Getter
public class PizzaDto {

    private String id;

    /** 피자 이름 - 슈프림, 쉬림프, 페퍼로니 등등 */
    private String name;

    /** 도우 : 호밀, 일반, 흑미 등등 */
    private String dough;

    /** 가격 */
    private String price;

    /** 그 무언가가 될 정보 */
    private String doSome;

}
```

엔티티와 dto에서 보면 차이점이 하나 눈에 보인다. 바로 변수 'price'이다.

여기서 dto는 뷰로 보내주기 위한 객체로 뷰에서 이 변수에 금액을 표시하는 numberFormat을 적용시켜 보내주고 싶었다는 것을 알 수 있다.

그래서 신입개발자 분께서는 엔티티에서 dto로 변환시 힘을 들이지 않기 위해서 엔티티 객체에 다음과 같은 코드를 하나 넣는다.

```java
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Pizza {

    /** 가격 */
    private long price;
    public String getPrice() {
        return new DecimalFormat("#,##0").format(this.price);;
    }
}
```

당연히 이 부분만 보면 아무 문제가 없는 코드이다.

재미있는 것은 이 경우가 딱 한가지 경우라서 해당 엔티티에 작성된 메소드를 다른 곳에서 사용할 일이 없었다는 것이다.

~~데자뷰: 하지만 추가 요청사항이 발생해 결국 이 것은 다른 곳에서 쓰이게 된다~~

무엇이 문제일까?

```Java
@RequiredArgsConstructor
public class PizzaService {

    private final PizzaRepository pizzaRepository

    public Option<Pizza> fetchPizza(String id) {
        return pizzaRepository.findById(id);
    }

}

```
만일 어디선가 이 서비스를 사용할 일이 생긴다면 문제가 될 소지가 있다.

어떤 어플에서 주문이후에 해당 고객에게 최종 주문서 정보를 알려준다고 상상해 보자.

이 서비스를 통해 가져온 피자의 가격이 long이길 바랬건만 getter를 통해 가져오는 순간 어떤 이슈를 만날 수 있기 때문이다.

```
optionPrice = 500;

long totalPrice = pizza.getPrice() + optionPrice;
```

'으잉? 왠 뻘겋게 에러가 나고 있는거지?'

결국 나는 이것을 위해 price를 가져와 replace로 ','를 지우고 Long으로 다시 파싱해서 계산해야만 한다.

### Head First처럼 어떤 방법이든 바보같은 것은 없다.

만일 '내가 신입이라면' 이라는 관점에서 다음과 같은 생각을 할 수 있다.

'그러면 엔티티에 get를 오버라이딩 하지 말고 다음과 같은 메소드 하나 추가 하면 되겠지?'

```java
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Pizza {

    /** 가격 */
    private long price;
    public String getFormattedPrice() {
        return new DecimalFormat("#,##0").format(this.price);
    }
}

```

~~오 나름 괜찮은데??~~

물론 나쁜 방식은 아니다. 나름대로 이유가 있는 방법이지만 다른 곳에서 사용할 때 이 메소드가 필요없다면 의미없는 코드 뭉치에 지나지 않는다.

또한 이 코드를 여러 dto에서 사용했는데 그 중에 하나가 이 부분을 변경해야 하는 상황이 발생한다면. 결국 이 메소드를 수정해야 하는 상황이 발생할 것이다.     

수정한다면 벌어질 수 있 side-effect는 고려조차 안할 수도 있다.      

차라리 이런 경우라면 dto에 dto의 성격에 따라서 그것을 수행하도록 위임하는게 최선이다. 비록 반복될 코드라고 하더라도 말이다.

이유는 반복된다 하더라도 변경이 될 경우에는 해당 dto에서만 변경 요청을 수행한다면 끝이다.

결국 엔티티는 원본 데이터만을 제공하며 dto마다 이 값을 뷰로 보내주기 위해 각기 다른 방식들을 사용할 수 있을 것이다.

','를 찍어서 사용자가 보기 편하게 해줄 수도 있고 어느 dto에서는 앞에 currency, 즉 통화 마크를 붙이거나 '원', '달러'등을 뒤에 붙이거나 할 수 있기 때문이다.

어짜피 이것은 뷰단의 클라이언트에서 사용할 정보이다. 작은 의미에서의 SRP를 지키기 위해서 차라리 엔티티는 그대로 두고 뷰단의 클라이언트로 정보를 넘겨줄 해당 dto로 코드를 옮기자.


```java
/**
 * pizza Dto
 */
@Setter
@Getter
public class PizzaDto {

    /** 피자 이름 - 슈프림, 쉬림프, 페퍼로니 등등 */
    private String name;

    /** 도우 : 호밀, 일반, 흑미 등등 */
    private String dough;

    /** 가격 */
    private long price;

    @JsonProperty("formattedPrice")
    public String getFormattedPrice() {
        return new DecimalFormat("#,##0").format(this.price);
    }

    /** 그 무언가가 될 정보 */
    private String doSome;

}

```

만일 컨트롤러를 통해 응담을 client로 줄때 원본 가격을 사용할 일이 있으면 dto에서는 long타입의 price와 formattedPrice라는 키로 변형된 값에 접근이 가능하다.

물론 뷰단에서 long타입의 price가 필요하지 않다면 그냥 메소드 명을 'getPrice()'로 생성해도 상관없다.

결국 뷰를 담당하는 클라이언트에서 변경 요청사항이 온다면 엔티티를 수정하는 것보다는 dto에서 그 기능을 담당하게 해서 빠르게 변경 사항을 대처할 수 있다.

엔티티에서 이것을 담당한다면 이것이 문제 될 코드가 아나리고 해도 다른 곳에서 이 엔티티를 사용할 때 side-effect 를 고려해봐야 하기 때문이다.

side-effect는 변경 요청에 대해서 어떻게 발생할 지 알 수 없다.      

~~발생할 수도 있고 안할 수도 있습니다.~~

모든 프로그램의 소스를 다 파악한다 해도 놓칠 수 있는 휴먼 미스테이크는 항상 발생할 수 있다.

그래 리팩토링하면서 이것이 여기저기 퍼져있어서 요청사항에 대한 변경이 상당히 힘들어 질수 있겠다는 생각을 하게 되었다.

결국 다 찾아서 뜯어고쳤다.


# 강조되고 반복되는 코드는 유지보수를 불안하게 해요

이게 무슨 말이냐?

## 1. 공통관심사를 묶은 UtilClass를 작성하라

회사에서는 현재 RabbitMQ를 통해 메세지큐를 사용하고 있다.

큐를 보낼 때 객체 정보를 object -> json string으로 받을 때는 json string -> obejct 로 serialize/deserializ가 발생한다.

코드 예제로 하나 보자.


```java

public class Service() {

    // do something

    public void queue1(Pizza pizza) {
        String message;
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            message = objectMapper.writeValueAsString(pizza);
        } catch (JsonProcessingException e) {
            throw new JsonConvertException(e.getMessage());
        }
        message.sendQueue(message);
    }

    public void queue1(Cola cola) {
        String message;
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            message = objectMapper.writeValueAsString(cola);
        } catch (JsonProcessingException e) {
            throw new JsonConvertException(e.getMessage());
        }
        message.sendQueue(message);
    }

}

```

여기까진 이해했지만 이 큐를 받는 리스너에서는?

```java
public class QueueListener() {

    // do something
    public void consumePizza(String message) {
        Pizza pizza;
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            pizza = objectMapper.readValue(message, Pizza.class);
        } catch (IOException e) {
            throw new JsonConvertException(e.getMessage());
        }
        consumer.consume(pizza);
    }

    public void consumeCola(String message) {
        Cola cola;
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            cola = objectMapper.readValue(message, Cola.class);
        } catch (IOException e) {
            throw new JsonConvertException(e.getMessage());
        }
        consumer.consume(cola);
    }

}

```
아닐거야 이건 꿈일거야라고 생각하나?      

실제로 이렇게 코딩을 해놨다.      

사실 이쯤대면 이것을 어떻게 해결할 수 있을까 고민을 해야 할 시점이라는 것을 느껴야 하는데 느끼지 못했던 것일까??

일단 이런 상황이 발생하게 된 이유는 변명이겠지만 난 분명 이것을 util로 만들어서 제공했다.

~~단지 가이드만 안줬을 뿐~~

즉 위에서처럼 어떤 dto나 어떤 클래스에 책임을 위임하는 방식을 사용할 수 없을 때는 UtilClass가 담당하게 하는 게 좋다.

조슈아 블로크 [이펙티브 자바]를 보면 챕터중에 '인스턴스화를 막으려거든 private 생성자를 사용하라'가 있다.

정적 메소드와 정적 필드만을 담은 클래스에 대한 내용인데 이제부터는 이렇게 산재되어 있는 반복되는 코드를 유틸클래스로 만들 필요성이 있다.

1. 이런 유틸리티 클래스는 인스턴스로 만들어 쓰려고 설계한게 아니다.
2. 추상 클래스로 만드는 것으로는 인스턴스화를 막을 수 없다.
3. private 생성자를 추가하면 클래스의 인스턴스화를 막을 수 있다.
4. 이 경우에는 인스턴스화를 막으며 상속이 불가능해진다.

일반적으로 이런 경우에는 사이트마다 다르겠지만 이런 클래스를 많이 볼 수 있다.

일단 맨 위에 dto에 사용했던 코드를 공통으로 사용할 수 있게 만들어 보자.


```java
/**
 * json 공통 유틸
 */
public class JsonUtils {

    /**
     * number type object to number format String
     * @param obj
     * @return String
     */
    public static String numberFormat(Object obj) {
        return new DecimalFormat("#,##0").format(obj);
    }

}
```
물론 Object의 값이 number형식이 아니라면 'Cannot format given Object as a Number' 에러를 뱉는다.

따라서 try - catch로 묶어서 에러가 나면 에러를 뱉기 보다는 null이나 "0"으로 바꿔 주던가 Object의 타입을 instanceof로 조건 필터를 주는 방법이 있다.

현재 회사에서는 이런 경우 "0"으로 반환하도록 요구사항이 있지만 에러처리 방식에 따라 바뀔 수 있으니 그냥 저 상태로 두겠다.

하지만 지금 이 상태의 코드는 이펙티브 자바의 저 챕터에서 언급하는 내용으로 볼때는 안전한 클래스가 아니다.

즉 위에 언급한 3번째 방법을 통해서 인스턴스화를 막아 상속도 불가능하게 만드는 방식을 취한다.

물론 이것을 상속할 사람이 있을까?

지금까지 그런 분들을 한번도 본적이 없다. 하지만 미래는 모른다.

~~딱히 상속한다 해도 문제될게 있나요? 잘 모르겠네요?~~


```java

public class JsonUtils {

    /** 3. private 생성자를 추가하면 클래스의 인스턴스화를 막을 수 있다. */
    private JsonUtils() {}

}


@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class JsonUtils {

}
```

아마도 롬복을 많이 쓸테니 어노테이션을 다음과 같이 줘서 빈 생성자를 만들 떄 AccessLevel을 private주자.

근데 신입 개발자분이 이런 질문을 한다.

'헛!!! 그런데 변환할 타입이 많아지면 반환할 타입만큼 저 메소드를 생성하는건 아닌거 같아요'


```java

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class JsonUtils {

    public static Pizza convertPizza(String content) {
        Pizza pizza;
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            pizza = objectMapper.readValue(content, Pizza.class);
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
        return pizza;
    }

    public static Cola convertCola(String content) {
        Cola cola;
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            cola = objectMapper.readValue(content, Cola.class);
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
        return cola;
    }

    // 그럼 Deserialize할 객체만큼 이 메소드를 생성한다고??

}
```

당연히 어림도 없지!

역시 [이펙티브 자바]에는 제네릭에 대한 내용을 상당부분 할애한다. 이 책의 내용을 참고하고 있지만 소개하는 곳은 아니기에 책을 구입하면 좋다.

그중 [이왕이면 제너릭 타입으로 만들어라]와 [이왕이면 제너릭 메소드로 만들어라]부분을 참고해 보자.

신입분들에게 제네릭에 대해서 이야기해보면 의외로 잘 모른다. 그냥 제네릭하다. 제네릭하게 쓴다는 표현을 말하는 것이고 다음 코드를 보면

```java
List<Pizza> list =  new ArrayList<>();

// 라든가

for(Pizza pizza : list) {
    // do something
}

```
위 코드를 보면 '이게 제네릭 아닌가요?'라고 한다.

지금에 와서 고백하는데 물론 나도 아주 오랬동안 그래왔고 그것으로 제네릭에 대해 안다고 생각했다.

제공했던 원래 유틸은 다음과 같다.


```java
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class JsonUtils {

    private static ObjectMapper OBJECT_MAPPER = null;

    private static ObjectMapper mapper() {
        if(OBJECT_MAPPER == null) {
            OBJECT_MAPPER = new ObjectMapper();
            OBJECT_MAPPER.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            OBJECT_MAPPER.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
            OBJECT_MAPPER.setSerializationInclusion(JsonInclude.Include.NON_NULL);
            OBJECT_MAPPER.enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT);
        }
        return OBJECT_MAPPER;
    }

    /**
     * 객체를 json string으로 변환한다.
     * @param object
     * @return String
     * @throws RuntimeException
     */
    public static String toJson(Object object) {
        String result;
        try {
            result = mapper().writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e.getMessage());
        }
        return result;
    }
}
```
잭슨 아저씨가 제공해 주는 ObjectMapper의 경우에는 글로벌하게 설정할 수 있지만 해당 유틸 클래스에서만 따로 옵션을 줘 정의할 필요성이 있어서 삽입한 코드이다.

또한 객체가 있다면 생성된 객체를 주고 없으면 새로 만들어 줘 불필요한 객체 생성을 막아주는 방식으로 사용한다.

나름 [이펙티브 자바]의 내용을 어느정도 참조한 부분이기도 하다.

어째든 이 부분은 입맛에 맞춰 코드를 수정할 수 있다.

어짜피 객체를 json string 형식으로 변환하는 것은 제네릭할 필요는 없다.

다만 다음 코드는 제네릭을 활용해서 만든 메소드이다.

```java

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class JsonUtils {

    private static ObjectMapper OBJECT_MAPPER = null;

    private static ObjectMapper mapper() {
        if(OBJECT_MAPPER == null) {
            OBJECT_MAPPER = new ObjectMapper();
            OBJECT_MAPPER.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            OBJECT_MAPPER.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
            OBJECT_MAPPER.setSerializationInclusion(JsonInclude.Include.NON_NULL);
            OBJECT_MAPPER.enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT);
        }
        return OBJECT_MAPPER;
    }

    /**
     * 객체를 json string으로 변환한다.
     * @param object
     * @return String
     * @throws RuntimeException
     */
    public static String toJson(Object object) {
        String result;
        try {
            result = mapper().writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e.getMessage());
        }
        return result;
    }

    /**
     * json String을 넘겨받은 객체 class타입으로 변환한다.
     *
     * @param content
     * @param clazz
     * @return T
     * @throws RuntimeException
     */
    public static <T> T convertObject(String content, Class<T> clazz) {
        T object;
        try {
            object = mapper().readValue(content, clazz);
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
        return object;
    }

    /**
     * Generic Collection Type covert method
     * 만일 특정 객체의 컬렉션 타입인 경우 해당 메소드를 사용한다.
     * @param content
     * @param clazz
     * @return T
     * @throws RuntimeException
     */
    public static <T> T convertObjectByTypeRef(String content, TypeReference<T> clazz) {
        T object;
        try {
            object = mapper().readValue(content, clazz);
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
        return object;
    }
}
```
convertObjectByTypeRef메소드는 String형태의 값이 어떤 객체의 리스트를 표현하기때문에 List<T>형태로 반환할 필요가 있을 경우 사용하게 된다.

```Java

String pizzaListStr = doSome;
TypeReference<List<Pizza>> typeRef = new TypeReference<>() {};
List<Pizza> pizzaList = convertObjectByTypeRef(pizzaListStr, typeRef);

```

결국 다음과 같이 코드가 수정된다.
```java
public class QueueListener() {

    // do something
    public void consumePizza(String message) {
        Pizza pizza = convertObject(message, Pizza.class);
        consumer.consume(pizza);
    }

    public void consumeCola(String message) {
        Cola cola = convertObject(message, Cola.class);
        consumer.consume(cola);
    }

}

```

확실히 가독성이 좋아진다.

단순한 제네릭 메서드면 이 정도면 충분하고 이로 인해서 타입 안전하고 쓰기도 쉽다정도로 책에서는 말한다.

실제로 catch부분에서는 RuntimeException을 발생하는데 오류에 대한 빠른 대응을 위해서는 이보다는 RuntimeException나 IOException을 상속해서

JsonParsingException같은 exception을 따로 정의해서 사용하는 것을 추천한다.

아니면 null을 반환해서 null check를 호출한 곳에서 검증해 사용하는 것도 상관없다. 어디까지나 어떤 방식을 취하냐는 것은 어플리케이션의 상황에 따라가야한다.

아울러 [이펙티브 자바]에서는 제네릭과 관련해 많은 정보를 얻을 수 있기 때문에 이 부분을 상세하게 보는 것을 추천한다.


## 2. 반복되는 패턴의 하드코딩으로 어떤 값을 가져오는 코드라면 enum을 적극 활용하자.

mySql의 datatime컬럼을 통해서 가져온 데이터를 가공할 일이 많았다.

예를 들면 LocalDateTime으로 부터 'MMddHHmmssSSS'를 가져와서 채번을 할때 유니크함을 위해 사용한다던가 응답애 담을 각 dto마다 표현할 패턴에 따라서

변환해서 보여주는 부분이 상당히 많았다.

보니깐 신입 개발자분이 DateUtil이라는 클래스를 다음과 같이 만들어서 사용하고 있다.

```java
/**
 * date utils
 */
public class DateUtils {

    /**
     * pattern에 맞춰 LocalDateTime -> String 형식으로 변환
     * @param localDateTime
     * @param pattern
     * @return String
     */
    public static String localDateTimeToStringWithPattern(LocalDateTime localDateTime, String pattern) {
        try {
            return localDateTime.format(DateTimeFormatter.ofPattern(pattern).withLocale(new Locale("ko")));
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * String to LocalDate with Pattern
     * @param localDate
     * @param pattern
     * @return LocalDate
     */
    public static LocalDate localDateWithPattern(String localDate, String pattern) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(pattern);
        return LocalDate.parse(localDate, dateTimeFormatter);
    }

}
```
@NoArgsConstructor(access = AccessLevel.PRIVATE)을 사용하게 했다.

그리고는 다음과 같이 필요하면 해당 메소드를 사용하고 있었다.

```java

String format1 = localDateTimeToStringWithPattern(localDateTime, "yyyyMMdd");
String format2 = localDateTimeToStringWithPattern(localDateTime, "yyyy-MM-dd HH:mm");
String format3 = localDateTimeToStringWithPattern(localDateTime, "yyyy.MM.dd HH:mm:ss");
String format4 = localDateTimeToStringWithPattern(localDateTime, "yyyy/MM/dd");

```

일단

```java
DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(pattern);
return LocalDate.parse(localDate, dateTimeFormatter);
```

이런 코드가 비지니스 로직상에 없다는 것만으로도 아주 박수를 쳐줄 만하다.

하지만 저기 패턴에 들어가는 부분이 너무 맘에 걸린다.

그래서 enum클래스에서 저것을 이용해 좀 더 직관적으로 사용할 수 있게 리팩토링을 한다.


```java
/**
 * LocalDateTimeForm pattern
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

    /** e.g 오후 2022년 6월 24일 8:18:38 */
    AMPM_MARK("a yyyy년 M월 d일 K:mm:ss");

    @Getter
    private String pattern;

}

```

이제는 이것을 이용해 다음과 같이 메소드를 하나 생성해 보자.

```java

/**
 * enum의 패턴에 맞춰서 LocalDateTime을 스트링으로 변환한다.
 * @param localDateTime
 * @return String
 */
public String transform(LocalDateTime localDateTime) {
    return localDateTimeToStringWithPattern(localDateTime, this.pattern);
}

```

이렇게 정의를 한다면 LocalDateTime으로부터 내가 원하는 패턴의 스트링 정보를 얻겠다면


```java
String format4 = LocalDateTimeForm.YYYY_MM_KO.transform(localDateTime);
```
위 코드처럼 좀 더 직관적이고 내가 패턴을 일일히 하드코딩으로 작성하는 것보다는 실수를 줄일 수 있다.

패턴이 필요하다면 enum에 정의하고 그저 나는 해당 enum으로부터 꺼내쓰면 그만이다.

~~이제 패턴을 하드코딩하는 것은 안녕~~

# At A Glance

최대한 실무에서 작성된 코드를 대상으로 비슷하게 재구성했다.

어찌 보면 아주 작은 부분일 수 있지만 탄탄한 코드를 작성하는 것은 협업하는 과정에서 큰 힘을 발휘한다.

이와 관련해서 추가적인 설명을 담은 테스트 코드도 함께 소스로 남겨본다.       



