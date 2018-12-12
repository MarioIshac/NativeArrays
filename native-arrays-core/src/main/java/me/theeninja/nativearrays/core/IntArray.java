package me.theeninja.nativearrays.core;

import java.util.function.IntConsumer;

public class IntArray extends Array<IntArray, IntConsumer, IndexIntPairConsumer, int[]> {
    // These two instance fields are accessed within JNI
    private final long address = 0;
    private final long size = -1;

    private static final String LIBRARY_NAME = IntArray.class.getSimpleName();

    static {
        System.out.println("Loading int array");
        Array.getLibraryLoader(IntArray.class).run();
        System.out.println("Finished loading");
    }

    public IntArray(long size) {
        super(size);
    }

    public IntArray(String size) {
        this(Long.parseUnsignedLong(size));
    }

    public static native IntArray fromJavaArray(final int... javaArray);

    public native int get(final long index);
    public native void set(final long index, final int value);

    public native long searchForwards(final int value);
    public native long searchBackwards(final int value);

    public native long count(final int value);

    public boolean contains(final int value) {
        return searchForwards(value) != NOT_FOUND;
    }

    public native void fill(final int value);

    public static native void unload();

    @Override
    native long malloc();

    @Override
    public native void sort();

    @Override
    public native int[] toJavaArray();

    @Override
    public native void intoJavaArray(int[] javaArray);

    @Override
    public native void forEachValue(IntConsumer intConsumer);

    @Override
    public native void forEachIndexValuePair(IndexIntPairConsumer indexValuePairConsumer);

    @Override
    public native IntArray copy();

    @Override
    public native void close();
}
