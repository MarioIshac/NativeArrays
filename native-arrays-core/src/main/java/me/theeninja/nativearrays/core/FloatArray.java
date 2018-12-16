package me.theeninja.nativearrays.core;

import java.util.function.DoubleBinaryOperator;

public class FloatArray extends Array<FloatArray, FloatConsumer, IndexFloatPairConsumer, FloatUnaryOperator, FloatBinaryOperator, FloatPredicate, FloatComparator, float[]> {
    static {
        Array.getLibraryLoader(FloatArray.class).run();
    }

    public FloatArray(String size) {
        this(Long.parseUnsignedLong(size));
    }

    public FloatArray(long size) {
        super(size);
    }

    @Override
    public native FloatArray map(FloatUnaryOperator mapper);

    @Override
    public native void mapLocally(FloatUnaryOperator mapper);

    @Override
    public native FloatArray filter(FloatPredicate predicate);

    @Override
    public native int filterLocally(FloatPredicate predicate);

    public static native FloatArray fromJavaArray(final float... javaArray);

    public native int get(final long index);
    public native void set(final long index, final float value);

    public native long searchForwards(final float value);
    public native long searchBackwards(final float value);

    public native long count(final float value);

    public boolean contains(final float value) {
        return searchForwards(value) != NOT_FOUND;
    }

    public native void fill(final float value);

    public static native void unload();

    @Override
    native long malloc();

    public native float reduce(FloatBinaryOperator reducer);

    @Override
    public native void sort();

    @Override
    public void sort(FloatComparator comparator) {

    }

    @Override
    public native float[] toJavaArray();

    @Override
    public native void intoJavaArray(float[] javaArray);

    @Override
    public native void forEachValue(FloatConsumer intConsumer);

    @Override
    public native void forEachIndexValuePair(IndexFloatPairConsumer indexValuePairConsumer);

    @Override
    public native FloatArray copy();

    @Override
    public native void close() throws Exception;
}
