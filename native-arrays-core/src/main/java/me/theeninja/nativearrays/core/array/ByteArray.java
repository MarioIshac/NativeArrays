package me.theeninja.nativearrays.core.array;

import me.theeninja.nativearrays.core.array.unfiltered.UnfilteredByteArray;
import me.theeninja.nativearrays.core.collection.ByteCollection;
import me.theeninja.nativearrays.core.comparator.ByteComparator;
import me.theeninja.nativearrays.core.consumers.pair.IndexBytePairConsumer;
import me.theeninja.nativearrays.core.consumers.value.ByteConsumer;
import me.theeninja.nativearrays.core.operators.binary.ByteBinaryOperator;
import me.theeninja.nativearrays.core.operators.unary.ByteUnaryOperator;
import me.theeninja.nativearrays.core.predicates.BytePredicate;

public abstract class ByteArray<
    FT extends ByteArray<
        FT,
        UT
    >,
    UT extends ByteArray<
        FT,
        UT
    >
> extends Array<
    ByteArray<
        FT,
        UT>,
    FT,
    UT,
    ByteConsumer,
    IndexBytePairConsumer,
    ByteUnaryOperator,
    ByteBinaryOperator,
    BytePredicate,
    ByteComparator,
    byte[]
> implements AutoCloseable, ByteCollection<
    ByteArray<
        FT,
        UT
    >,
    UT
> {
    static {
        Array.getLibraryLoader(ByteArray.class).run();

        int a = Integer.parseUnsignedInt("12");
    }

    public ByteArray(long size) {
        super(size);
    }

    @Override
    public native long searchForwards(final byte value);

    @Override
    public native long searchBackwards(final byte value);

    @Override
    public boolean contains(byte value) {
        return searchForwards(value) != NOT_FOUND;
    }

    @Override
    native long malloc();

    public static native UnfilteredByteArray fromJavaArray(final byte... javaArray);

    public static native void unload();

    @Override
    public native UT copy();

    @Override
    public native void close();
}
