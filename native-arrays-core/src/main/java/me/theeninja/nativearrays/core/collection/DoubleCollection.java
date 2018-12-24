package me.theeninja.nativearrays.core.collection;

import me.theeninja.nativearrays.core.array.filtered.FilteredCollection;
import me.theeninja.nativearrays.core.array.unfiltered.UnfilteredCollection;
import me.theeninja.nativearrays.core.consumers.pair.IndexDoublePairConsumer;

import java.util.function.DoubleBinaryOperator;
import java.util.function.DoubleConsumer;
import java.util.function.DoublePredicate;
import java.util.function.DoubleUnaryOperator;

import static me.theeninja.nativearrays.core.array.Array.NOT_FOUND;

public interface DoubleCollection<
    T extends DoubleCollection<
        T,
        FT,
        UT
    >,
    FT extends DoubleCollection<
        T,
        FT,
        UT
    > & FilteredCollection<
        T,
        FT,
        UT
    >,
    UT extends DoubleCollection<
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
    DoubleConsumer,
    IndexDoublePairConsumer,
    DoubleUnaryOperator,
    DoubleBinaryOperator,
    DoublePredicate,
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
