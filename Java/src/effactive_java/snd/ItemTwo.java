package effactive_java.snd;

import static effactive_java.snd.Hawaiian.Pineapple.LITTLE;
import static effactive_java.snd.Pizza.Topping.*;

public class ItemTwo {
// item 2 : 생성자의 매개변수가 너무 많을 경우

/*
정적 팩터리와 생성자에서 공통적으로 나타나는 제약을 해결하기 위해 사용되는 빌더 패턴이다.

먼저 점층적 생성자 패턴이다.
* */

//public class Computer {
//    private final String cpu;
//    private final String ram;
//    private final String mainBoard;
//    private final Strign power;
//    private final String hardDrive;
//    private final String ssd;
//    private final String monitor;
//    private final String gpu;
//
//    public Computer(String cpu, String ram, String mainBoard, String power) {
//        this(cpu, ram, mainBoard, power);
//    }
//
//    public Computer(String cpu, String ram, String mainBoard, String power, String gpu) {
//        this(cpu, ram, mainBoard, power, gpu);
//    }
//
//    // ...
//
//    public Computer(String cpu, String ram, String mainBoard, String power, String gpu, String hardDrive, String ssd, String monitor) {
//        this.cpu = cpu;
//        this.ram = ram;
//        this.mainBoard = mainBoard;
//        this.power = power;
//        this.gpu = gpu;
//        this.hardDrive = hardDrive;
//        this.ssd = ssd;
//        this.monitor = monitor;
//    }
//}

// instance : Computer myCom = new Computer(i7, samsung 8gb, asus-b550, P-1000, nvidia rtx3070, toshiba, skt-hinyx, lg)

/*
보다시피 점층적 생성자 패턴은 클래스의 매개변수가 매우 늘어나는걸 볼 수 있다. 점점 클래스가 거대해지면 생성자 중첩도 점점 비대해진다.
이는 클라이언트 코드의 작성이 혼란을 줄 수 밖에 없다. 생성자에 입력되는 순서를 전부 알 순 없기 때문이다.

이번에는 자바 빈즈 패턴으로 해결한 방식이다.
* */

//public class Computer {
//    private String cpu = "i5-5500";
//    private String ram = "samsung 8GB";
//    private String mainBoard = "asus b550";
//    private String power = "p-1000";
//    private String hardDrive = null;
//    private String ssd = null;
//    private String monitor = null;
//    private String gpu = null;
//
//    public Computer() { }
//
//    public void setCpu(String val) {
//        cpu = val;
//    }
//
//    public void setRam(String val) {
//        ram = val;
//    }
//
//    public void setMainBoard(String val) {
//        mainBoard = val;
//    }
//
//    public void setPower(String val) {
//        power = val;
//    }
//
//    public void setHardDrive(String val) {
//        hardDrive = val;
//    }
//
//    public void setSsd(String val) {
//        ssd = val;
//    }
//
//    public void setMonitor(String val) {
//        monitor = val;
//    }
//
//    public void setGpu(String val) {
//        gpu = val;
//    }
//}

// Computer myCom = new Computer();
// myCom.setCpu("i9-9900")
// ...
// myCom.setGpu("nvidia RTX-3090Ti")

/*
이 패턴은 인스턴스를 생성하기 비교적 쉽다. 그러나 객체 하나를 만들기 위해 여러 메소드를 호출해야 하고 이는 객체가 완전히 생성되기 전까지 일관성이 무너진 상태에 놓인다.
점층적 생성자 패턴은 생성자를 통해 매개변수들의 유효로 일관성 여부를 확인할 수 있었다. 그러나 메서드를 일일히 호출해야 하는 작업에서는 원하는 과정대로 진행됐는지 매번 확인해야 한다.

그렇기 때문에 자바 빈즈 패턴에서는 클래스를 불변으로 만들 수 없으며 이를 해결하기 위해 생성이 끝난 객체를 수동으로 얼린(freezing) 후 다시 사용하도록 만들어야 한다.
이 방식은 상당히 다루기 어렵고 객체 사용 전 프로그래머가 freeze 메서드를 확실히 호출했는지 확인해야한다.

이런 단점들을 해결하는 3번째 방식이 있다.

바로 빌더 패턴이다.
클라이언트가 필요한 객체를 직접 만드는 대신, 필수 매개변수만으로 생성자(혹은 정적 팩터리)를 호출해 빌더 객체를 얻는다. 그런 다음 빌더 객체가 제공하는 일종의 세터 메서드들로 원하는
선택 매개변수들을 설정한다. 마지막으로 매개변수가 없는 build 메서드를 호출해 보통은 불변인 객체를 얻는다. 이 빌더는 생성할 클래스 안에 정적 멤버 클래스로 만들어두는 게 보통이다.
* */

//public class Computer {
//    private final String cpu;
//    private final String ram;
//    private final String mainBoard;
//    private final String power;
//    private final String hardDrive;
//    private final String ssd;
//    private final String monitor;
//    private final String gpu;
//
//    public static class Builder {
//        private final String cpu;
//        private final String ram;
//        private final String mainBoard;
//        private final String power;
//
//        private String hardDrive = "toshiba 128GB";
//        private String ssd = "samsung 128GB";
//        private String monitor = "LG g9703";
//        private String gpu = "nvidia RTX3080";
//
//        public Builder(String cpu, String ram, String mainBoard, String power) {
//            this.cpu = cpu;
//            this.ram = ram;
//            this.mainBoard = mainBoard;
//            this.power = power;
//        }
//
//        public Builder setHardDrive(String val) {
//            hardDrive = val;
//            return this;
//        }
//
//        public Builder setSsd(String val) {
//            ssd = val;
//            return this;
//        }
//
//        public Builder setMonitor(String val) {
//            monitor = val;
//            return this;
//        }
//
//        public Builder setGpu(String val) {
//            gpu = val;
//            return this;
//        }
//
//        public Computer Build() {
//            return new Computer(this);
//        }
//    }
//
//    private Computer(Builder builder) {
//        cpu = builder.cpu;
//        ram = builder.ram;
//        mainBoard = builder.mainBoard;;
//        power = builder.power;
//        hardDrive = builder.hardDrive;
//        ssd = builder.ssd;
//        monitor = builder.monitor;
//        gpu = builder.gpu;
//    }
//}

// Computer myCom = new Computer.Builder("i9-10900K", "samsung 16GB x 4", "asus darkhero", "D-10000P").setHardDrive("Toshiba 1TB").setSsd("sk hynix 500GB").setMonitor("samsung 32inch odyssey").setGpu("nvidia RTX4090").build();

/*
해당 클래스는 불변이며 모든 매개변수의 기본값들을 한곳에 모아놓았다. 빌더의 세터 메서드들은 빌더 자신을 반환하기 때문에 연쇄적인 호출이 가능하다.
이는 플루언트 API(fluent API), 메서드 연쇄(method chaining)이라고 한다.

이 빌더 패턴은 파이썬과 스칼라에 있는 명명된 선택적 매개변수(named optional parameters)를 흉내낸 것이다. 해당 builder에서 유효성 검사와 잘못된 매개변수 입력 시
illegalArgumentException을 던지는 코드도 추가해야 한다.
 - 검색해도 이와 유사한 내용의 python 관련 내용은 나오지 않았으나 그나마 유사한 부분은 python의 데코레이터를 활용하는 부분에서 함수에서 함수로 넘길 때 외부 함수는 내부 함수의
 인수에 대해 매번 다르게 조절 할 수 없다. 그렇기 때문에 *args, **Kargs를 통해 인수가 몇가지 작성 되어 있던 간에 전부 받아 들일 수 있다.
 이 부분을 흉내냈다고 하는 것 같다.

빌더 패턴은 계층적으로 설계된 클래스와 함께 쓰기도 좋다. 각 계층의 클래스에 관련 빌더를 멤버로 정의한다. 추상 클래스는 추상 빌더를, 구체 클래스는 구체 빌더를 갖게 한다.
* */

//public abstract Class Pizza {
//    public enum Topping {
//        HAM, MUSHROOM, ONION, PEPPER, SAUSAGE, OLIVE
//    }
//    final Set<Topping> toppingset;
//
//    abstract static class Builder<T extends Builder<T>> {
//        EnumSet<Topping> toppings = EnumSet.noneOf(Topping.class);
//        public T addTopping(Topping topping) {
//            toppings.add(object.requireNonNull(topping));
//            return self();
//        }
//
//        abstract Pizza build();
//
//        protected abstract T self();
//    }
//
//    Pizza(Builder<?> builder) {
//        toppings = builder.toppings.clone();
//    }
//}

//public class HawaiianPizza extends Pizza {
//    public enum Pineapple { NONE, LITTLE, MEDIUM, MANY}
//    private final Pineapple pineapple;
//
//    public static class Builder extends Pizza.Builder<Builder> {
//        private final Pineapple pineapple;
//
//        public Builder(Pineapple pineapple) {
//            this.pineapple = pineapple;
//        }
//
//        @Override
//        public HawaiianPizza build() {
//            return new HawaiianPizza(this);
//        }
//
//        @Override
//        protected Builder self() {
//            return this;
//        }
//    }
//
//    private HawaiianPizza(Builder builder) {
//        super(builder);
//        pineapple = builder.pineapple;
//    }
//}
//
//public class Calzone extends Pizza {
//    private final boolean sauceInside;
//
//    public static class Builder extends Pizza.Builder<Builder> {
//        private boolean sauceInside = false;
//
//        public Builder sauceInside() {
//            sauceInside = true;
//            return this;
//        }
//
//        @Override
//        public Calzone build() {
//            return new Calzone(this);
//        }
//
//        @Override
//        protected Builder self() {
//            return this;
//        }
//    }
//
//    private Calzone(Builder builder) {
//        super(builder);
//        sauceInside = builder.sauceInside;
//    }
//}
//
//HawaiianPizza hawaiianPizza = new HawaiianPizza(LITTLE).addTopping(SAUSAGE).addTopping(ONION).build();
//Calzone calzone = new Calzone().addTopping(OLIVE).sauceInside().build();

/*
Pizza.Builder 클래스는 재귀적 타입 한정을 이용하는 제네릭 타입이다. 여기서 추상 메서드인 self를 통해 하위 클래스에서는 형변환 없이 메서드 연쇄를 지원할 수 있다. - self가 있는 버전과 없는 버전을 만들어 보기
self 타입이 없는 자바를 위한 이 우회 방법을 시뮬레이트한 셀프 타입 (simulated self-type) 관용구라 한다.

각 하위 클래스의 빌더가 정의한 build 메서드는 해당하는 구체 하위 클래스를 반환하도록 선언한다. HawaiianPizza.Builder -> HawaiianPizza Calzone.Builder -> Calzone
하위 클래스의 메서드가 상위 클래스의 메서드가 정의한 반납 타입이 아닌 그 하위 타입을 반환하는 기능을 공변 반환 타이핑 (covariant return typing) 이라고 한다.
이 기능을 통해 클라이언트가 형변환에 신경쓰지 않고 빌더를 사용할 수 있다.

다만 빌더 패턴의 단점인데, 이를 생성하는데 클래스 내에 매번 Builder 패턴을 작성해야 한다는 부분이다. 빌더의 생성 비용이 크지는 않으나 성능에 민감한 상황에서는 문제가 될 수 있다.
점층적 생성자 패턴보다 코드가 크기 때문에 매개변수가 4개 이상일 경우에 보다 구현하는데 비용적인 이득이 있을 것이다.
또한 만약 매개변수를 추가해야 하는 경우 상당히 번거로울 수 있기 때문에 처음부터 빌더 패턴을 통해 구현하는 편이 좋다.
* */

    public static void main(String[] args) {
        Computer myCom = new Computer.Builder("i9-10900K", "samsung 16GB x 4", "asus darkhero", "D-10000P").setHardDrive("Toshiba 1TB").setSsd("sk hynix 500GB").setMonitor("samsung 32inch odyssey").setGpu("nvidia RTX4090").build();

        System.out.println("successful made instance Computer : myCom = " + myCom);

        Hawaiian hawaiian = new Hawaiian.Builder(LITTLE).addTopping(SAUSAGE).addTopping(ONION).build();
        Calzone calzone = new Calzone.Builder().addTopping(OLIVE).sauceInside().build();

        System.out.println("Hawaiian = " + hawaiian);
        System.out.println("calzone = " + calzone);
    }
}
