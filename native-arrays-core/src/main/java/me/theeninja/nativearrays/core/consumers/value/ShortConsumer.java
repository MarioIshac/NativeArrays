package me.theeninja.nativearrays.core.consumers.value;

import java.util.Objects;

@FunctionalInterface
public interface ShortConsumer {
    void accept(short value);

    default ShortConsumer andThen(ShortConsumer after) {
        Objects.requireNonNull(after);

        return value -> {
            accept(value);
            after.accept(value);
        };
    }
}
