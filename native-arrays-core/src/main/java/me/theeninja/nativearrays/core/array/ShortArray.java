package me.theeninja.nativearrays.core.array;

import me.theeninja.nativearrays.core.array.filtered.FilteredCollection;
import me.theeninja.nativearrays.core.array.unfiltered.UnfilteredCollection;
import me.theeninja.nativearrays.core.array.unfiltered.UnfilteredShortArray;
import me.theeninja.nativearrays.core.collection.ShortCollection;
import me.theeninja.nativearrays.core.consumers.pair.IndexShortPairConsumer;
import me.theeninja.nativearrays.core.consumers.value.ShortConsumer;
import me.theeninja.nativearrays.core.operators.binary.ShortBinaryOperator;
import me.theeninja.nativearrays.core.operators.unary.ShortUnaryOperator;
import me.theeninja.nativearrays.core.predicates.ShortPredicate;

public abstract class ShortArray<
    FT extends ShortArray<
        FT,
        UT
    > & FilteredCollection<
        ShortArray<
            FT,
            UT
        >,
        FT,
        UT
    >,
    UT extends ShortArray<
        FT,
        UT
    > & UnfilteredCollection<
        ShortArray<
            FT,
            UT
        >,
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
    short[]
> implements ShortCollection<
    ShortArray<
        FT,
        UT
    >,
    FT,
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

    public abstract short reduce(ShortBinaryOperator reducer);

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
    public native long count(short value);

    @Override
    public native UT copy();

    @Override
    public native void close();
}
