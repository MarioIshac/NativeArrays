package me.theeninja.nativearrays.core.array.filtered;

import me.theeninja.nativearrays.core.array.ShortArray;
import me.theeninja.nativearrays.core.array.unfiltered.UnfilteredDoubleArray;
import me.theeninja.nativearrays.core.array.unfiltered.UnfilteredShortArray;
import me.theeninja.nativearrays.core.comparator.ShortComparator;
import me.theeninja.nativearrays.core.consumers.pair.IndexShortPairConsumer;
import me.theeninja.nativearrays.core.consumers.value.ShortConsumer;
import me.theeninja.nativearrays.core.operators.binary.ShortBinaryOperator;
import me.theeninja.nativearrays.core.operators.unary.ShortUnaryOperator;
import me.theeninja.nativearrays.core.predicates.ShortPredicate;

public class FilteredShortArray extends ShortArray<FilteredShortArray, UnfilteredShortArray> {
    @Override
    public native String toString();

    @Override
    public native FilteredShortArray iFilter(ShortPredicate predicate);

    public FilteredShortArray(String size) {
        super(size);
    }

    @Override
    public native int get(long index);

    @Override
    public native void set(long index, short value);

    @Override
    public native long searchForwards(short value);

    @Override
    public native long searchBackwards(short value);

    @Override
    public native long count(short value);

    @Override
    public native void fill(short value);

    @Override
    public native int reduce(ShortBinaryOperator reducer);

    @Override
    public native UnfilteredShortArray rFilter(ShortPredicate predicate);

    @Override
    public native UnfilteredShortArray rMap(ShortUnaryOperator mapper);

    @Override
    public native void iMap(ShortUnaryOperator mapper);

    @Override
    public native void sort();

    @Override
    public native void sort(ShortComparator comparator);

    @Override
    public native short[] toJavaArray();

    @Override
    public native void intoJavaArray(short[] javaArray);

    @Override
    public native void forEachValue(ShortConsumer intConsumer);

    @Override
    public native void forEachIndexValuePair(IndexShortPairConsumer indexValuePairConsumer);

    @Override
    public native void copyInto(UnfilteredShortArray nativeArray, long offset, long newSize);
}
