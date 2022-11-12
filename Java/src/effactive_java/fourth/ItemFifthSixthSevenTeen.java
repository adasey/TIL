package effactive_java.fourth;

import java.util.List;

public final class ItemFifthSixthSevenTeen {
    // item 15

    public static final String[] Values = {"a", "b"};
    
    /*
    위와 같은 배열은 변경이 가능하다

    이때의 해결책이다.
    * */
    private static final String[] PRIVATE_VALUE1 = {"a", "b"};

    // 1
    public static final List<String> VALUES = List.of(PRIVATE_VALUE1); // 아래와 같음 == Collections.unmodifiableList(Arrays.asList(PRIVATE_VALUE1));

    // 2
    public static final String[] values() {
        return PRIVATE_VALUE1.clone();
    }
    
    /*
    1번 방식은 public 배열에 대해 private으로 바꾸고 public 불변 리스트를 추가하는 것이다.
    2번 방식은 마찬가지로 배열에 대해 private으로 바꾸고 그 복사본을 반환하는 public 메서드를 추가하는 방법이다. (방어적 복사)

    클라이언트의 원하는 기능에 맞춰 선택하면 될 듯 하다.
    * */

    // item 16
    // public 클래스에서 public 필드가 아닌 접근자 메서드를 활용하자.
    /*
    public 필드 대신 private 필드를 활용해 패키지 외부에서 접근 가능한 접근자(getter, setter)를 제공해 활용하면 된다.
    public 클래스의 필드가 만일 불변이라면 직접 노출되더라도 나쁘지는 않다. 그렇다고 좋은 것은 아니다. 이는 불변식을 보장할 수 있게 된다.
    * */

    // item 17
    // 변경 가능성을 최소화하라
    /*
    불변 클래스는 인스턴스 내부 값을 수정할 수 없는 클래스이다.

    불변 클래스를 만드는 규칙 5가지이다.
        1. 객체의 상태를 변경하는 메서드를 제공하지 않는다.
        2. 클래스를 확장할 수 없도록 한다. - 상속을 막음
        3. 모든 필드를 final로 선언한다.
        4. 모든 필드를 private로 선언한다.
        5. 자신 외에는 내부의 가변 컴포넌트에 접근할 수 없도록 한다.

    이 불변 객체는 변경이 불가능하므로 스레드에도 안정적이다. 불변 객체는 자유롭게 공유가 될 수 있다. 또한 불변 객체끼리는 내부 데이터를 공유할 수도 있다.
    실패 원자성을 제공한다. 이는 실패 상태에 도달해도 이전 데이터를 유지한다.

    단점은 값이 다를 경우 독립적인 새로운 객체를 생성해야 한다. 또한 값이 많아 비용이 큰 경우 매번 큰 비용을 사용하게 된다.
    * */

    // 불변 클래스 구현
    private final double x;
    private final double y;

    public ItemFifthSixthSevenTeen(double x, double y) {
        this.x = x;

        this.y = y;
    }

    public double positionX() {
        return x;
    }

    public double positionY() {
        return y;
    }

    public ItemFifthSixthSevenTeen plus(ItemFifthSixthSevenTeen i) {
        return new ItemFifthSixthSevenTeen(x + i.x, y + i.y);
    }

    public ItemFifthSixthSevenTeen minus(ItemFifthSixthSevenTeen i) {
        return new ItemFifthSixthSevenTeen(x - i.x, y - i.y);
    }

    public static final ItemFifthSixthSevenTeen zeroPoint = new ItemFifthSixthSevenTeen(0, 0);

    /*
    다음과 같은 상수를 제공할 수도 있다.

    다음과 같은 불변 클래스는 장점이 많다. 클래스는 꼭 필요한 경우가 아니라면 불변이 좋다. 그러므로 getter가 존재해도 setter가 있을 필요가 없다.
    그러나 반드시 불변 클래스로 만들 수는 없다. 그렇기 때문에 변경 가능한 부분을 최소화 하도록 하자. class의 필드는 private final일 수록 좋다.
    * */
}
