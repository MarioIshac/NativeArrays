package me.theeninja.nativearrays.core;

import java.util.function.DoubleBinaryOperator;

public class ShortArray extends Array<ShortArray, ShortConsumer, IndexShortPairConsumer, ShortUnaryOperator, ShortBinaryOperator, ShortPredicate, ShortComparator, short[]> {
    static {
        Array.getLibraryLoader(ShortArray.class).run();
    }

    private ShortArray(long size) {
        super(size);
    }

    @Override
    public native ShortArray map(ShortUnaryOperator mapper);

    @Override
    public native void mapLocally(ShortUnaryOperator mapper);

    @Override
    public native int filterLocally(ShortPredicate predicate);

    @Override
    public native ShortArray filter(ShortPredicate predicate);

    public ShortArray(String size) {
        this(Long.parseUnsignedLong(size));
    }

    public static native ShortArray fromJavaArray(final short... javaArray);

    public native int get(final long index);
    public native void set(final long index, final short value);

    public static final long NOT_FOUND = -1;

    public native long searchForwards(final short value);
    public native long searchBackwards(final short value);

    public native long count(final short value);

    public boolean contains(final short value) {
        return searchForwards(value) != NOT_FOUND;
    }

    public native void fill(final short value);

    public static native void unload();

    public native short reduce(ShortBinaryOperator reducer);

    @Override
    native long malloc();

    @Override
    native public void sort();

    @Override
    public native void sort(ShortComparator comparator);

    @Override
    public native short[] toJavaArray();

    @Override
    public native void intoJavaArray(short[] javaArray);

    @Override
    public native void forEachValue(ShortConsumer intConsumer);

    @Override
    public native void forEachIndexValuePair(IndexShortPairConsumer indexValuePairConsumer);

    @Override
    public native ShortArray copy();

    @Override
    public native void close() throws Exception;
}
