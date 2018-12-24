package me.theeninja.nativearrays.core.collection;

import me.theeninja.nativearrays.core.array.filtered.FilteredCollection;
import me.theeninja.nativearrays.core.array.unfiltered.UnfilteredCollection;
import me.theeninja.nativearrays.core.consumers.pair.IndexShortPairConsumer;
import me.theeninja.nativearrays.core.consumers.value.ShortConsumer;
import me.theeninja.nativearrays.core.operators.binary.ShortBinaryOperator;
import me.theeninja.nativearrays.core.operators.unary.ShortUnaryOperator;
import me.theeninja.nativearrays.core.predicates.ShortPredicate;

public interface ShortCollection<
    T extends ShortCollection<
        T,
        FT,
        UT
    >,
    FT extends ShortCollection<
        T,
        FT,
        UT
    > & FilteredCollection<
        T,
        FT,
        UT
    >,
    UT extends ShortCollection<
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
    ShortConsumer,
    IndexShortPairConsumer,
    ShortUnaryOperator,
    ShortBinaryOperator,
    ShortPredicate,
    short[]
> {
    int get(final long index);
    void set(final long index, final short value);

    long searchForwards(final short value);
    long searchBackwards(final short value);

    long count(final short value);

    boolean contains(final short value);

    void fill(final short value);
    short reduce(ShortBinaryOperator reducer);
}
