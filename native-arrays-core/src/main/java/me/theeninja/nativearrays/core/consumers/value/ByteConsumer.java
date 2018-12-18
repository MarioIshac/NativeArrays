package me.theeninja.nativearrays.core.consumers.value;

import java.util.Objects;

@FunctionalInterface
public interface ByteConsumer { 
    void accept(byte value);

    default ByteConsumer andThen(ByteConsumer after) {
        Objects.requireNonNull(after);

        return value -> {
            accept(value);
            after.accept(value);
        };
    }
}
