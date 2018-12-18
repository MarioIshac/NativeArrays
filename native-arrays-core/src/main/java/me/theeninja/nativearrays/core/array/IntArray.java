package me.theeninja.nativearrays.core.array;

import me.theeninja.nativearrays.core.collection.IntCollection;
import me.theeninja.nativearrays.core.comparator.IntComparator;
import me.theeninja.nativearrays.core.array.unfiltered.UnfilteredIntArray;
import me.theeninja.nativearrays.core.consumers.pair.IndexIntPairConsumer;

import java.util.function.*;

public abstract class IntArray<
    FT extends IntArray<
        FT,
        UT
    >,
    UT extends IntArray<
        FT,
        UT
    >
> extends Array<
    IntArray<
        FT,
        UT
    >,
    FT,
    UT,
    IntConsumer,
    IndexIntPairConsumer,
    IntUnaryOperator,
    IntBinaryOperator,
    IntPredicate,
    IntComparator,
    int[]
> implements IntCollection<
    IntArray<
        FT,
        UT
    >,
    UT
> {
    static {
        System.out.println("Loading int array");
        Array.getLibraryLoader(IntArray.class).run();
        System.out.println("Finished loading");
    }

    public static UnfilteredIntArray of(long size) {
        return new UnfilteredIntArray(size);
    }

    public IntArray(long size) {
        super(size);
    }

    protected IntArray(long size, long address) {
        super(size, address);
    }

    public native int filterInPlaceWithSwap(IntPredicate predicate);

    public IntArray(String size) {
        this(Long.parseUnsignedLong(size));
    }

    public static native UnfilteredIntArray fromJavaArray(final int... javaArray);

    @Override
    public native long searchForwards(final int value);

    @Override
    public native long searchBackwards(final int value);

    public static native void unload();

    @Override
    native long malloc();

    @Override
    public native UT copy();

    @Override
    public native void close();
}
