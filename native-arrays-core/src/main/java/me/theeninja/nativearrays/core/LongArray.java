package me.theeninja.nativearrays.core;

import java.util.function.LongConsumer;

public class LongArray extends Array<LongArray, LongConsumer, IndexLongPairConsumer, long[]> {
    private static final String LIBRARY_NAME = LongArray.class.getSimpleName();

    static {
        Array.getLibraryLoader(LongArray.class).run();
    }

    LongArray(long size) {
        super(size);
    }

    public static native LongArray fromJavaArray(final long[] javaArray);

    public native int get(final long index);
    public native void set(final long index, final long value);

    public static final long NOT_FOUND = -1;

    public native long searchForwards(final long value);
    public native long searchBackwards(final long value);

    public native long count(final long value);

    public boolean contains(final long value) {
        return searchForwards(value) != NOT_FOUND;
    }

    public native void fill(final long value);

    public static native void unload();

    @Override
    native long malloc(long size);

    @Override
    public native void sort();

    @Override
    public native long[] toJavaArray();

    @Override
    public native void intoJavaArray(long[] javaArray);

    @Override
    public native void forEachValue(LongConsumer intConsumer);

    @Override
    public native void forEachIndexValuePair(IndexLongPairConsumer indexValuePairConsumer);

    @Override
    public native LongArray copy();

    @Override
    public native void close() throws Exception;
}
