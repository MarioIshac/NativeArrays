package me.theeninja.nativearrays.core.consumers.pair;

import java.util.Objects;

@FunctionalInterface
public interface IndexLongPairConsumer {
    void accept(long index, long value);

    default IndexLongPairConsumer andThen(IndexLongPairConsumer after) {
        Objects.requireNonNull(after);

        return (index, value) -> {
            accept(index, value);
            after.accept(index, value);
        };
    }
}
