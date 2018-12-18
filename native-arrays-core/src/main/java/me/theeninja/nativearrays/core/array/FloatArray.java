package me.theeninja.nativearrays.core.array;

import me.theeninja.nativearrays.core.array.unfiltered.UnfilteredFloatArray;
import me.theeninja.nativearrays.core.collection.FloatCollection;
import me.theeninja.nativearrays.core.comparator.FloatComparator;
import me.theeninja.nativearrays.core.consumers.pair.IndexFloatPairConsumer;
import me.theeninja.nativearrays.core.consumers.value.FloatConsumer;
import me.theeninja.nativearrays.core.operators.binary.FloatBinaryOperator;
import me.theeninja.nativearrays.core.operators.unary.FloatUnaryOperator;
import me.theeninja.nativearrays.core.predicates.FloatPredicate;

public abstract class FloatArray<
    FT extends FloatArray<
        FT,
        UT
    >,
    UT extends FloatArray<
        FT,
        UT
    >
> extends Array<
    FloatArray<
        FT,
        UT
    >,
    FT,
    UT,
    FloatConsumer,
    IndexFloatPairConsumer,
    FloatUnaryOperator,
    FloatBinaryOperator,
    FloatPredicate,
    FloatComparator,
    float[]
> implements FloatCollection<
    FloatArray<
        FT,
        UT
    >,
    UT
> {
    static {
        Array.getLibraryLoader(FloatArray.class).run();
    }

    public FloatArray(String size) {
        this(Long.parseUnsignedLong(size));
    }

    public FloatArray(long size) {
        super(size);
    }

    public static native UnfilteredFloatArray fromJavaArray(final float... javaArray);

    @Override
    public native long searchForwards(final float value);

    @Override
    public native long searchBackwards(final float value);

    public abstract long count(final float value);

    public boolean contains(final float value) {
        return searchForwards(value) != NOT_FOUND;
    }

    public static native void unload();

    @Override
    native long malloc();

    @Override
    public native UT copy();

    @Override
    public native void close();
}
