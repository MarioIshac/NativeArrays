package me.theeninja.nativearrays.core;

public class FloatArray extends Array<FloatArray, FloatConsumer, IndexFloatPairConsumer, float[]> {
    // These two instance fields are accessed within JNI
    private static final String LIBRARY_NAME = FloatArray.class.getSimpleName();

    static {
        Array.getLibraryLoader(FloatArray.class).run();
    }

    FloatArray(long size) {
        super(size);
    }

    public static native FloatArray fromJavaArray(final float[] javaArray);

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
    native long malloc(long size);

    @Override
    public native void sort();

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
