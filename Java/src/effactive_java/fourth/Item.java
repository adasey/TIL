package effactive_java.fourth;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Item {
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
}
