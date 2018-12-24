package me.theeninja.nativearrays.core.collection;

import me.theeninja.nativearrays.core.array.filtered.FilteredCollection;
import me.theeninja.nativearrays.core.array.unfiltered.UnfilteredCollection;
import me.theeninja.nativearrays.core.consumers.pair.IndexLongPairConsumer;

import java.util.function.LongBinaryOperator;
import java.util.function.LongConsumer;
import java.util.function.LongPredicate;
import java.util.function.LongUnaryOperator;

import static me.theeninja.nativearrays.core.array.Array.NOT_FOUND;

public interface LongCollection<
    T extends LongCollection<
        T,
        FT,
        UT
    >,
    FT extends LongCollection<
        T,
        FT,
        UT
    > & FilteredCollection<
        T,
        FT,
        UT
    >,
    UT extends LongCollection<
        T,
        FT,
        UT
    > & UnfilteredCollection<
        T,
        FT,
        UT
    >
> extends NativeCollection<
    T,
    FT,
    UT,
    LongConsumer,
    IndexLongPairConsumer,
    LongUnaryOperator,
    LongBinaryOperator,
    LongPredicate,
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
