package effactive_java.snd;

public class Computer {
    private final String cpu;
    private final String ram;
    private final String mainBoard;
    private final String power;
    private final String hardDrive;
    private final String ssd;
    private final String monitor;
    private final String gpu;

    public static class Builder {
        private final String cpu;
        private final String ram;
        private final String mainBoard;
        private final String power;

        private String hardDrive = "toshiba 128GB";
        private String ssd = "samsung 128GB";
        private String monitor = "LG g9703";
        private String gpu = "nvidia RTX3080";

        public Builder(String cpu, String ram, String mainBoard, String power) {
            this.cpu = cpu;
            this.ram = ram;
            this.mainBoard = mainBoard;
            this.power = power;
        }

        public Builder setHardDrive(String val) {
            hardDrive = val;
            return this;
        }

        public Builder setSsd(String val) {
            ssd = val;
            return this;
        }

        public Builder setMonitor(String val) {
            monitor = val;
            return this;
        }

        public Builder setGpu(String val) {
            gpu = val;
            return this;
        }

        public Computer build() {
            return new Computer(this);
        }
    }

    private Computer(Builder builder) {
        cpu = builder.cpu;
        ram = builder.ram;
        mainBoard = builder.mainBoard;;
        power = builder.power;
        hardDrive = builder.hardDrive;
        ssd = builder.ssd;
        monitor = builder.monitor;
        gpu = builder.gpu;
    }
}
