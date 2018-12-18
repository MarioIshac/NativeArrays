package me.theeninja.nativearrays.core.consumers.pair;

import java.util.Objects;

@FunctionalInterface
public interface IndexFloatPairConsumer{
    void accept(long index, float value);

    default IndexFloatPairConsumer andThen(IndexFloatPairConsumer after) {
        Objects.requireNonNull(after);

        return (index, value) -> {
            accept(index, value);
            after.accept(index, value);
        };
    }
}
