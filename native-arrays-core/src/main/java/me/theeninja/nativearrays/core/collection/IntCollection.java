package me.theeninja.nativearrays.core.collection;

import me.theeninja.nativearrays.core.comparator.IntComparator;
import me.theeninja.nativearrays.core.consumers.pair.IndexIntPairConsumer;

import java.util.function.IntBinaryOperator;
import java.util.function.IntConsumer;
import java.util.function.IntPredicate;
import java.util.function.IntUnaryOperator;

public interface IntCollection<
    T extends IntCollection<
        T, UT
    >,
    UT extends T
> extends NativeCollection<
    T,
    UT,
    IntConsumer,
    IndexIntPairConsumer,
    IntUnaryOperator,
    IntPredicate,
    IntComparator,
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
