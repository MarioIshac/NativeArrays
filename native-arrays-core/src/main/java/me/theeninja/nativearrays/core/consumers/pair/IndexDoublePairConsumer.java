package me.theeninja.nativearrays.core.consumers.pair;

import java.util.Objects;

@FunctionalInterface
public interface IndexDoublePairConsumer {
    void accept(long index, double value);

    default IndexDoublePairConsumer andThen(IndexDoublePairConsumer after) {
        Objects.requireNonNull(after);

        return (index, value) -> {
            accept(index, value);
            after.accept(index, value);
        };
    }
}
