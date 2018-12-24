package me.theeninja.nativearrays.core.array;

import me.theeninja.nativearrays.core.array.filtered.FilteredCollection;
import me.theeninja.nativearrays.core.array.unfiltered.UnfilteredCollection;
import me.theeninja.nativearrays.core.array.unfiltered.UnfilteredIntArray;
import me.theeninja.nativearrays.core.collection.IntCollection;
import me.theeninja.nativearrays.core.consumers.pair.IndexIntPairConsumer;

import java.util.function.IntBinaryOperator;
import java.util.function.IntConsumer;
import java.util.function.IntPredicate;
import java.util.function.IntUnaryOperator;

public abstract class IntArray<
    FT extends IntArray<
        FT,
        UT
    > & FilteredCollection<
        IntArray<
            FT,
            UT
        >,
        FT,
        UT
    >,
    UT extends IntArray<
        FT,
        UT
    > & UnfilteredCollection<
        IntArray<
            FT,
            UT
        >,
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
    int[]
> implements IntCollection<
    IntArray<
        FT,
        UT
    >,
    FT,
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

    @Override
    public boolean contains(int value) {
        return searchForwards(value) != NOT_FOUND;
    }

    @Override
    public native long count(final int value);

    public static native void unload();

    @Override
    native long malloc();

    @Override
    public native UT copy();

    @Override
    public native void close();
}
