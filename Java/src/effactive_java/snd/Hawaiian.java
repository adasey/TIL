package effactive_java.snd;

import java.util.Objects;

public class Hawaiian extends Pizza {
    public enum Pineapple { NONE, LITTLE, MEDIUM, MANY}
    private final Pineapple pineapple;

    public static class Builder extends Pizza.Builder<Builder> {
        private final Pineapple pineapple;

        public Builder(Pineapple pineapple) {
            this.pineapple = Objects.requireNonNull(pineapple);
        }

        @Override
        public Hawaiian build() {
            return new Hawaiian(this);
        }

        @Override
        protected Builder self() {
            return this;
        }
    }

    private Hawaiian(Builder builder) {
        super(builder);
        pineapple = builder.pineapple;
    }
}
