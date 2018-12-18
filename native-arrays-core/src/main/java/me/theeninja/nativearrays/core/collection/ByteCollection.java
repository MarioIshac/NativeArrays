package me.theeninja.nativearrays.core.collection;

import me.theeninja.nativearrays.core.comparator.ByteComparator;
import me.theeninja.nativearrays.core.consumers.pair.IndexBytePairConsumer;
import me.theeninja.nativearrays.core.consumers.value.ByteConsumer;
import me.theeninja.nativearrays.core.operators.binary.ByteBinaryOperator;
import me.theeninja.nativearrays.core.operators.unary.ByteUnaryOperator;
import me.theeninja.nativearrays.core.predicates.BytePredicate;

import static me.theeninja.nativearrays.core.array.Array.NOT_FOUND;

public interface ByteCollection<
    T extends ByteCollection<
        T, UT
    >,
    UT extends T
> extends NativeCollection<
    T,
    UT,
    ByteConsumer,
    IndexBytePairConsumer,
    ByteUnaryOperator,
    BytePredicate,
    ByteComparator,
    byte[]
> {
    int get(final long index);
    void set(final long index, final byte value);

    long searchForwards(final byte value);
    long searchBackwards(final byte value);

    long count(final byte value);

    default boolean contains(final byte value) {
        return searchForwards(value) != NOT_FOUND;
    }

    void fill(final byte value);

    int reduce(ByteBinaryOperator reducer);
}
