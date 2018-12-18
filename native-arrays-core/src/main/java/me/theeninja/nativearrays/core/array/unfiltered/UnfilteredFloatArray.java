package me.theeninja.nativearrays.core.array.unfiltered;

import me.theeninja.nativearrays.core.array.FloatArray;
import me.theeninja.nativearrays.core.array.filtered.FilteredFloatArray;
import me.theeninja.nativearrays.core.comparator.FloatComparator;
import me.theeninja.nativearrays.core.consumers.pair.IndexFloatPairConsumer;
import me.theeninja.nativearrays.core.consumers.value.FloatConsumer;
import me.theeninja.nativearrays.core.operators.binary.FloatBinaryOperator;
import me.theeninja.nativearrays.core.operators.unary.FloatUnaryOperator;
import me.theeninja.nativearrays.core.predicates.FloatPredicate;

public class UnfilteredFloatArray extends FloatArray<FilteredFloatArray, UnfilteredFloatArray> {
    public UnfilteredFloatArray(String size) {
        super(size);
    }

    @Override
    public native String toString();

    @Override
    public native int get(long index);

    @Override
    public native void set(long index, float value);

    @Override
    public native long count(float value);

    @Override
    public native void fill(float value);

    @Override
    public native int reduce(FloatBinaryOperator reducer);

    @Override
    public native FilteredFloatArray iFilter(FloatPredicate predicate);

    @Override
    public native UnfilteredFloatArray rFilter(FloatPredicate predicate);

    @Override
    public native UnfilteredFloatArray rMap(FloatUnaryOperator mapper);

    @Override
    public native void iMap(FloatUnaryOperator mapper);

    @Override
    public native void sort();

    @Override
    public native void sort(FloatComparator comparator);

    @Override
    public native float[] toJavaArray();

    @Override
    public native void intoJavaArray(float[] javaArray);

    @Override
    public native void forEachValue(FloatConsumer intConsumer);

    @Override
    public native void forEachIndexValuePair(IndexFloatPairConsumer indexValuePairConsumer);

    @Override
    public native void copyInto(UnfilteredFloatArray nativeArray, long offset, long newSize);
}
