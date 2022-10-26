package effactive_java.second;

import java.io.*;
import java.lang.ref.Cleaner;

public class ItemEightAndNine implements AutoCloseable {
    public static final int BUFFER_SIZE = 10;

    // item 8
    /*
    자바에서 제공하는 2가지 객체 소멸자로 finalizer와 cleaner가 존재한다. finalizer는 예측할 수 없고 상황에 따라 위험한 문제를 일으킬 수 있다.
    오동작, 낮은 성능, 이식성 문제의 원인이 되기 때문에 이보다 cleaner를 사용하기를 권장하나, cleaner마저 예측할 수 없고, 느리고, 일반적으로 불필요하다.
    두 개념은 C++의 파괴자와는 전혀 다른 개념이다. 이 둘은 즉시 수행된다는 보장도 없기 때문에 제때 실행되는 작업에 적용할 수 없다. 중대한 작업을 맡길 경우
    절대 언제 작동할 지 알 수 없다. JLS, 12.6 에서 객체에 접근할 수 없게 된 이후 두 메서드가 실행되기까지 얼마나 걸릴지 알 수가 없다.

    이 메서드는 가비지 컬랙터 구현에 따라 실행 속도가 바뀌기 때문에 현업에서도 문제를 일으킬 가능성이 높다.
    finalizer의 경우 스레드가 다른 스레드의 우선순위보다 낮으면 실행하지 못한채 대기한다. 이로인해 많은 스레드가 종료되지 못하고 누적되면서
    프로그램에 버그를 불러왔다. cleaner는 그나마 자신을 수행할 스레드를 제어할 수 있으나 이마저도 백그라운드에서 수행되고 가비지 컬렉터의 통제를
    받으니 즉시 수행된다고 보장할 수 없다.

    상태를 영구적으로 수정하는 작업에서 절대 finalizer나 cleaner에 의존해서는 안된다. 예를 들면 DB에서 공유 자원의 영구 락 해체를 finalizer,
    cleaner에 맡겨 놓으면 분산 시스템 전체가 서서히 멈출 것이다. 제때 실행되지 못한 작업으로 인해 정상적인 동작을 할 수 없는 것이다.

    finalizer에 또다른 부작용이 있는데 이 메서드 실행 도중 발생한 예외는 무시되고 처리할 작업이 남아있더라도 그 순간 종료하게 된다.
    이런 예외로 인해 해당 객체가 마무리가 덜 된 상태로 남을 수도 있는 것이다. cleaner가 그나마 이런 문제는 발생하지 않는다.

    이 finalizer와 cleaner는 성능 문제도 발생한다. AutoCloseable를 통한 객체 생성, 가비지 컬렉터 수거까지 걸린 시간과 finalizer를 통한 수거는 50배 이상
    차이났다. finalizer를 사용한 해결 대신 AutoCloseable과 안정망으로 cleaner를 활용하면 약 5배만 느려진다.

    finalizer를 통한 클래스는 finalizer 공격에 취약하다. 생성자나 직렬화 과정에서 예외 발생 시 이 생성되다 만 객체에 악의적인 하위 클래스의
    finalizer가 실행될 수 있다. 이 finalizer는 정적 필드에 자신의 참조를 할당해 가비지 컬렉터가 수집하지 못하게 막을 수 있다.
    이제 이 객체의 메서드를 호출해 할 수 없었던 작업을 수행할 수 있다. 객체 생성을 막으려면 생성자에서 예외를 던지는 것만으로 충분했으나,
    finalizer가 있다면 그렇지 못하다. 이때 final 클래스들은 안전하나 final이 아닌 클래스인 경우 아무 일도 하지 않는 finalize 메서드를 만들고
    final로 선언하자.

    그렇다면 이 파일과 스레드의 등 종료해야 할 자원을 담은 객체의 클래스에서 이를 대신할 묘안은 무엇일까?
    AutoCloseable을 구현하고 클라이언트에서 인스턴스를 다 쓰고 나면 close 메서드를 호출한다. 일반적으로는 예외 발생에도 정상 종료 되도록
    try-with-resources를 사용해야 한다. 그리고 close 메서드는 이 객체는 더 이상 유용하지 않음을 필드에 기록하고, 다른 메서드가 이 필드를
    검사해 닫힌 이후 불렸다면 IllegalStateException을 던지는 것이다.

    이 cleaner와 finalizer의 활용은 2가지이다.
    1. close 메서드를 호출하지 않을 경우 안전망으로써 사용된다.
    2. 네이티브 피어와 연결된 객체에서다.

    네이티브 피어는 일반 자바 객체가 네이티브 메서드를 통해 기능을 위임한 네이티브 객체를 말한다. 네이티브 피어는 자바 객체가 아니므로 가비지 컬렉터가
    이를 회수하지 못한다. 그러나 네이티브 피어가 심각한 자원을 가지고 있지 않는 경우만 사용하기 적절하다. 즉시 회수하거나 성능 저하가 감당된다면
    앞서 말한 close 메서드를 활용하기 바란다.

    네이티브 메서드 : 예를 들자면 C, C++ 등 네이티브 언어에서 작성한 메서드이다.

    cleaner에 대한 사용
    * */

    private static final Cleaner cleaner = Cleaner.create();

    private static class State implements Runnable {
        int numJunksPiles;

        State(int numJunksPiles) {
            this.numJunksPiles = numJunksPiles;
        }

        @Override
        public void run() {
            System.out.println("방 청소");
        }
    }

