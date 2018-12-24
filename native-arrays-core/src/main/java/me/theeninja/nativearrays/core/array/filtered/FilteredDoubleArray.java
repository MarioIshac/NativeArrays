package me.theeninja.nativearrays.core.array.filtered;

import me.theeninja.nativearrays.core.array.DoubleArray;
import me.theeninja.nativearrays.core.array.unfiltered.UnfilteredDoubleArray;
import me.theeninja.nativearrays.core.consumers.pair.IndexDoublePairConsumer;

import java.util.function.DoubleBinaryOperator;
import java.util.function.DoubleConsumer;
import java.util.function.DoublePredicate;
import java.util.function.DoubleUnaryOperator;

public class FilteredDoubleArray extends DoubleArray<
    FilteredDoubleArray,
    UnfilteredDoubleArray
> implements FilteredCollection<
    DoubleArray<
        FilteredDoubleArray,
        UnfilteredDoubleArray
    >,
    FilteredDoubleArray,
    UnfilteredDoubleArray
> {
    public FilteredDoubleArray(long size) {
        super(size);
    }

    @Override
    public native String toString();

    @Override
    public native FilteredDoubleArray iFilter(DoublePredicate predicate);

    @Override
    public native UnfilteredDoubleArray rFilter(DoublePredicate predicate);

    @Override
    public native int get(long index);

    @Override
    public native void set(long index, double value);

    @Override
    public native void fill(double value);

    @Override
    public native int reduce(DoubleBinaryOperator reducer);

    @Override
    public native UnfilteredDoubleArray rMap(DoubleUnaryOperator mapper);

    @Override
    public native void iMap(DoubleUnaryOperator mapper);

    @Override
    public native void sort(DoubleBinaryOperator comparator, long startIndex, long endIndex);

    @Override
    public native double[] toJavaArray();

    @Override
    public native void intoJavaArray(double[] javaArray);

    @Override
    public native void forEachValue(DoubleConsumer intConsumer);

    @Override
    public native void forEachIndexValuePair(IndexDoublePairConsumer indexValuePairConsumer);

    @Override
    public native void copyInto(UnfilteredDoubleArray nativeArray, long offset, long newSize);

    @Override
    public native UnfilteredDoubleArray asUnfiltered();
}
