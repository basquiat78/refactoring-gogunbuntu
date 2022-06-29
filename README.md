# refactoring-gogunbuntu
리팩토링 고군분투기

# 사람들이 많이 쓰는 라이브러리를 써보자.

jpa가 아니더라도 어떤 객체에서 dto같은 객체로 정보를 옮기는 경우 많이 쓰는 라이브러리들이 있다.

그 중에 가장 많이 알려진 MapStruct를 사용해서 지루하고 반복되는 코드를 줄여보는게 목적이다.

흔한 정보를 담는 내용이고 이와 관련해서 구글에게 관련 정보를 물어봐도 잘 나온 기술 블로그도 상당히 많지만 그럼에도 불구하고 기록으로 남기기 위함이다.

# Entity or DTO

이전에는 ModelMapper라는 녀석을 통해서 entity에서 dto 또는 그 반대의 경우를 쉽게 만들었었다.

그러다가 이와 비슷한 MapStruct가 있고 성능이 훨씬 좋다는 글을 보게 되었다.

사실 리플렉션을 이용한 ModelMapper의 속도가 느리다고 알려져 있는데 이에 대해 고민한 적이 없다.      

왜냐하면 속도/성능은 일단 먹는건가요 라는 스탠스로 던져버리고 잘 되기만 하면 된다는 마인드?      

아마도 리플렉션으로 인한 속도 성능의 저하보다는 다른 부분일 것이라고 추측하지만 첫 단추를 이걸로 했으니깐.

물론 리플렉션으로 객체를 계속적으로 생성하고 private 변수에 접근하기 위한 getDeclaredFields가 엄청 사용될 것이다.

값 세팅을 위해 setAccessible(true)와 인스턴스 비교...... perm 에러가 나는 경우도 있다고 했는데 본적이 없어서.....

그래도 유의미하든 무의미하든 성능을 조금이라도 최적화 할 수 있다면 좋은 거 아닐까?

어째든 흔히 말하는 Boilerplate Code를 제거하기 위해서인데 JPA의 경우에는 정말 필요하지 않는 경우에는 Setter를 두는 것을 권장하지 않는다.

또한 이번에 신입분들이 작업한 백엔드 쪽의 소스들을 살펴보면서 dto 변환 코드가 여기저기 산재해 있다.

게다가 가끔은 '이걸 꼭 dto로 변환해야되?'라는 생각을 하기도 한다.

물론 의도치 않은 dirty checking을 경험하게 되면 또 해야할 거 같고.

게다가 서비스 오픈 이후 계속되는 변경/요구 사항들이 늘어나면서 엔티티에서 dto로 변환한다든가 dto에서 다른 dto로 변환하는 코드를 계속 만져야 하는 상황이 왔다.

결국 지루하고 반복되지만 꼭 해야만 하는 보일러플레이트 코드가 발생할 수 밖에 없다.

햔재까지는 이와 같은 매퍼 라이브러리를 사용하지 않았기 때문에 이런 것이 있다는 것도 알려줄 겸 리팩토링을 하게 되었다.

# 그래서 MapStruct쓰면 좀 나아지나?

일단 이런 것을 써야하는 이유가 먼저이다.

왜 이것을 써야 하는지 불편한게 무엇인지 알아야 좀 더 게을러질 수 있기 때문이다.

게으르다는 것은 '필요하지만 지루하고 반복되는 보일러플레이트 코드'를 개선해서 다른 곳에 더 집중하기 위해서이다.

자 그럼 이전에는 이것을 어떻게 했을까??

