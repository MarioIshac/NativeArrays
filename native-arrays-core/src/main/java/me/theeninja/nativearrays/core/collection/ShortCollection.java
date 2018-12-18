package me.theeninja.nativearrays.core.collection;

import me.theeninja.nativearrays.core.comparator.ShortComparator;
import me.theeninja.nativearrays.core.consumers.pair.IndexShortPairConsumer;
import me.theeninja.nativearrays.core.consumers.value.ShortConsumer;
import me.theeninja.nativearrays.core.operators.binary.ShortBinaryOperator;
import me.theeninja.nativearrays.core.operators.unary.ShortUnaryOperator;
import me.theeninja.nativearrays.core.predicates.ShortPredicate;

public interface ShortCollection<
    T extends ShortCollection<
        T,
        UT
    >,
    UT extends T
> extends NativeCollection<
    T,
    UT,
    ShortConsumer,
    IndexShortPairConsumer,
    ShortUnaryOperator,
    ShortPredicate,
    ShortComparator,
    short[]
> {
    int get(final long index);
    void set(final long index, final short value);

    long searchForwards(final short value);
    long searchBackwards(final short value);

    long count(final short value);

    boolean contains(final short value);

    void fill(final short value);
    int reduce(ShortBinaryOperator reducer);
}
