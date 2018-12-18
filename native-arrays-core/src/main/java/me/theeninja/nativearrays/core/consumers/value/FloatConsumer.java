package me.theeninja.nativearrays.core.consumers.value;

import java.util.Objects;

@FunctionalInterface
public interface FloatConsumer {
    void accept(float value);

    default FloatConsumer andThen(FloatConsumer after) {
        Objects.requireNonNull(after);

        return value -> {
            accept(value);
            after.accept(value);
        };
    }
}