여기 [첫 번째 브랜치 follow-the-rule](https://github.com/basquiat78/refactoring-gogunbuntu/tree/follow-the-rule) 에서 사용한 엔티티와 dto를 보자.

```java
/**
 * pizza entity
 * builder의 경우에는 현재 선언된 모든 변수를 인자로 받는 생성자에 대해 빌드 패턴이 적용된다.
 * 만일 수많은 변수들중 특정 인자들로만 빌드 패턴을 적용하고 싶다면
 * <pre>
 *   @ToString
 *   @NoArgsConstructor(access = AccessLevel.PROTECTED)
 *   public class Pizza {
 *
 *       @Builder
 *       public Pizza(String name) {
 *           this.name = name;
 *       }
 *
 *       @Getter
 *       private String name;
 *
 *       @Getter
 *       private String dough;
 *
 *   }
 * <pre>
 *
 * 빌드 패턴을 적용한 생성자에 어떤 조건을 주고 싶다면 if문을 활용해 IllegalArgumentException같은 것을 던지거나 
 * Assert를 통해서 이것을 대신할 수도 있는 장점이 있지만 여기서는 그냥 간단하게 진행한다.
 *
 */
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Pizza {

  /** 피자 이름 - 슈프림, 쉬림프, 페퍼로니 등등 */
  @Getter
  private String name;

  /** 도우 : 호밀, 일반, 흑미 등등 */
  @Getter
  private String dough;

}

/**
 * pizza Dto
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PizzaDto {

  /** 피자 이름 - 슈프림, 쉬림프, 페퍼로니 등등 */
  private String name;

  /** 도우 : 호밀, 일반, 흑미 등등 */
  private String dough;

}
```
앞서 언급했듯이 나는 개인적으로 엔티티쪽은 특별한 경우가 아니라면 그냥 그 역할에 충실하게 만들고 일반적으로 dto에 다음과 같은 코드를 넣는 것을 선호한다.

```java
/**
 * entity -> dto
 * @param entity
 * @return PizzaDto
 */
public static PizzaDto entityToDto(Pizza entity) {
    return PizzaDto.builder()
                   .name("entity -> dto : " + entity.getName())
                   .dough("entity -> dto : " + entity.getDough())
                   .build();
}
```   

물론 그 반대로 entitiy쪽에서 해도 무방하다. 하지만 이건 어디까지나 개발자의 선택일 뿐.

누군가는 entityToDto는 엔티티쪽에 dtoToEntitiy는 dto쪽에 넣는 분도 봤는데 그것도 어디까지나 선택사항이다.

어떻게 하든 어디까지나 여러분의 선택사항이다.

```java
/**
 * pizza Dto
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PizzaDto {

  /** 피자 이름 - 슈프림, 쉬림프, 페퍼로니 등등 */
  private String name;

  /** 도우 : 호밀, 일반, 흑미 등등 */
  private String dough;

  /**
   * entity -> dto
   * @param entity
   * @return PizzaDto
   */
  public static PizzaDto entityToDto(Pizza entity) {
    return PizzaDto.builder()
                   .name("entity -> dto : " + entity.getName())
                   .dough("entity -> dto : " + entity.getDough())
                   .build();
  }

  /**
   * dto -> entity
   * @return Pizza
   */
  public Pizza dtoToEntity() {
    return Pizza.builder()
                .name("dto -> entity : " + this.name)
                .dough("dto -> entity : " + this.dough)
                .build();
  }

}

```

아마두?

잘 작동하는가? 두말하면 잔소리 그냥 확인해 보자.

```java
class SimpleTest {

  @Test
  void Convert_Test() {

    Pizza pizza = Pizza.builder()
                       .name("고구마피자")
                       .dough("흑미")
                       .build();
    System.out.println(pizza.toString());
    PizzaDto dto = PizzaDto.entityToDto(pizza);
    System.out.println(dto.toString());
    Pizza again = dto.dtoToEntity();
    System.out.println(again.toString());
  }

}
```

```
result:

Pizza(name=고구마피자, dough=흑미)
PizzaDto(name=entity -> dto : 고구마피자, dough=entity -> dto : 흑미)
Pizza(name=dto -> entity : entity -> dto : 고구마피자, dough=dto -> entity : entity -> dto : 흑미)
```

문득 이런 질문을 할 수 있다.

'이런 예제로만으로는 굳이 MapStruct를 써야할 이유가 있나?'

예제로만 본다는 굳이 MapStrcut를 써서 그에 맞는 관련 API를 보고 작성하는 노력을 할 이유가 없다.

물론 지금이야 선언된 변수가 많지 않으니 불편하지 않을 것이다.

하지만 실무에서 어떤 database로부터 가져온 entity에 선언된 변수들은 딸랑 2, 3개만 있지 않을 것이다.

하지만 적든 많든 일단 저렇게 만들어 놓으면 끝이라고 생각하면 그거슨 큰 오산이다.

보통 사이트에 따라 다르겠지만 선언된 변수가 10개가 훨씬 넘는 경우도 상당히 많다.

빌드 패턴을 사용하든 그냥 생성자를 통해 dto나 엔티티를 생성후에 set으로 일일이 한땀한땀 넣어준다면 이게 여간 귀찮은게 아니다.       

게다가 앞으로 어떤 요구사항이 올지도 모른다. 지금 당장 안해도 차후에 이런 지루한 작업을 또 해야할 수 도 있다는 의미이다.     

그리고 이커머스쪽이라면 어떨까? 아시는 분들은 공감할 것이고 모르신다면 상상을 해보시면 될거 같다.

스타트업이나 회사의 사업 자체가 빠르게 변하거나 변경을 요구한다면 저것은 결국 계속 관리를 해줘야 한다.

~~추가하거나 삭제하거나~~

그렇다는 것은 예기치 않은 휴먼 미스테이크가 발생할 소지가 충분하다.

일단 만들어 놓기만 하면 수정하는 것은 일이 아니라고 할 수 있겠지만

'나는 그런거 신경안쓰고 싶은데? 알아서 해주는거 없을까?'

이런 생각이 들었다면 MapStruct를 통해 이것을 털어버리자.

gradle에서 다음과 같이 dependency를 추가한다.

```javascript
plugins {
    id 'org.springframework.boot' version '2.7.1'
    id 'io.spring.dependency-management' version '1.0.11.RELEASE'
    id 'java'
}

group 'io.basquiat'
version '1.0-SNAPSHOT'
sourceCompatibility = '11'

repositories {
    mavenCentral()
}

dependencies {

    implementation 'org.springframework.boot:spring-boot-starter-web'

    compileOnly group: 'org.projectlombok', name: 'lombok', version: '1.18.24'
    implementation group: 'org.mapstruct', name: 'mapstruct', version: '1.5.2.Final'
    implementation group: 'org.projectlombok', name: 'lombok-mapstruct-binding', version: '0.2.0'

    annotationProcessor group: 'org.projectlombok', name: 'lombok', version: '1.18.24'
    annotationProcessor group: 'org.mapstruct', name: 'mapstruct-processor', version: '1.5.2.Final'
    annotationProcessor group: 'org.projectlombok', name: 'lombok-mapstruct-binding', version: '0.2.0'

    testImplementation('org.springframework.boot:spring-boot-starter-test') {
        exclude group: 'org.junit.vintage', module: 'junit-vintage-engine'
    }

}

test {
    useJUnitPlatform()
}
```
lombok-mapstruct-binding은 스택오버플로우나 다른 분들의 블로그를 보면 순서로 인해 문제가 발생할 수 있기 때문에 순서상관없이 문제를 해결할 수 있게 만드는 라이브러리이다.

이제 Mapper를 하나 만들어 보자

```java
@Mapper
public interface PizzaMapper {

  PizzaMapper INSTANCE = Mappers.getMapper(PizzaMapper.class);

  @Mapping(target = "name", expression = "java(formatEntityName(dto.getName()))")
  @Mapping(target = "dough", expression = "java(formatEntityDough(dto.getDough()))")
  Pizza dtoToEntity(PizzaDto dto);

  default String formatEntityName(String name) {
    return "dto -> entity : " + name;
  }

  default String formatEntityDough(String dough) {
    return "dto -> entity : " + dough;
  }

  @Mapping(target = "name", expression = "java(formatDtoName(entity.getName()))")
  @Mapping(target = "dough", expression = "java(formatDtoDough(entity.getDough()))")
  PizzaDto entityToDto(Pizza entity);

  default String formatDtoName(String name) {
    return "entity -> dto : " + name;
  }

  default String formatDtoDough(String dough) {
    return "entity -> dto : " + dough;
  }

}

```

기존의 코드를 그대로 옮기기 위해서 expression에서 특정 표현식을 사용했다.

```java
/**
 * pizza Dto
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PizzaDto {

  /** 피자 이름 - 슈프림, 쉬림프, 페퍼로니 등등 */
  private String name;

  /** 도우 : 호밀, 일반, 흑미 등등 */
  private String dough;

  /**
   * entity -> dto
   * @param entity
   * @return PizzaDto
   */
  public static PizzaDto entityToDto(Pizza entity) {
    return PizzaMapper.INSTANCE.entityToDto(entity);
  }

  /**
   * dto -> entity
   * @return Pizza
   */
  public Pizza dtoToEntity() {
    return PizzaMapper.INSTANCE.dtoToEntity(this);
  }

}
```
dto에서는 다음과 같이 처리하면 코드 한줄로 모든 것이 끝나버렸다.

또한 내가 복잡한 dto내에서 해당 코드를 손댈 필요가 없다.

프론트엔드 또는 view에 요청에 의해 dto에 변수가 추가되더라도 내가 더이상 손 될 것은 없다.

물론 맵퍼 안에 몇가지 테스트를 위한 코드가 들어가 그렇긴 하지만 어째든!

근데 문득 이런 생각이 들 것이다.

'아니 그러면 엔티티가 몇개일지 모르는데 그 엔티티마다 dto로 매핑할려면 저 mapper를 구현해서 일일히 등록해야 하는 것이냐?'

어쩔 수 없지 않을까? 모든 것에는 trade-off가 존재하는데?

하지만 비슷한 도메인일 경우에는 묶어서 표현할 수 있을수 있다.

예를 들면 다음과 같이

```java
@Mapper
public interface PizzaMapper {

  PizzaMapper INSTANCE = Mappers.getMapper(PizzaMapper.class);

  @Mapping(target = "name", expression = "java(formatEntityName(dto.getName()))")
  @Mapping(target = "dough", expression = "java(formatEntityDough(dto.getDough()))")
  Pizza dtoToEntity(PizzaDto dto);

  @Mapping(target = "name", expression = "java(formatEntityName(dto.getName()))")
  @Mapping(target = "dough", expression = "java(formatEntityDough(dto.getDough()))")
  PizzaTwo dtoToEntity(PizzaDtoTwo dto);

  default String formatEntityName(String name) {
    return "dto -> entity : " + name;
  }

  default String formatEntityDough(String dough) {
    return "dto -> entity : " + dough;
  }

  @Mapping(target = "name", expression = "java(formatDtoName(entity.getName()))")
  @Mapping(target = "dough", expression = "java(formatDtoDough(entity.getDough()))")
  PizzaDto entityToDto(Pizza entity);

  @Mapping(target = "name", expression = "java(formatDtoName(entity.getName()))")
  @Mapping(target = "dough", expression = "java(formatDtoDough(entity.getDough()))")
  PizzaDtoTwo entityToDto(PizzaTwo entity);

  default String formatDtoName(String name) {
    return "entity -> dto : " + name;
  }

  default String formatDtoDough(String dough) {
    return "entity -> dto : " + dough;
  }

}
```

그레이들이면 build > generated > annotationProcessor 하위 폴더, 메이븐이라면 target폴더에 코드젠이 된것을 볼 수 있다.

아래가 바로 생성된 클래스이다.
```java
package domain.mapper;

import domain.dto.PizzaDto;
import domain.dto.PizzaDtoTwo;
import domain.entity.Pizza;
import domain.entity.PizzaTwo;
import java.text.DecimalFormat;
import javax.annotation.processing.Generated;

@Generated(
        value = "org.mapstruct.ap.MappingProcessor",
        date = "2022-06-29T19:50:51+0900",
        comments = "version: 1.5.2.Final, compiler: IncrementalProcessingEnvironment from gradle-language-java-6.7.jar, environment: Java 11.0.10 (AdoptOpenJDK)"
)
public class PizzaMapperImpl implements PizzaMapper {

  @Override
  public Pizza dtoToEntity(PizzaDto dto) {
    if ( dto == null ) {
      return null;
    }

    Pizza.PizzaBuilder pizza = Pizza.builder();

    pizza.name( formatEntityName(dto.getName()) );
    pizza.dough( formatEntityDough(dto.getDough()) );
    pizza.price( priceToLong(dto.getPrice()) );

    return pizza.build();
  }

  @Override
  public PizzaTwo dtoToEntity(PizzaDtoTwo dto) {
    if ( dto == null ) {
      return null;
    }

    PizzaTwo.PizzaTwoBuilder pizzaTwo = PizzaTwo.builder();

    pizzaTwo.name( formatEntityName(dto.getName()) );
    pizzaTwo.dough( formatEntityDough(dto.getDough()) );
    pizzaTwo.price( priceToLong(dto.getPrice()) );

    return pizzaTwo.build();
  }

  @Override
  public PizzaDto entityToDto(Pizza entity) {
    if ( entity == null ) {
      return null;
    }

    PizzaDto.PizzaDtoBuilder pizzaDto = PizzaDto.builder();

    pizzaDto.price( new DecimalFormat( "#,##0" ).format( entity.getPrice() ) );

    pizzaDto.name( formatDtoName(entity.getName()) );
    pizzaDto.dough( formatDtoDough(entity.getDough()) );

    return pizzaDto.build();
  }

  @Override
  public PizzaDtoTwo entityToDto(PizzaTwo entity) {
    if ( entity == null ) {
      return null;
    }

    PizzaDtoTwo.PizzaDtoTwoBuilder pizzaDtoTwo = PizzaDtoTwo.builder();

    pizzaDtoTwo.price( new DecimalFormat( "#,##0" ).format( entity.getPrice() ) );

    pizzaDtoTwo.name( formatDtoName(entity.getName()) );
    pizzaDtoTwo.dough( formatDtoDough(entity.getDough()) );

    return pizzaDtoTwo.build();
  }
}

```

MapStruct가 해당 mapper를 구현한 구현체를 생성할 텐데 default 메소드를 작성해 expression에서 사용할 수 있도록 한다.

메서드 시그니처로 인해 오버로딩이 가능하니 좀 코드가 지져분하게 보일 여지가 있어도 변경점이 한곳으로 모이기 때문에 유지보수에 도움이 될 수도(?) 있다.

expression이나 여타 옵션들을 제공하기 때문에 입맛에 맞춰 사용할 수 있다.

또한 소스 객체로부터 생성한 타겟 객체로 매핑시 변수명의 갭이 존재한다면 @Mapping안에 source와 target으로 변수명을 설정해 매핑도 가능하다.

한편으로는 expression부분이 저렇게 스트링으로 표현해야해서 오타 발생 및 휴먼 미스테이크로 인해 실행전까지 에러 유무를 알수가 없다.

'차라리 그냥 로우하게 하는게 편하겠는데?'라는 생각이 들 수도 있다.

# MapStruct 사용시 마주할 수 있는 정책과 전략 이슈

```java
/**
 * pizza Dto
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PizzaDto {

  /** 피자 이름 - 슈프림, 쉬림프, 페퍼로니 등등 */
  private String name;

  /** 도우 : 호밀, 일반, 흑미 등등 */
  private String dough;

  /** 가격 */
  private String price;

  /** 그 무언가가 될 정보 */
  private String doSome;

  /**
   * entity -> dto
   * @param entity
   * @return PizzaDto
   */
  public static PizzaDto entityToDto(Pizza entity) {
    return PizzaMapper.INSTANCE.entityToDto(entity);
  }

  /**
   * dto -> entity
   * @return Pizza
   */
  public Pizza dtoToEntity() {
    return PizzaMapper.INSTANCE.dtoToEntity(this);
  }

}

/**
 * pizza entity
 */
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Pizza {

  /** 피자 이름 - 슈프림, 쉬림프, 페퍼로니 등등 */
  @Setter
  @Getter
  private String name;

  /** 도우 : 호밀, 일반, 흑미 등등 */
  @Setter
  @Getter
  private String dough;

  /** 가격 */
  @Setter
  @Getter
  private long price;

  @Setter
  @Getter
  private String maker;

}

```

위 코드처럼 어플리케이션내에서는 항상 entity와 dto는 같은 필드를 갖는다는 것을 보장할 수 없다.

당연한 이야긴데 dto의 특징 자체가 단순하게 entity의 정보를 그대로 가져온다는 개념으로만 보면 백엔드에서 view 또는 프론트에 보낼 수 있는 정보가 한정된다.

그래서 여러 정보들을 조합해 dto에 담아 보내게 되는데 다음과 같다고 생각해 보자.

dto에서는 어떤 비즈니스 로직에 의해 세팅될 doSome이라는 필드가 있고 엔티티에는 해당 피자를 발명한 사람의 정보를 담는다고 해보자.

하지만 해당 dto는 해당 피자를 발명한 사람의 정보는 여기서는 필요하지 않다.

그렇다면 기존의 테스트를 실행하면 어떻게 될까?

100프로 다음과 같은 warning이 뜰것이다.

```
Warning: XXXX java: Unmapped target property: "maker".
Warning: XXXX java: Unmapped target property: "doSome".
```

단순 경고 로그라 실행시 문제될 것은 없다.        

하지만 이런 경고문이 로그로 남는건 문제될게 없다고 해도 찜찜하다.

MapStrcut에는 정책과 전략을 제공한다.

## Policy
- unmappedSourcePolicy
  - default : IGNORE
- unmappedTargetPolicy
  - default : WARN
- typeConversionPolicy
  - default : WARN
    IGNORE, WARN, ERROR 3가지가 적용되는데 그 default설정이 위에서처럼 각기 다르다.


## Strategy
- nullValueMappingStrategy
  - default :  RETURN_NULL
  - RETURN_DEFAULT
- nullValuePropertyMappingStrategy
  - default : SET_TO_NULL
  - SET_TO_DEFAULT
  - IGNORE

때론 엔티티와 dto의 상태를 항상 같게 유지하고 싶은 경우도 있을 것이다.

그렇다면 몇가지 방법이 있는데 Mapper안에 선언한 @Mapping에서 이것을 일일히 적용하는 방법이 있다.

또는 unmappedTargetPolicy정책을 엄격하게 가져기기 위해 default를 WARN에서 ERROR로 변경해 무조건 컴파일시에 에러를 발생시킨다던가?

하지만 여기서는 그렇게 세세한 것보다는 엔티티와 dto는 항상 같은 변수를 가질 필요가 없다거나 할때 Warn을 줄이고자 한다.      

다르다면 @Mapping의 source와 target를 설정해 세팅해 주는 방식으로 가는게 최우선이다.      

다음 밑에서처럼 설정을 해주면 된다.     

Baeldong 사이트를 가보면 이것을 Configuration으로 빼고 이름을 주어 config로 불러오게 하는 방법을 추천한다.

[Ignoring Unmapped Properties with MapStruct](https://www.baeldung.com/mapstruct-ignore-unmapped-properties)


```java
@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PizzaMapper {

  PizzaMapper INSTANCE = Mappers.getMapper(PizzaMapper.class);

  @Mapping(target = "name", expression = "java(formatEntityName(dto.getName()))")
  @Mapping(target = "dough", expression = "java(formatEntityDough(dto.getDough()))")
  @Mapping(target = "price", expression = "java( priceToLong(dto.getPrice()) )")
  Pizza dtoToEntity(PizzaDto dto);

  @Mapping(target = "name", expression = "java(formatEntityName(dto.getName()))")
  @Mapping(target = "dough", expression = "java(formatEntityDough(dto.getDough()))")
  @Mapping(target = "price", expression = "java( priceToLong(dto.getPrice()) )")
  PizzaTwo dtoToEntity(PizzaDtoTwo dto);

  /**
   * 변경이 잘되고 있는지 확인하기 위한 임의 메소드
   * @param name
   * @return String
   */
  default String formatEntityName(String name) {
    return "dto -> entity : " + name;
  }

  /**
   * 변경이 잘되고 있는지 확인하기 위한 임의 메소드
   * @param dough
   * @return String
   */
  default String formatEntityDough(String dough) {
    return "dto -> entity : " + dough;
  }

  @Mapping(target = "name", expression = "java(formatDtoName(entity.getName()))")
  @Mapping(target = "dough", expression = "java(formatDtoDough(entity.getDough()))")
  @Mapping(target = "price", numberFormat = "#,##0")
  PizzaDto entityToDto(Pizza entity);

  @Mapping(target = "name", expression = "java(formatDtoName(entity.getName()))")
  @Mapping(target = "dough", expression = "java(formatDtoDough(entity.getDough()))")
  @Mapping(target = "price", numberFormat = "#,##0")
  PizzaDtoTwo entityToDto(PizzaTwo entity);

  /**
   * 변경이 잘되고 있는지 확인하기 위한 임의 메소드
   * @param name
   * @return String
   */
  default String formatDtoName(String name) {
    return "entity -> dto : " + name;
  }

  /**
   * 변경이 잘되고 있는지 확인하기 위한 임의 메소드
   * @param dough
   * @return String
   */
  default String formatDtoDough(String dough) {
    return "entity -> dto : " + dough;
  }

  /**
   * long에서 string으로 numberFormat적용시 구분자 ','가 들어가기 때문에 다음과 제거후 롱으로 반환한다.
   * @param value
   * @return Long
   */
  default Long priceToLong(String value) {
    return Long.valueOf(value.replace(",", ""));
  }

}
```
저런 것도 할 수 있다는 것을 예제에 추가한 최종 버전이 되시겠다.

일단 뭔가 번잡해 보이는건 함정이지만 기존의 dto와 entity쪽으로 분산된 것이 하나의 변경점으로 모인다는 것은 나름 괜찮다.

그리고 예제 자체가 너무 간결해서 이걸 써야 할 이유를 찾지 못할 수도 있다.

하지만 당장 실무에서 10개 또는 그 이상의 엔티티를 dto로 변환하는 작업을 하게 된다면 어떨까?

```java
// 이러고 싶진 않아...ㅠㅠ
SomeDTO.builder()
       .a(doSomething)
       .b(doSomething)
       .c(doSomething)
       .d(doSomething)
       .e(doSomething)
       .f(doSomething)
       .g(doSomething)
       .h(doSomething)
       .i(doSomething).j(doSomething).k(doSomething)................
       .build();
```

하나에 몰아놓기 보다는 그냥 관리차원에서 나눠서 작성하는 것이 작성해야할 인터페이스가 늘어날 지언정 변경에 유연할 수 있다는 점은 분명 매력적인 부분이다.

현재 남겨진 소스는 dto내부에 변환해주는 메소드를 넣었는데 이마저도 싫다면 PizzaMapper에서 직접적으로 호출해서 사용할 수 있다.

회사에서는 dto에서도 불필요한 메소드라 판단해서 다 지웠지만 이 브랜치에서는 그대로 놔둔다.

나의 경우에는 명시적인 것이 좋아서 dto 남겨두는 것을 선호하지만 동료들은 그마저도 싫은듯.

그런 의견은 존중한다.

# At a Glance

주니어분들께서 왜 이걸 이제 알려주냐고 나한테 오히려 꾸사리를 줬다.

~~미안해. 화내지마. 솔직히 나도 이거 써야겠다고 생각못했어.~~

하지만 이래나 저래나 완벽한 자동완성은 없다.

그럼에도 이런 라이브러리의 존재는 어디에 관점을 더 포커싱할 것인가에 대한 결과물이 아닐까?

지금까지 한동안은 MapStruct를 사용할 일이 없었는데 최근 어떻게든 서비스를 오픈해야 했던 회사의 사정상 코드 스멜이 여기저기 퍼져있던 것들을 돌아 볼 기회가 없었다.

최근에서야 이 부분을 봐야할 시점이 오면서 하나씩 처리해 나가고 있다.

이때 활용성이 높은 라이브러리의 존재가 너무 고맙게 느껴진다.    


