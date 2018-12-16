package me.theeninja.nativearrays.core;

import java.util.function.*;

public class IntArray extends Array<IntArray, IntConsumer, IndexIntPairConsumer, IntUnaryOperator, IntBinaryOperator, IntPredicate, IntComparator, int[]> {
    private static final String LIBRARY_NAME = IntArray.class.getSimpleName();

    static {
        System.out.println("Loading int array");
        Array.getLibraryLoader(IntArray.class).run();
        System.out.println("Finished loading");
    }

    public IntArray(long size) {
        super(size);
    }

    @Override
    public native IntArray map(IntUnaryOperator mapper);

    @Override
    public native void mapLocally(IntUnaryOperator mapper);

    @Override
    public native IntArray filter(IntPredicate predicate);

    @Override
    public native int filterLocally(IntPredicate predicate);

    public IntArray(String size) {
        this(Long.parseUnsignedLong(size));

        IndexIntPairConsumer c = (a, b) -> System.out.println(a + " " + b);

        ByteArray byteArray = new ByteArray(5);

        byteArray.forEachIndexValuePair(c::accept);
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

    public native int reduce(IntBinaryOperator reducer);

    @Override
    native long malloc();

    @Override
    public native void sort();

    @Override
    public native void sort(IntComparator comparator);

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
