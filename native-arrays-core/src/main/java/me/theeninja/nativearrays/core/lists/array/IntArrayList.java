/* package me.theeninja.nativearrays.core.lists.array;

import me.theeninja.nativearrays.core.array.filtered.FilteredCollection;
import me.theeninja.nativearrays.core.array.filtered.FilteredIntArray;
import me.theeninja.nativearrays.core.array.unfiltered.UnfilteredIntArray;
import me.theeninja.nativearrays.core.collection.IntCollection;
import me.theeninja.nativearrays.core.array.IntArray;
import me.theeninja.nativearrays.core.consumers.pair.IndexIntPairConsumer;

import java.util.function.IntBinaryOperator;
import java.util.function.IntConsumer;
import java.util.function.IntPredicate;
import java.util.function.IntUnaryOperator;

public abstract class IntArrayList<
    FilteredArrayListType extends IntArrayList<
       FilteredArrayListType,
       FilteredArrayType,
       UnfilteredArrayListType,
       UnfilteredArrayType
    > & FilteredCollection<
       IntArrayList<
           FilteredArrayListType,
           FilteredArrayType,
           UnfilteredArrayListType,
           UnfilteredArrayType
       >,
       FilteredArrayListType,
       UnfilteredArrayListType
    >,
    FilteredArrayType extends IntArray<
       FilteredArrayType,
       UnfilteredArrayType
    > & FilteredCollection<
        IntArray<
            FilteredArrayType,
            UnfilteredArrayType
        >,
        FilteredArrayType,
        UnfilteredArrayType
    >,
    UnfilteredArrayListType extends IntArrayList<
        FilteredArrayListType,
        FilteredArrayType,
        UnfilteredArrayListType,
        UnfilteredArrayType
    >,
    UnfilteredArrayType extends IntArray<
        FilteredArrayType,
        UnfilteredArrayType
    >
> extends NativeArrayList<
    IntArrayList<
        FilteredArrayListType,
        FilteredArrayType,
        UnfilteredArrayListType,
        UnfilteredArrayType
    >,
    IntArray<
       FilteredArrayType,
       UnfilteredArrayType
    >,
    FilteredArrayListType,
    FilteredArrayType,
    UnfilteredArrayListType,
    UnfilteredArrayType,
    IntConsumer,
    IndexIntPairConsumer,
    IntUnaryOperator,
    IntBinaryOperator,
    IntPredicate,
    int[]
> implements IntCollection<
    IntArrayList<
        FilteredArrayListType,
        FilteredArrayType,
        UnfilteredArrayListType,
        UnfilteredArrayType
    >,
    FilteredArrayListType,
    UnfilteredArrayListType
> {

    public IntArrayList(long size, double sizeChangeFactor) {
        super(size, sizeChangeFactor);
    }

    @Override
    public int get(long index) {
        return getUnderlyingArray().get(index);
    }

    @Override
    public void set(long index, int value) {
        getUnderlyingArray().set(index, value);
    }

    @Override
    public long searchForwards(int value) {
        return getUnderlyingArray().searchForwards(value);
    }

    @Override
    public long searchBackwards(int value) {
        return getUnderlyingArray().searchBackwards(value);
    }

    @Override
    public long count(int value) {
        return getUnderlyingArray().count(value);
    }

    @Override
    public boolean contains(int value) {
        return getUnderlyingArray().contains(value);
    }

    @Override
    public void fill(int value) {
        getUnderlyingArray().fill(value);
    }

    @Override
    public int reduce(IntBinaryOperator reducer) {
        return getUnderlyingArray().reduce(reducer);
    }

     {
        final long sizeChange = values.getUsedSize();
        final double newUsedSize = getUsedSize() + sizeChange;

        if (newUsedSize > getSize()) {
            final double sizeChangeFactorRequired = newUsedSize / getSize();

            final double sizeChangeFactor = Math.ceil(Math.log(sizeChangeFactorRequired) / Math.log(2));

            final long newSize = (long) (getSize() * sizeChangeFactor);

            setUnderlyingArray(newSize);
        }

        for ()
    }
} */