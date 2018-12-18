package me.theeninja.nativearrays.core.collection;

import me.theeninja.nativearrays.core.comparator.LongComparator;
import me.theeninja.nativearrays.core.consumers.pair.IndexLongPairConsumer;

import java.util.function.*;

import static me.theeninja.nativearrays.core.array.Array.NOT_FOUND;

public interface LongCollection<
    T extends LongCollection<
        T, UT
    >,
    UT extends T
> extends NativeCollection<
    T,
    UT,
    LongConsumer,
    IndexLongPairConsumer,
    LongUnaryOperator,
    LongPredicate,
    LongComparator,
    long[]
> {
    int get(final long index);
    void set(final long index, final long value);

    long searchForwards(final long value);
    long searchBackwards(final long value);

    long count(final long value);

    default boolean contains(final long value) {
        return searchForwards(value) != NOT_FOUND;
    }

    void fill(final long value);

    int reduce(final LongBinaryOperator reducer);
}
