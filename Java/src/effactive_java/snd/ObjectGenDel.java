package effactive_java.snd;

import java.sql.*;

public class ObjectGenDel {
    // item 1 : 클래스는 생성자와 별도로 정적 팩토리 매세드를 제공할 수 있음. (static factory method)

    // 클래스가 정적 메소드와 생성자를 동시에 제공할 때 정적 팩토리 메소드가 제공하는 장점 5가지가 있다.
    /*
    1. 이름을 가질 수 있다. 이름을 통해 반환될 객체의 특성을 쉽게 묘사할 수 있다.
        또한 비슷한 인자 개수만 다른 클래스의 생성자를 여러개 만드는 것 보단 이름이 정해진 정적 팩토리 메서드를
        여러개 만드는 편이 API를 사용할 사용자가 혼동하지 않을 수 잇다.
    2. 호출될 때마다 인스턴스를 새로 생성하지 않아도 된다.
        불변 클래스는 인스턴스를 미리 만들어놓거나 새로 생성한 인스턴스를 캐싱해 재활용하는 식으로 불필요한 객체 생성을 피할 수 있다.
        같은 객체가 자주 요청되는 상황인 경우 성능을 상당히 끌어올려 준다. 이는 FlyWeight pattern와도 비슷한 기법이다.

        반복되는 요청에 같은 객체를 반환하는 식으로 정적 팩토리 방식의 클래스는 언제 어느 인스턴스를 살아있게 통제할 수 있다.
        이런 클래스를 인스턴스 통제(instance-controlled class) 클래스라 한다.

        인스턴스가 통제되어야 하는 이유? : 클래스는 싱글턴(singleton), 인스턴스화 불가(noninstantiable)로 만들 수 있다.
        또한 불변 값 클래스에서 동치인 인스턴스가 단 하나뿐임을 보장할 수 있다. a == b 일때 a.equals(b) 가 성립.
        즉 클래스의 참조도 같다.

        이 인스턴스의 통제는 flyweight 패턴의 근간이 된다. 열거 타입은 인스턴스가 하나만 만들어짐을 보장한다.
    3. 반환 타입의 하위 타입 객체를 반환할 수 있는 능력이 있다. 반환할 객체의 클래스를 자유롭게 선택할 수 있게 하는 유연성이다.
       API를 만들때 구현 클래스를 공개하지 않고도 그 객체를 반환할 수 있기 때문에 API를 작게 유지할 수 있다.
    4. 입력 매개변수에 따라 매번 다른 클래스의 객체를 반환할 수 있다.
       특정 클래스에서 기존에는 반환하던 여러 하위 클래스에서 성능을 개선한 또다른 하위 클래스를 반환하거나 반환하는 하위 클래스의 개수를
       늘려도 괜찮다. 해당 클래스의 하위 클래스를 반환하기만 하면 된다.
    5. 정적 팩토리 메서드를 작성하는 시점에는 반환할 객체의 클래스가 존재하지 않아도 된다. 이 유연함은 서비스 제공자 프레임워크를 만드는 근간이 된다.

       대표 : JDBC

       여기서 제공자(provider)는 서비스의 구현체이다. 이 구현체들을 클라이언트에 제공하는 역할을 프레임워크가 통제하여, 클라이언트를 구현체로부터 분리한다.
       서비스 제공자 프레임워크는 3개의 핵심 컴포넌트로 이뤄진다.
       1. 서비스 인터페이스 : 구현체의 동작을 정의
       2. 제공자 등록 API : 제공자가 구현체를 등록 할 때 사용
       3. 서비스 접근 API : 클라이언트가 서비스의 인스턴스를 얻을 때 사용

       종종 서비스 제공자 인터페이스(service provider interface)라고 하는 4번째 컴포넌트가 쓰이기도 한다. 서비스 제공자 인터페이스가 없다면,
       각 구현체를 인스턴스로 만들 때 리플렉션을 사용해야 한다. - 혹시 이게 Map을 인스턴스로 선언할 때 인지..? -

       예시 ) JDBC에서 Connection이 서비스 인터페이스 역할을, DriverManager.registerDriver가 제공자 등록 API 역할을,
       DriverManager.getConnection이 서비스 접근 API역할을, Driver가 서비스 제공자 인터페이스 역할을 수행한다.

       사용자 제공자 프레임워크 패턴에는 여러 변형이 있다. 서비스 접근 API는 공급자가 제공하는 것보다 더 풍부한 서비스 인터페이스를 클라이언트에 반환할 수 있다.
       브리지 패턴(Bridge Pattern)이 그 경우이다. 의존 객체 주입 (dependency injection, 의존성 주입) 프레임워크도 강력한 서비스 제공자라고 할 수 있다.
    * */

