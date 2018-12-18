package me.theeninja.nativearrays.core.array;

import me.theeninja.nativearrays.core.array.unfiltered.UnfilteredShortArray;
import me.theeninja.nativearrays.core.collection.NativeCollection;
import me.theeninja.nativearrays.core.collection.ShortCollection;
import me.theeninja.nativearrays.core.comparator.ShortComparator;
import me.theeninja.nativearrays.core.consumers.pair.IndexShortPairConsumer;
import me.theeninja.nativearrays.core.consumers.value.ShortConsumer;
import me.theeninja.nativearrays.core.operators.binary.ShortBinaryOperator;
import me.theeninja.nativearrays.core.operators.unary.ShortUnaryOperator;
import me.theeninja.nativearrays.core.predicates.ShortPredicate;

public abstract class ShortArray<
    FT extends ShortArray<
        FT,
        UT
    >,
    UT extends ShortArray<
        FT,
        UT
    >
> extends Array<
    ShortArray<
        FT,
        UT
    >,
    FT,
    UT,
    ShortConsumer,
    IndexShortPairConsumer,
    ShortUnaryOperator,
    ShortBinaryOperator,
    ShortPredicate,
    ShortComparator,
    short[]
> implements ShortCollection<
    ShortArray<
        FT,
        UT
    >,
    UT
> {
    static {
        Array.getLibraryLoader(ShortArray.class).run();
    }

    private ShortArray(long size) {
        super(size);
    }

    public ShortArray(String size) {
        this(Long.parseUnsignedLong(size));
    }

    public static native UnfilteredShortArray fromJavaArray(final short... javaArray);

    public static native void unload();

    public abstract int reduce(ShortBinaryOperator reducer);

    @Override
    public native long searchForwards(final short value);

    @Override
    public native long searchBackwards(final short value);

    @Override
    public boolean contains(short value) {
        return searchForwards(value) != NOT_FOUND;
    }

    @Override
    native long malloc();

    @Override
    public native UT copy();

    @Override
    public native void close();
}
