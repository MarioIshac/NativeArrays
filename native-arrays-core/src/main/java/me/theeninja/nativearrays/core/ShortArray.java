package me.theeninja.nativearrays.core;

public class ShortArray extends Array<ShortArray, ShortConsumer, IndexShortPairConsumer, short[]> {
    // These two instance fields are accessed within JNI
    private final long address = 0;
    private final long size = -1;

    private static final String LIBRARY_NAME = ShortArray.class.getSimpleName();

    static {
        Array.getLibraryLoader(ShortArray.class).run();
    }

    ShortArray(long size) {
        super(size);
    }

    public static native ShortArray fromJavaArray(final short[] javaArray);

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

    @Override
    native long malloc(long size);

    @Override
    native public void sort();

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
