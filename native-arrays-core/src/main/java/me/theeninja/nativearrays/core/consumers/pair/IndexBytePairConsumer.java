package me.theeninja.nativearrays.core.consumers.pair;

import java.util.Objects;

@FunctionalInterface
public interface IndexBytePairConsumer {
    void accept(long index, byte value);

     default IndexBytePairConsumer andThen(IndexBytePairConsumer after) {
         Objects.requireNonNull(after);

         return (index, value) -> {
             accept(index, value);
             after.accept(index, value);
         };
    }
}
