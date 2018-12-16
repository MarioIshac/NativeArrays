package me.theeninja.nativearrays.core;

public class ByteArray extends Array<ByteArray, ByteConsumer, IndexBytePairConsumer, ByteUnaryOperator, ByteBinaryOperator, BytePredicate, ByteComparator, byte[]> implements AutoCloseable {
    static {
        Array.getLibraryLoader(ByteArray.class).run();

        int a = Integer.parseUnsignedInt("12");
    }

    public ByteArray(long size) {
        super(size);
    }

    @Override
    public native ByteArray map(ByteUnaryOperator mapper);

    @Override
    public native void mapLocally(ByteUnaryOperator mapper);

    @Override
    public native ByteArray filter(BytePredicate predicate);

    @Override
    public native int filterLocally(BytePredicate predicate);

    @Override
    native long malloc();

    public native byte reduce(ByteBinaryOperator reducer);

    public native byte get(final long index);
    public native void set(final long index, final byte value);

    public native long searchForwards(final byte value);
    public native long searchBackwards(final byte value);

    public native long count(final byte value);

    public boolean contains(final byte value) {
        return searchForwards(value) != Array.NOT_FOUND;
    }

    public static native ByteArray fromJavaArray(final byte... javaArray);

    public native void fill(final byte value);

    @Override
    public native boolean equals(final Object object);

    @Override
    public native int hashCode();

    @Override
    public native String toString();

    @Override
    public native void close();

    public static native void unload();

    @Override
    public native void sort();

    @Override
    public native void sort(ByteComparator comparator);

    @Override
    public native byte[] toJavaArray();

    @Override
    public native void intoJavaArray(byte[] javaArray);

    @Override
    public native void forEachValue(ByteConsumer intConsumer);

    @Override
    public native void forEachIndexValuePair(IndexBytePairConsumer indexValuePairConsumer);

    @Override
    public native ByteArray copy();
}