    public static void main(String[] args) throws ClassNotFoundException, SQLException {
        String driver = "com.mariadb.jdbc.Driver"; // mariadb, mysql 등 JDBC를 통해 connection을 지원하는 DB의 Driver에 접근해 실행하기 위한 경로

        Class.forName(driver); // new DriverManage instance - DriverManager를 실행하도록 해당 JDBC의 Driver static을 실행한다.

        DriverManager.getConnection("url", "user", "pw"); // getConnection을 통해 사용자는 서비스 접근 API를 활용한다.
    }

    // JDBC 내부 동작
    // public final class Driver implements java.sql.Driver { - Driver 인터페이스의 구현체 사용자는 구현체인 Driver을 알 필요없이 경로를 제공하면 Class.forName을 통해 클래스를 호출한다.
    /*
    static {
        try {
            DriverManager.registerDriver(new Driver()); // 제공자 등록 API : DriverManager의 인스턴스를 생성
        } catch (SQLException e) {
            // eat
        }
    }
    * */

    /*
    지금부터는 단점이다.

    1. 상속을 하려면 public이나 protected 생성자가 필요하니 정적 팩터리 메서드만 제공하면 하위 클래스를 만들 수 없다.
       이는 컬렉션 프레임워크의 유틸리티 구현 클래스들은 상속할 수 없다. 상속보다 컴포지션을 사용하도록 유도하고 불변 타입으로 만들려면 이 제약을 지켜야 하기 때문에
       반드시 단점은 아니다.

    2. 정적 팩토리 메서드는 프로그래머가 찾기 어렵다. 생성자처럼 API 설명에 명확히 드러나지 않는다.
       사용자가 정적 팩터리 메서드 방식 클래스를 인스턴스화 할 방법을 알아내야 한다. 아직은 자바독(java 파일에 작성하는 설명 주석)에서 스스로 처리한다면 좋겠지만
       API 문서를 잘 써놓고 메서드 이름도 널리 알려진 규약을 따라 짓는 식으로 문제를 완화해야 한다.
    * */

    // 정적 메서드 명명 방식
    /*
    from : 매개변수 하나를 받아 해당 타입의 인스턴스 반환 형변환 메서드
    of : 여러 매개변수를 받아 적합한 타입의 인스턴스 반환 집계 메서드
    valueOf : from, of의 상세 버전
    instance or getInstance : 매개변수를 받을 시 매개변수로 명시한 인스턴스를 반환하지만, 같은 인스턴스임을 보장하지 않는다.
    create or newInstance : 위와 같으나 매번 새로운 인스턴스 생성을 반환함 보장
    getType : getInstance와 같으나 생성할 클래스가 아닌 다른 클래스에 팩터리 메서드를 정의할 때. Type은 팩터리 메서드가 반환할 객체의 타입
    newType : newInstance와 같으나 생성할 클래스가 아닌 다른 클래스에 팩터리 메서드를 정의할 때. Type은 팩터리 메서드가 반환할 객체의 타입
    type : getType과 newType 대신 사용하면 됨
    * */
}

// item 2 : 생성자의 매개변수가 너무 많을 경우
/*
정적 팩터리와 생성자에서 공통적으로 나타나는 제약을 해결하기 위해 사용되는 빌더 패턴이다.

먼저 정적 팩터리이다.
* */

class  {
}