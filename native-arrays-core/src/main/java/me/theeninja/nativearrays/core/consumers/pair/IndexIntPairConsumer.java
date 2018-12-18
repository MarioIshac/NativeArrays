package me.theeninja.nativearrays.core.consumers.pair;

import java.util.Objects;

@FunctionalInterface
public interface IndexIntPairConsumer {
    void accept(long index, int value);

    default IndexIntPairConsumer andThen(IndexIntPairConsumer after) {
        Objects.requireNonNull(after);

        return (index, value) -> {
            accept(index, value);
            after.accept(index, value);
        };
    }
}
