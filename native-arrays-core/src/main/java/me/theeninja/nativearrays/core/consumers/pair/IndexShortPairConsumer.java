package me.theeninja.nativearrays.core.consumers.pair;

import java.util.Objects;

@FunctionalInterface
public interface IndexShortPairConsumer {
    void accept(long index, short value);

    default IndexShortPairConsumer andThen(IndexShortPairConsumer after) {
        Objects.requireNonNull(after);

        return (index, value) -> {
            accept(index, value);
            after.accept(index, value);
        };
    }
}