    private final State state;

    private final Cleaner.Cleanable cleanable;

    public ItemEightAndNine(int numJunksPiles) {
        state = new State(numJunksPiles);
        cleanable = cleaner.register(this, state);
    }

    @Override
    public void close() {
        cleanable.clean();
    }

    /*
    static으로 선언된 중첩 클래스인 State은 cleaner가 방 청소를 수행할 때 수거할 자원을 담고 있다. 이 예시에서 보다 현실적이기 위해서는 네이티브 피어
    를 가리키는 포인터를 담은 final long 변수여야 한다. State는 Runnable을 구현하고, 그 안의 run 메서드는 cleanable에 의해 딱 한 번만 호출 된다.
    이 cleanable 객체는 Item 생성자에서 cleaner에 Room과 State를 등록할 때 얻는다.

    run 메서드가 호출되는 상황은 2가지이다. Room의 close 메서드를 호출할 때이다. close 메서드에서 cleanable의 clean을 호출하면 이 메서드 안에서
    run을 호출한다. 또는 가비지 컬렉터가 Room을 회수할 때까지 클라이언트가 close를 호출하지 않으면, cleaner가 State의 run 메서드를 호출해 줄 것이다.

    이때 State 인스턴스는 절대 Room 인스턴스를 참조해서는 안 된다. Room 인스턴스를 참조할 경우 순환참조가 생겨 가비지 컬렉터가 Room 인스턴스를
    회수해갈 기회가 오지 않는다. 자동 청소를 할 수 없게 된다. 그렇기 때문에 State는 정적 중첩 클래스이다.

    정적이 아닌 중첩 클래스는 자동으로 바깥 객체의 참조를 갖게 되기 때문이다. 람다 또한 바깥 객체의 참조를 갖기 쉬우니 사용하지 않는게 좋다.
    * */

    public static void main(String[] args) {
        try (ItemEightAndNine myItemEightAndNine = new ItemEightAndNine(7)) {
            System.out.println("안녕");
        }
        // 정상적으로 방청소를 출력한다.

        new ItemEightAndNine(99);
        System.out.println("어쩔티비");
        System.gc(); // 이 스크립트를 통해 종료할 수 있다.
        // 방청소를 출력하지 않는다.
    }

    /*
    위의 try에 싸인 명령은 정상적으로 종료 시 방청소가 출력 된다. 그러나 아래의 방식은 방청소를 출력하지 않는다.
    아래의 방식은 예측할 수 없는 상황이다.

    cleaner의 명세에 따르면 다음과 같다.
    System.exit을 호출할 때의 cleaner 동작은 구현하기 나름이다. 청소가 이뤄질지는 보장하지 않는다.

    이는 일반적인 프로그램 종료도 마찬가지이다. System.gc()를 main에 추가한다면 보장되겠지만 항상 그렇다고 할 순 없다.
    * */

    // item 9

    /*
    전통적인 자원의 닫힘을 보장하는 수단은 try-finally이다. 예외가 발생하거나 메서드가 반환되는 경우를 포함해서 말이다.
    * */

//    static String first(String path) throws IOException {
//        BufferedReader br = new BufferedReader(new FileReader(path));
//        try {
//            return br.readLine();
//        } finally {
//            br.close();
//        }
//    }
//
//    static void copy(String src, String dst) throws IOException {
//        InputStream in = new FileInputStream(src);
//        try {
//            OutputStream out = new FileOutputStream(dst);
//            try {
//                byte[] buf = new byte[BUFFER_SIZE];
//                int n;
//                while ((n = in.read(buf)) >= 0)
//                    out.write(buf, 0, n);
//            } finally {
//                out.close();
//            }
//        } finally {
//            in.close();
//        }
//    }

    /*
    이 try-finally 블록은 예외가 모두 발생할 가능성이 있다.
    first 함수를 예로 들때 기기에 물리적인 오류, 혹은 인터럽트가 발생해 문제가 생긴다면 readLine 메서드가 예외를 던지고, close 메서드도 실패할 것이다.
    이때 두번째 예외가 첫번째 예외를 완전히 집어삼켜 버린다. 그러면 스택 추적 내역에 첫번째 예외에 관한 정보가 남지 않고 두번째 예외에 대해서만 남는다.
    이는 디버깅을 매우 어렵게 한다. 이런 문제를 해결하기 위해 try-with-resources를 사용한다.
    * */

    static String first(String path) throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            return br.readLine();
        }
    }

    static void copy(String src, String dst) throws IOException {
        try (InputStream in = new FileInputStream(src);
             OutputStream out = new FileOutputStream(dst)) {
            byte[] buf = new byte[BUFFER_SIZE];
            int n;
            while ((n = in.read(buf)) >= 0)
                out.write(buf, 0, n);
        }
    }

    /*
    훨씬 짧고 직관적이다. first 메서드에서 readLine에서 발생한 예외만이 기록된다. close에서 발생한 예외는 더이상 첫번째 예외를 덮지 않는다.
    이 숨겨진 예외는 스택 추적 내역에 suppressed라는 표를 달고 출력된다. 자바의 Throwable에 추가된 getSuppressed 메서드를 이용하면 숨겨진 예외도
    가져올 수 있다.

    이때 catch를 사용할 수도 있다.
    * */

    static String firstCatch(String path) {
        String defaultValue = "";
        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            return br.readLine();
        } catch (IOException e) {
            return defaultValue;
        }
    }
}
