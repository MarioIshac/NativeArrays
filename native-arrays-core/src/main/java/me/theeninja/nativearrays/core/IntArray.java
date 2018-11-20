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

    private IntArray(final long size) {
        this.size = size;
        this.address = allocate();
    }

    public native int get(long index);
    public native void set(long index, int value);

    public static final int NOT_FOUND = -1;

    public native IntArray newIntArray();

    public native long searchForwards(int value);
    public native long searchBackwards(int value);
    public native long count(int value);

    public boolean contains(int value) {
        return searchForwards(value) != NOT_FOUND;
    }

    public native void sort();

    private native long allocate();
    public native void fill(int value);

    public static native IntArray fromJavaArray(int... javaArray);
    public native int[] toJavaArray();
    public native void intoJavaArray(final int[] javaArray);

    public native void forEachValue(IntConsumer intConsumer);
    public native void forEachIndexValuePair(IndexValuePairConsumer indexValuePairConsumer);

    public native IntArray copy();

    public long size() {
        return size;
    }

    @Override
    public native boolean equals(Object object);

    @Override
    public native int hashCode();

    @Override
    public native String toString();

    @Override
    public native void close();

    public static void tryAndClose(Supplier<IntArray> intArraySupplier, Consumer<IntArray> intArrayConsumer) {
        try (IntArray intArray = intArraySupplier.get()) {
            intArrayConsumer.accept(intArray);
        }
    }

    /**
     * {@inheritDoc}
     *
     * What is returned is a non-native {@code Iterator<Integer>} that suffers performance penalties of autoboxing.
     *
     * @see IntArray#forEach
     * @return
     */
    /* @Override
    public Iterator<Integer> iterator() {
        return new IntArrayIterator(this);
    } */
}
