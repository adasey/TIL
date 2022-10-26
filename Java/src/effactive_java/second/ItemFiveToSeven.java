package effactive_java.second;

import java.util.EmptyStackException;
import java.util.regex.Pattern;

public class ItemFiveToSeven {
    // item 5
    /*
    의존 객체 주입을 사용하자.

    정적 유틸리티나 싱글턴 패턴을 잘못 적용한 경우 유연하지 못하고 테스트 하기도 어렵다.
    사용하는 자원에 따라 동작이 달라지는 클래스에는 정적 유틸리티 클래스나 싱글턴 방식 대신 클래스가 여러 자원 인스턴스를 지원할 수 있는 패턴으로 사용해야 한다.

    이런 상황에서는 인스턴스를 생성할 때 생성자에 필요한 자원을 넘겨주는 방식을 사용하면 된다. 의존 객체 주입의 한 형태이다.
    * */

    private final Lexicon dictionary;

    public ItemFiveToSeven(Lexicon dictionary) {
        this.dictionary = dictionary;
    }

    // 이는 스프링에서도 사용하는 방식으로 DI (Dependency Injection)로 불리며 @RequiredArgsConstructor나
    // @ComponentScan에서 @Component를 통해 Bean에 등록한 객체를 찾아내 생성자를 만들어 주입합니다.

    /*
    불변을 보장한다. 그로 인해 같은 자원을 사용하려는 여러 클라이언트가 의존 객체들을 안심하고 공유할 수 있다. 의존 객체 주입은 생성자, 정적 팩토리, 빌더 모두에 똑같이 응용할 수 있다.

    이 패턴의 변형으로 생성자에 자원 팩토리를 넘겨주는 방식이 있다. 팩토리란 호출할 때마다 특정 타입의 인스턴스를 반복해서 만들어주는 객체를 말한다.
    즉 팩토리 메서드 패턴 (Factory Method Pattern)을 구현한 것이다. Supplier<T> 인터페이스가 팩토리를 표현한 완벽한 예이다.
    * */

    // Mosaic create(Supplier<? extends Tile> tileFactory) {...}

    /*
    의존 객체 주입은 유연성과 테스트 용이성을 개선해준다. 하지만 의존성이 너무 많은 프로젝트에서는 코드를 어지럽게 만들기 마련이다.
    이때 프레임워크의 도움을 통해 의존 객체 주입을 간결하게 사용할 수 있다. Spring, Dagger, Guice 등이 있다.
    * */

    // item 6
    /*
    불필요한 객체 생성은 피하는게 좋다. 객체 하나를 재사용하는 편이 나을 때가 많다. 물론 항상 그렇지만은 않으나 대체로 좋다.
    불변 객체의 경우 언제든 재사용할 수 있다.

    피해야 하는 경우이다.
    String s = new String("text")
    이 경우 문장이 실행 될 때마다 매번 새로운 인스턴스를 생성한다.

    String s = "text"
    그러나 이 방식은 새로운 인스턴스를 매번 만들지 않고 하나의 string 인스턴스를 사용한다. 나아가 이 방식을 사용할 경우 같은 가상 머신 내의 똑같은
    문자열 리터럴을 사용하는 모든 코드가 같은 객체를 재사용함이 보장된다.
    * */

    /*
    생성자 대신 정적 팩토리 메서드를 제공하는 불변 클래스에서 정적 팩토리 메서드를 사용해 불필요한 객체 생성을 피할 수 있다.

    Boolean(String) 대신 Boolean.valueOf(String)

    생성 비용이 아주 비싼 객체도 종종 있는데 이때 반복해서 비싼 객체를 필요로 한다면 캐싱해서 사용하는걸 권장한다.
    * */

//    static boolean isRomanNumeral(String s) {
//        return s.matches("^(?=.)M*(C[MD]|D?C{0,3})" + "(X[CL]|L?X{0,3})(I[XV]|V?I{0,3})$");
//    }

