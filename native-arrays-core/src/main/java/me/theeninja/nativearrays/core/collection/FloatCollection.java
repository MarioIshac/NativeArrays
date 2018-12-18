package me.theeninja.nativearrays.core.collection;

import me.theeninja.nativearrays.core.comparator.FloatComparator;
import me.theeninja.nativearrays.core.comparator.IntComparator;
import me.theeninja.nativearrays.core.consumers.pair.IndexFloatPairConsumer;
import me.theeninja.nativearrays.core.consumers.pair.IndexIntPairConsumer;
import me.theeninja.nativearrays.core.consumers.value.FloatConsumer;
import me.theeninja.nativearrays.core.operators.binary.FloatBinaryOperator;
import me.theeninja.nativearrays.core.operators.unary.FloatUnaryOperator;
import me.theeninja.nativearrays.core.predicates.FloatPredicate;

import java.util.function.DoubleBinaryOperator;
import java.util.function.IntConsumer;
import java.util.function.IntPredicate;
import java.util.function.IntUnaryOperator;

import static me.theeninja.nativearrays.core.array.Array.NOT_FOUND;

public interface FloatCollection<
    T extends FloatCollection<
        T, UT
    >,
    UT extends T
> extends NativeCollection<
    T,
    UT,
    FloatConsumer,
    IndexFloatPairConsumer,
    FloatUnaryOperator,
    FloatPredicate,
    FloatComparator,
    float[]
>{
    int get(final long index);
    void set(final long index, final float value);

    long searchForwards(final float value);
    long searchBackwards(final float value);

    long count(final float value);

    default boolean contains(final float value) {
        return searchForwards(value) != NOT_FOUND;
    }

    void fill(final float value);

    int reduce(FloatBinaryOperator reducer);
}
