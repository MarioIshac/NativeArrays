package me.theeninja.nativearrays.core;

import java.util.function.DoubleConsumer;

public class DoubleArray extends Array<DoubleArray, DoubleConsumer, IndexDoublePairConsumer, double[]> {
    private static final String LIBRARY_NAME = DoubleArray.class.getSimpleName();

    static {
        Array.getLibraryLoader(DoubleArray.class).run();
    }

    public DoubleArray(long size) {
        super(size);
    }

    public native int get(final long index);
    public native void set(final long index, final double value);

    public static final int NOT_FOUND = -1;

    public native long searchForwards(final double value);
    public native long searchBackwards(final double value);

    public native long count(final double value);

    public boolean contains(final double value) {
        return searchForwards(value) != NOT_FOUND;
    }

    public static native DoubleArray fromJavaArray(final double[] javaArray);

    public native void fill(final double value);

    @Override
    native long malloc(long size);

    @Override
    public native void sort();

    @Override
    public native double[] toJavaArray();

    @Override
    public native void intoJavaArray(double[] javaArray);

    @Override
    public native void forEachValue(DoubleConsumer intConsumer);

    @Override
    public native void forEachIndexValuePair(IndexDoublePairConsumer indexValuePairConsumer);

    @Override
    public native DoubleArray copy();


    public static native void unload();

    @Override
    public native void close();
}
