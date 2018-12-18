package me.theeninja.nativearrays.core.array;

import me.theeninja.nativearrays.core.array.filtered.FilteredLongArray;
import me.theeninja.nativearrays.core.array.unfiltered.UnfilteredLongArray;
import me.theeninja.nativearrays.core.collection.LongCollection;
import me.theeninja.nativearrays.core.comparator.LongComparator;
import me.theeninja.nativearrays.core.consumers.pair.IndexLongPairConsumer;

import java.util.function.*;

public abstract class LongArray<
    FT extends LongArray<
        FT,
        UT
    >,
    UT extends LongArray<
        FT,
        UT
    >
> extends Array<
    LongArray<
        FT,
        UT
    >,
    FT,
    UT,
    LongConsumer,
    IndexLongPairConsumer,
    LongUnaryOperator,
    LongBinaryOperator,
    LongPredicate,
    LongComparator,
    long[]
> implements LongCollection<
    LongArray<
        FT,
        UT
    >,
    UT
> {
    static {
        Array.getLibraryLoader(LongArray.class).run();
    }

    public LongArray(long size) {
        super(size);
    }

    public LongArray(String size) {
        this(Long.parseUnsignedLong(size));
    }

    public static native UnfilteredLongArray fromJavaArray(final long... javaArray);

    public abstract int get(final long index);
    public abstract void set(final long index, final long value);

    @Override
    public native long searchForwards(final long value);

    @Override
    public native long searchBackwards(final long value);

    public abstract long count(final long value);

    public boolean contains(final long value) {
        return searchForwards(value) != NOT_FOUND;
    }

    public abstract void fill(final long value);

    public static native void unload();

    public abstract int reduce(LongBinaryOperator reducer);

    @Override
    native long malloc();

    @Override
    public native UT copy();

    @Override
    public native void close();
}
