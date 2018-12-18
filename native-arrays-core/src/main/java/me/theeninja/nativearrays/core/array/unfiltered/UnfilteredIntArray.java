package me.theeninja.nativearrays.core.array.unfiltered;

import me.theeninja.nativearrays.core.array.filtered.FilteredIntArray;
import me.theeninja.nativearrays.core.consumers.pair.IndexIntPairConsumer;
import me.theeninja.nativearrays.core.array.IntArray;
import me.theeninja.nativearrays.core.comparator.IntComparator;
import me.theeninja.nativearrays.core.predicates.FloatPredicate;

import java.util.function.IntBinaryOperator;
import java.util.function.IntConsumer;
import java.util.function.IntPredicate;
import java.util.function.IntUnaryOperator;

public class UnfilteredIntArray extends IntArray<FilteredIntArray, UnfilteredIntArray> {
    public UnfilteredIntArray(long size) {
        super(size);
    }

    @Override
    public native String toString();

    @Override
    public native FilteredIntArray iFilter(IntPredicate predicate);

    @Override
    public native UnfilteredIntArray rFilter(IntPredicate predicate);

    @Override
    public native UnfilteredIntArray rMap(IntUnaryOperator mapper);

    @Override
    public native void iMap(IntUnaryOperator mapper);

    @Override
    public native int filterInPlaceWithSwap(IntPredicate predicate);

    @Override
    public native int get(long index);

    @Override
    public native void set(long index, int value);

    @Override
    public native long searchForwards(int value);

    @Override
    public native long searchBackwards(int value);

    @Override
    public native long count(int value);

    @Override
    public native boolean contains(int value);

    @Override
    public native void fill(int value);

    @Override
    public native int reduce(IntBinaryOperator reducer);

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
    public native void copyInto(UnfilteredIntArray nativeArray, long offset, long newSize);

    @Override
    public native UnfilteredIntArray copy();
}
