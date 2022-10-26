package effactive_java.second;

public class ItemThreeAndFour {
    // item 3
    // 싱글톤 : 인스턴스를 오직 하나만 생성할 수 있는 클래스

    /*
    클래스를 싱글턴으로 만들면 이를 사용하는 클라이언트를 테스트하기 어려워질 수 있다. 타입을 인스턴스로 정의하고 그 인스턴스를 구현해서 만든 싱글턴이 아니라면
    싱글턴 인스턴스를 가짜 (mock) 구현으로 대체할 수 없다. - spring에서 spring bean을 통해 bean에 등록되는 인스턴스에 대해 싱글톤으로 유지한다고 되어 있었는데,
    이에 따르면 테스트가 어렵고 의존성 주입이 구현을 의존해야 하는 상황이 벌어진다. 그렇기 때문에 인터페이스로 만든 후 구현체 (implement)를 통해 생성한 듯 하다.

    싱글톤을 만드는 방식이다.
    생성자를 private으로 감춰둔다. 인스턴스 접근 수단으로 public static 멤버를 하나 마련한다.
    * */

    public static final ItemThreeAndFour INSTANCE = new ItemThreeAndFour();
//    private ItemThreeAndFour() { }

    /*
    private 생성자는 public static final 필드인 ItemThreeAndFour.INSTANCE를 초기화 할 때만 한번 호출되고 그 이상의 호출은 불가능하다.
    단 권한이 있는 클라이언트가 리플렉션 API인 AccessibleObject.setAccessible을 사용해 private 생성자를 호출할 수 있으니 생성자에서 두 번째 객체가 생성되려 할 때
    예외를 던지면 된다.

    두 번째 방식이다. 정적 팩토리 메서드를 public static 멤버로 제공하는 것이다.
    * */

//    public static final ItemThreeAndFourAndFour INSTANCE = new ItemThreeAndFourAndFourAndFour();
//    private ItemThreeAndFourAndFour() { }
//    public static ItemThreeAndFourAndFour getInstance() {
//        return INSTANCE;
//    }

    /*
    ItemThreeAndFour.getInstance는 항상 같은 객체의 참조를 반환한다. 마찬가지로 리플렉션 예외 처리는 넣어야 한다.
    public 필드 방식의 장점은
    1. 해당 클래스가 싱글턴임을 API에서 드러난다는 것이다. public static 필드가 final이니 절대 다른 객체를 참조할 수 없다.
    2. 간결하다.
    정적 팩토리 방식의 장점은
    1. API를 바꾸지 않고 싱글턴이 아니게 변경할 수 있다. 유일한 인스턴스를 반환하던 메서드를 스레드별로 다른 인스턴스를 넘기게 할 수 있다.
    2. 정적 팩터리를 제네릭 싱글턴 팩토리로 만들 수 있다.
    3. 정적 팩토리의 메서드 참조를 공급자로 사용할 수 있다는 점이다.

    정적 팩토리 방식에서 해당 장점들을 굳이 사용하지 않을 것이면 public 필드 방식이 더 낫다는 것이다.

    위의 두가지 방식으로 만든 싱글턴 클래스를 직렬화하려면 Serializable을 구현한다고 선언하는 것으로 끝이 아니다. 모든 인스턴스 필드를 일시적으로 선언하고
    readResolve 메서드를 제공해야 한다. 이런 식으로 만들지 않으면 직렬화된 인스턴스를 역직렬화할 때마다 새로운 인스턴스가 만들어진다.
    * */

//    싱글턴임을 보장하는 readResolve 메서드
//    private Object readResolve() {
//        return INSTANCE;
//    }

    /*
    싱글턴을 만드는 마지막 방식이다. 원소가 하나인 열거 타입을 선언하는 것이다.
    직렬화가 쉽고 리플렉션 공격에서도 제 2의 인스턴스가 생기는 일을 막아준다. 원소가 하나뿐인 열거 타입이 싱글턴을 만드는 가장 좋은 방법이다.
    그러나 싱글턴이 Enum 외의 클래스를 상속해야 한다면 이 방법은 사용할 수 없다. 대신 열거 타입이 다른 인터페이스를 구현하도록 선언할 수 있다.
    * */
    public enum Item { INSTANCE; }

    // item 4
    /*
    정적 메서드와 정적 필드만을 담은 클래스를 만들게 되면 객체 지향적 사고와는 전혀 거리가 멀다. 그러나 java.lang.Math, java.util.Arrays 처럼 기본 타입 값이나
    배열 관련 메서드들을 모아놓을 수 있다. java.util.Collections처럼 특정 인터페이스를 구현하는 객체를 생성해주는 정적 메서드 - 혹은 팩토리 - 를 모아놓을 수도 있다.
    -> 인스턴스에 넣을 수 있게 추가됨
    final 클래스와 관련한 메서드들을 모아놓을 때도 사용한다. 정적 멤버, 정적 함수만 존재하면 인스턴스 없이 사용이 가능하기에 인스턴스를 생성할 필요가 없다.
    그러나 이러한 클래스임에도 불구하고 인스턴스를 생성하도록 허용한 경우가 있다. 또한 추상 클래스로 만드는 것으론 인스턴스화를 막을 수 없다.
    상속을 통해 인스턴스화를 하게 되면 더 좋지 않은 결과가 발생하기 때문이다.
    이를 방지하기 위해 private 생성자를 추가하면 클래스의 인스턴스화를 막을 수 있다. - 예시로 JDBC의 DriverManager가 있다. -
    * */

    // 기본 생성자를 생성하는 것을 막는다. prevent to make default instance
    private ItemThreeAndFour() {
        throw new AssertionError();
    }

    /*
    해당 에러를 발생함으로써 클래스 안에서의 생성자 호출을 방지한다. 또한 상속을 방지하는 효과도 있다.
    * */
}
