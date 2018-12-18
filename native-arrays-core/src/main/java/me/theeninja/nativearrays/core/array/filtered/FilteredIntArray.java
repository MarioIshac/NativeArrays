package me.theeninja.nativearrays.core.array.filtered;

import me.theeninja.nativearrays.core.array.unfiltered.UnfilteredDoubleArray;
import me.theeninja.nativearrays.core.consumers.pair.IndexIntPairConsumer;
import me.theeninja.nativearrays.core.array.IntArray;
import me.theeninja.nativearrays.core.comparator.IntComparator;
import me.theeninja.nativearrays.core.array.unfiltered.UnfilteredIntArray;

import java.util.function.IntBinaryOperator;
import java.util.function.IntConsumer;
import java.util.function.IntPredicate;
import java.util.function.IntUnaryOperator;

public class FilteredIntArray extends IntArray<FilteredIntArray, UnfilteredIntArray> {
    private final int excludedValue;

    FilteredIntArray(long size, long address, int excludedValue) {
        super(size, address);
        this.excludedValue = excludedValue;
    }

    @Override
    public native String toString();

    @Override
    public native FilteredIntArray iFilter(IntPredicate predicate);

    @Override
    public native UnfilteredIntArray rFilter(IntPredicate predicate);

    @Override
    public native UnfilteredIntArray rMap(final IntUnaryOperator filterUnawareMapper);

    @Override
    public native void iMap(final IntUnaryOperator filterUnawareMapper);

    @Override
    public native int get(final long requestedIndex);

    @Override
    public native void set(final long requestedIndex, final int value);

    @Override
    public native void sort();

    @Override
    public native void forEachValue(final IntConsumer filterUnawareConsumer);

    @Override
    public native void forEachIndexValuePair(final IndexIntPairConsumer indexValuePairConsumer);

    @Override
    public native void copyInto(UnfilteredIntArray nativeArray, long offset, long newSize);

    @Override
    public native void intoJavaArray(final int[] javaArray);

    @Override
    public native void sort(IntComparator comparator);

    @Override
    public native int reduce(final IntBinaryOperator filterUnawareReducer);

    @Override
    public native long count(final int value);

    public boolean isFilterAffected() {
        return contains(getExcludedValue());
    }

    @Override
    public native boolean contains(final int value);

    @Override
    public native void fill(final int value);

    @Override
    public native int[] toJavaArray();

    @Override
    public native UnfilteredIntArray copy();

    private int getExcludedValue() {
        return excludedValue;
    }

    public boolean safeContains(final int value) {
        if (value == getExcludedValue()) {
            throw new UnsupportedOperationException("value == getExpectedValue()");
        }

        return contains(value);
    }

    private void prohibitExcludedValueSearch(final int value) {
        if (value == getExcludedValue()) {
            throw new UnsupportedOperationException("value == getExpectedValue()");
        }
    }

    public long safeSearchForwards(final int value) {
        prohibitExcludedValueSearch(value);

        return searchForwards(value);
    }

    public long safeSearchBackwards(final int value) {
        prohibitExcludedValueSearch(value);

        return searchBackwards(value);
    }
}
