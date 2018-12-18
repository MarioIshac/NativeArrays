package me.theeninja.nativearrays.core.array.filtered;

import me.theeninja.nativearrays.core.array.ByteArray;
import me.theeninja.nativearrays.core.array.unfiltered.UnfilteredByteArray;
import me.theeninja.nativearrays.core.comparator.ByteComparator;
import me.theeninja.nativearrays.core.consumers.pair.IndexBytePairConsumer;
import me.theeninja.nativearrays.core.consumers.value.ByteConsumer;
import me.theeninja.nativearrays.core.operators.binary.ByteBinaryOperator;
import me.theeninja.nativearrays.core.operators.unary.ByteUnaryOperator;
import me.theeninja.nativearrays.core.predicates.BytePredicate;

public class FilteredByteArray extends ByteArray<FilteredByteArray, UnfilteredByteArray> {
    public FilteredByteArray(long size) {
        super(size);
    }

    @Override
    public native String toString();

    @Override
    public native FilteredByteArray iFilter(BytePredicate bytePredicate);

    @Override
    public native UnfilteredByteArray rFilter(BytePredicate bytePredicate);

    @Override
    public native int get(long index);

    @Override
    public native void set(long index, byte value);

    @Override
    public native long count(byte value);

    @Override
    public native void fill(byte value);

    @Override
    public native int reduce(ByteBinaryOperator reducer);

    @Override
    public native UnfilteredByteArray rMap(ByteUnaryOperator mapper);

    @Override
    public native void iMap(ByteUnaryOperator mapper);

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
    public native void copyInto(UnfilteredByteArray nativeArray, long offset, long newSize);
}