    /*
    위처럼 주어진 문자열이 유효한 로마 숫자인지 확인하는 메서드가 있다. 이 방식의 문제는 String.matches를 사용하는데 있다. 이 함수가 정규표현식으로
    문자열 형태를 확인하는 가장 쉬운 방법이나 성능이 중요한 상황에 반복해 사용하기가 적합하지 않다. 이 메서드는 내부에서 만드는 정규표현식용 Pattern
    인스턴스는 한 번 쓰고 버려져서 곧바로 가비지 컬렉션 대상이 된다. Pattern은 입력받은 정규표현식에 해당하는 유한 상태 머신 (finite state machine)
    을 만들기 때문에 인스턴스 생성 비용이 높다. 성능을 개선하기 위해 정규표현식을 표현하는 불변 Pattern 인스턴스를 클래스 초기화 과정에서 직접 생성해
    캐싱해두고, 나중에 isRomanNumeral 메서드가 호출될 때마다 이 인스턴스를 재사용한다.
    * */

    private static final Pattern ROMAN = Pattern.compile("^(?=.)M*(C[MD]|D?C{0,3})" + "(X[CL]|L?X{0,3})(I[XV]|V?I{0,3})$");

    static boolean isRomanNumeral(String s) {
        return ROMAN.matcher(s).matches();
    }

    /*
    - 해당 함수의 동작과 내부 동작을 이해한다면 어느정도 쉽게 다가올 수도 있는 내용이나 String의 Match 함수 내부 동작에 대해 알고 있어야 하므로
    난이도가 있는 편이다. -

    이제부터는 isRomanNumeral이 자주 호출되더라도 성능을 올릴 수 있다. - 성능 도구를 사용해 측정해봐야 할 것 같다.

    만일 클래스가 초기화된 후 메서드를 호출하지 않는다면 ROMAN 필드는 쓸데없이 초기화된 꼴이다. isRomanNumeral메서드 호출 시 필드를 초기화하는
    지연 초기화를 통해 불필요한 초기화를 없앨 수 있으나 지연 초기화에서 코드가 복잡해진다. 게다가 성능도 크게 증가하지 않는 경우가 많다.

    어댑터 (view)는 실제 작업은 뒷단 객체에 위임하고 자신은 인터페이스 역할을 해주는 객체이다. 어댑터는 뒷단 객체만 관리하면 되므로 하나씩만 만들어도 충분하다.
    Map 인터페이스의 KeySet 메서드는 Map 객체 안의 키를 전부 담은 Set뷰를 반환한다. 이는 KeySet을 호출할 때마다 새로운 Set 인스턴스가 생긴다고
    생각할 수도 있지만 매번 같은 Set인스턴스를 반환할 지도 모른다. 반환된 Set 인스턴스가 일반적으로 가변이더라도 반환된 인스턴스들은 기능적으로
    모두 똑같다. 즉 반환한 객체 중 하나를 수정하면 다른 모든 객체가 따라 바뀐다. 모두 똑같은 Map 인스턴스를 대변하기 때문이다.

    불필요한 객체를 만드는 예론는 오토박싱 (auto boxing)을 들 수 있다. 오토박싱은 프로그래머가 기본 타입과 박싱된 기본 타입을 섞어 쓸 때 자동으로
    상호 변환해주는 기술이다. 오토박싱은 기본 타입과 그에 대응하는 박싱된 기본 타입의 구분을 흐려주지만, 완전히 없애주는 것은 아니다.
    의미상으로는 크게 다를 것이 없으나 성능은 그렇지 않다.

    예시이다.
    * */

//    private static long sum() {
//        Long sum = 0L;
//        for (long i = 0; i <= Integer.MAX_VALUE; i++) {
//            sum += i;
//        }
//
//        return sum;
//    }

