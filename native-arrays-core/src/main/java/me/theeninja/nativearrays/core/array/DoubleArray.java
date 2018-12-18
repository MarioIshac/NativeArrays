package me.theeninja.nativearrays.core.array;

import me.theeninja.nativearrays.core.array.unfiltered.UnfilteredDoubleArray;
import me.theeninja.nativearrays.core.collection.DoubleCollection;
import me.theeninja.nativearrays.core.comparator.DoubleComparator;
import me.theeninja.nativearrays.core.consumers.pair.IndexDoublePairConsumer;

import java.util.function.*;

public abstract class DoubleArray<
    FT extends DoubleArray<
        FT,
        UT
    >,
    UT extends DoubleArray<
        FT,
        UT
    >
> extends Array<
    DoubleArray<
        FT,
        UT
    >,
    FT,
    UT,
    DoubleConsumer,
    IndexDoublePairConsumer,
    DoubleUnaryOperator,
    DoubleBinaryOperator,
    DoublePredicate,
    DoubleComparator,
    double[]
> implements DoubleCollection<
    DoubleArray<
        FT, UT
    >,
    UT
> {
    static {
        Array.getLibraryLoader(DoubleArray.class).run();
    }

    public DoubleArray(long size) {
        super(size);
    }

    public DoubleArray(String size) {
        this(Long.parseUnsignedLong(size));
    }

    @Override
    native long malloc();

    public static native UnfilteredDoubleArray fromJavaArray(final double... javaArray);

    @Override
    public native long searchForwards(final double value);

    @Override
    public native long searchBackwards(final double value);

    public static native void unload();

    @Override
    public native UT copy();

    @Override
    public native void close();
}
