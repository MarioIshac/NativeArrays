package me.theeninja.nativearrays.core.collection;

import me.theeninja.nativearrays.core.array.filtered.FilteredCollection;
import me.theeninja.nativearrays.core.array.unfiltered.UnfilteredCollection;
import me.theeninja.nativearrays.core.consumers.pair.IndexIntPairConsumer;

import java.util.function.IntBinaryOperator;
import java.util.function.IntConsumer;
import java.util.function.IntPredicate;
import java.util.function.IntUnaryOperator;

public interface IntCollection<
    T extends IntCollection<
        T,
        FT,
        UT
    >,
    FT extends IntCollection<
        T,
        FT,
        UT
    > & FilteredCollection<
        T,
        FT,
        UT
    >,
    UT extends IntCollection<
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
    IntConsumer,
    IndexIntPairConsumer,
    IntUnaryOperator,
    IntBinaryOperator,
    IntPredicate,
    int[]
> {
    int get(final long index);
    void set(final long index, final int value);

    long searchForwards(final int value);
    long searchBackwards(final int value);

    long count(final int value);

    boolean contains(final int value);

    void fill(final int value);
    int reduce(IntBinaryOperator reducer);
}