    /*
    sum 변수를 long이 아닌 Long으로 선언해 불필요한 인스턴스가 약 2^31개가 만들어진다. (대략 long 타입인 i가 Long 타입인 sum에 더해질 때마다)
    sum이 Long에서 long으로 변경되면 60배는 빨라지게 된다.
    박싱된 기본 타입보다는 기본 타입을 사용하고, 오토박싱은 유의하기 바란다.

    객체 생성은 비싸니 피해야 한다는 의미는 아니다. 요즘엔 JVM에서 별다른 일을 하지 않는 작은 객체를 생성하고 최수하는 일은 크게 부담되지 않는다.
    그러나 가벼운 객체 생성을 피하고자 객체 풀을 만든다면 DB연결과는 다르게 메모리 사용량을 늘리고 성능을 떨어뜨린다. 요즘 JVM은 최적화가 잘 되어있는 편이다.
    * */

    // item 7

    /*
    가비지 컬렉션을 갖춘 자바는 다 쓴 객체를 알아서 회수 해가는 기능이 있다. 프로그래머는 이에 대해 신경을 덜 써도 될뿐 아예 신경을 쓰지 않으면 안된다.

    stack을 구현한 코드에서 예를 찾아본다. 이 코드에서 문제는 전혀 없이 정상 작동하지만 메모리 누수라는 꼭꼭 숨어있는 문제가 있다. 설명하자면,
    스택은 커졌다가 줄었다가 하면서 스택에서 꺼내진 객체들을 가비지 컬렉터가 회수 하지 않는다. 프로그램에서 객체를 더 이상 사용하지 않더라도 말이다.
    이 스택이 그 객체들의 다 쓴 참조 (obsolete reference)를 여전히 가지고 있기 때문이다. 다 쓴 참조란 문자 그대로 앞으로 다시 쓸 일이 없는 경우로
    elements 배열의 활성 영역 밖의 참조들이 모두 여기에 포함된다. 활성 영역은 인덱스가 size보다 작은 원소들로 구성된다.

    이제 해결법은 null 처리를 통해 참조 해제를 진행하면 된다.
    * */
    private int size = 1;
    private Object[] elements;

//    public Object pop() {
//        if (size == 0) {
//            throw new EmptyStackException();
//        }
//        return elements[--size];
//    }

// 수정 전

    public Object pop() {
        if (size == 0) {
            throw new EmptyStackException();
        }
        Object result = elements[--size];
        elements[size] = null;
        return result;
    }

// 수정 후

    /*
    솔직히 다른 알고리즘에서 stack 구조에서 pop 시 단순히 리스트에서 전 단계의 요소를 참조하고 끝내서 null 처리에 대한 의문이 있었는데 여기서 확실히 알아간다.

    그러나 객체 참조를 null 처리하는 일은 예외적인 경우만 사용하고 그 외에는 유효 범위 밖으로 밀어내는 것이다. 메모리 누수에 대해 신경쓰기 바란다.

    캐시 역시 메모리 누수를 일으키는 주범이다. 해결은 운 좋게 캐시 외부에서 키를 참조하는 동안만 엔트리가 살아 있는 캐시가 필요한 상황이라면 WeekHashMap을 사용해 캐시를 만든다.
    그러나 캐시를 만들 때 캐시 엔트리 유효 기간을 정확이 정의하기 어렵다.

    이런 방식에서는 쓰지 않는 엔트리 방식을 사용한다. 백그라운드 스레드를 활용하거나 캐시에 새 엔트리를 추가할 때 부수 작업으로 수행하는 방법이다.
    더 복잡한 캐시에 새 엔트리를 추가하는 방식으로 java.lang.ref

    메모리 누수의 3번째는 listener, callback 등이 있다. 클라이언트가 콜백을 등록하고 명확히 해지 하지 않는다면 콜백은 계속 쌓여간다.
    * */

    public static void main(String[] args) {
        String si = new String("text");
        String s1 = "text";
    }
}
