package me.theeninja.nativearrays.core.collection;

import me.theeninja.nativearrays.core.array.filtered.FilteredCollection;
import me.theeninja.nativearrays.core.array.unfiltered.UnfilteredCollection;
import me.theeninja.nativearrays.core.consumers.pair.IndexFloatPairConsumer;
import me.theeninja.nativearrays.core.consumers.value.FloatConsumer;
import me.theeninja.nativearrays.core.operators.binary.FloatBinaryOperator;
import me.theeninja.nativearrays.core.operators.unary.FloatUnaryOperator;
import me.theeninja.nativearrays.core.predicates.FloatPredicate;

import static me.theeninja.nativearrays.core.array.Array.NOT_FOUND;

public interface FloatCollection<
    T extends FloatCollection<
        T,
        FT,
        UT
    >,
    FT extends FloatCollection<
        T,
        FT,
        UT
    > & FilteredCollection<
        T,
        FT,
        UT
    >,
    UT extends FloatCollection<
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
    FloatConsumer,
    IndexFloatPairConsumer,
    FloatUnaryOperator,
    FloatBinaryOperator,
    FloatPredicate,
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
