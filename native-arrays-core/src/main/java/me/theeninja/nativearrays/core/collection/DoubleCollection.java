package me.theeninja.nativearrays.core.collection;

import me.theeninja.nativearrays.core.comparator.DoubleComparator;
import me.theeninja.nativearrays.core.consumers.pair.IndexDoublePairConsumer;

import java.util.function.DoubleBinaryOperator;
import java.util.function.DoubleConsumer;
import java.util.function.DoublePredicate;
import java.util.function.DoubleUnaryOperator;

import static me.theeninja.nativearrays.core.array.Array.NOT_FOUND;

public interface DoubleCollection<
    T extends DoubleCollection<
        T,
        UT
    >,
    UT extends T
> extends NativeCollection<
    T,
    UT,
    DoubleConsumer,
    IndexDoublePairConsumer,
    DoubleUnaryOperator,
    DoublePredicate,
    DoubleComparator,
    double[]
> {
    int get(final long index);
    void set(final long index, final double value);

    long searchForwards(final double value);
    long searchBackwards(final double value);

    long count(final double value);

    default boolean contains(final double value) {
        return searchForwards(value) != NOT_FOUND;
    }

    void fill(final double value);

    int reduce(DoubleBinaryOperator reducer);
}
