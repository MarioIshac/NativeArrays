package me.theeninja.nativearrays.core.array;

import me.theeninja.nativearrays.core.array.filtered.FilteredCollection;
import me.theeninja.nativearrays.core.array.unfiltered.UnfilteredCollection;
import me.theeninja.nativearrays.core.array.unfiltered.UnfilteredDoubleArray;
import me.theeninja.nativearrays.core.collection.DoubleCollection;
import me.theeninja.nativearrays.core.consumers.pair.IndexDoublePairConsumer;

import java.util.function.DoubleBinaryOperator;
import java.util.function.DoubleConsumer;
import java.util.function.DoublePredicate;
import java.util.function.DoubleUnaryOperator;

public abstract class DoubleArray<
    FT extends DoubleArray<
        FT,
        UT
    > & FilteredCollection<
        DoubleArray<
            FT,
            UT
        >,
        FT,
        UT
    >,
    UT extends DoubleArray<
        FT,
        UT
    > & UnfilteredCollection<
        DoubleArray<
            FT,
            UT
        >,
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
    double[]
> implements DoubleCollection<
    DoubleArray<
        FT,
        UT
    >,
    FT,
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

    @Override
    public native long count(double value);

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
