package me.theeninja.nativearrays.core;

import java.util.function.Consumer;
import java.util.function.IntConsumer;
import java.util.function.Supplier;

public class IntArray implements AutoCloseable {
    // These two instance fields are accessed within JNI
    private final long address;
    private final long size;

    private static final String LIBRARY_NAME = IntArray.class.getSimpleName();

    static {
        System.load("D:/DevelopmentWorkspaces/Java/NativeArrays/native-arrays-core/src/main/resources/native/" + LIBRARY_NAME + ".dll");
    }

    private IntArray() {
        throw new UnsupportedOperationException("Must construct IntArray through JNI interface.");
    }

    public native int get(final long index);
    public native void set(final long index, final int value);

    public static final int NOT_FOUND = -1;

    public native long searchForwards(final int value);
    public native long searchBackwards(final int value);

    public native long count(final int value);

    public boolean contains(final int value) {
        return searchForwards(value) != NOT_FOUND;
    }

    public native void sort();

    public native void fill(final int value);

    public static native IntArray fromJavaArray(final int... javaArray);
    public native int[] toJavaArray();
    public native void intoJavaArray(final int[] javaArray);

    public native void forEachValue(final IntConsumer intConsumer);
    public native void forEachIndexValuePair(final IndexIntPairConsumer indexValuePairConsumer);

    public native IntArray copy();

    public static native IntArray create();

    public long size() {
        return size;
    }

    @Override
    public native boolean equals(final Object object);

    @Override
    public native int hashCode();

    @Override
    public native String toString();

    @Override
    public native void close();

    public static void tryAndClose(final Supplier<IntArray> intArraySupplier, final Consumer<IntArray> intArrayConsumer) {
        try (final IntArray intArray = intArraySupplier.get()) {
            intArrayConsumer.accept(intArray);
        }
    }

    public static native void unload();
}
