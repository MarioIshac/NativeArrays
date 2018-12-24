/* package me.theeninja.nativearrays.core.lists.array;

import me.theeninja.nativearrays.core.array.Array;
import me.theeninja.nativearrays.core.array.IntArray;
import me.theeninja.nativearrays.core.array.filtered.FilteredCollection;
import me.theeninja.nativearrays.core.collection.NativeCollection;
import me.theeninja.nativearrays.core.consumers.pair.IndexIntPairConsumer;

import java.util.function.*;

public abstract class NativeArrayList<
    ArrayListType extends NativeArrayList<
        ArrayListType,
        ArrayType,
        FilteredArrayListType,
        FilteredArrayType,
        UnfilteredArrayListType,
        ?,
        TVC,
        TIVC,
        TVU,
        TVB,
        TVP,
        TA
    >,
    ArrayType extends Array<
        ArrayType,
        FilteredArrayType,
        UnfilteredArrayType,
        TVC,
        TIVC,
        TVU,
        TVB,
        TVP,
        TA
    >,
    FilteredArrayListType extends NativeArrayList<
        ArrayListType,
        ArrayType,
        FilteredArrayListType,
        FilteredArrayType,
        UnfilteredArrayListType,
        UnfilteredArrayType,
        TVC,
        TIVC,
        TVU,
        TVB,
        TVP,
        TA
    > & FilteredCollection<
        ArrayListType,
        FilteredArrayListType,
        UnfilteredArrayListType
    >,
    FilteredArrayType extends Array<
        ArrayType,
        FilteredArrayType,
        UnfilteredArrayType,
        TVC,
        TIVC,
        TVU,
        TVB,
        TVP,
        TA
    > & FilteredCollection<
        ArrayType,
        FilteredArrayType,
        UnfilteredArrayType
    >,
    UnfilteredArrayListType extends NativeArrayList<
       ArrayListType,
       ArrayType,
       FilteredArrayListType,
       FilteredArrayType,
       UnfilteredArrayListType,
       UnfilteredArrayType,
       TVC,
       TIVC,
       TVU,
       TVB,
       TVP,
       TA
    >,
    UnfilteredArrayType extends Array<
        ArrayType,
        FilteredArrayType,
        UnfilteredArrayType,
        TVC,
        TIVC,
        TVU,
        TVB,
        TVP,
        TA
    >,
    TVC,
    TIVC,
    TVU,
    TVB,
    TVP,
    TA
> implements NativeCollection<
    ArrayListType,
    FilteredArrayListType,
    UnfilteredArrayListType,
    TVC,
    TIVC,
    TVU,
    TVB,
    TVP,
    TA
> {
    static double DEFAULT_SIZE_CHANGE_FACTOR = 2;

    private UnderlyingArrayType underlyingArray;

    public NativeArrayList(long size, double sizeChangeFactor) {
        setUnderlyingArray(size);

        this.sizeChangeFactor = sizeChangeFactor;
    }

    private final double sizeChangeFactor;

    private long usedSize;

    public final native void addAll(ArrayListType values);
    public final native void addAll(ArrayType values);
    public final native void addAll(TA values);

    abstract UnderlyingArrayType newUnderlyingArray(long size);

    final long getUsedSize() {
        return usedSize;
    }

    final void incrementUsedSize(long changeInSize) {
        this.usedSize += changeInSize;
    }

    final void decrementUsedSize(long changeInSize) {
        this.usedSize -= changeInSize;
    }

    public final double getSizeChangeFactor() {
        return sizeChangeFactor;
    }

    public void copyInto(UnfilteredArrayType values) {
        getUnderlyingArray().copyInto(values);
    }

    @Override
    public UnfilteredArrayListType rFilter(TVP predicate) {
        UnfilteredArrayType result = getUnderlyingArray().rFilter(predicate);

        return new UnfilteredArrayListType(result);
    }

    @Override
    public UnfilteredArrayListType rMap(TVU mapper) {
        return null;
    }

    @Override
    public void iMap(TVU mapper) {

    }

    @Override
    public TA toJavaArray() {
        return getUnderlyingArray().toJavaArray();
    }

    @Override
    public void intoJavaArray(TA javaArray) {
        getUnderlyingArray().intoJavaArray(javaArray);
    }

    @Override
    public void forEachValue(TVC consumer) {
        getUnderlyingArray().forEachValue(consumer);
    }

    @Override
    public void forEachIndexValuePair(TIVC indexValuePairConsumer) {
        getUnderlyingArray().forEachIndexValuePair(indexValuePairConsumer);
    }

    @Override
    public UnfilteredArrayListType copy(long newSize) {
        final UnfilteredArrayType copiedIntArray = getUnderlyingArray().copy(newSize);
    }

    @Override
    public void copyInto(UnfilteredArrayListType nativeArray, long offset, long newSize) {
        final UnfilteredArrayType otherUnderlyingArray = nativeArray.getUnderlyingArray();

        getUnderlyingArray().copyInto(, offset, newSize);
    }

    @Override
    public long getSize() {
        return getUnderlyingArray().getSize();
    }

    @Override
    public void sort(TVB comparator, long startIndex, long endIndex) {
        getUnderlyingArray().sort(comparator, startIndex, endIndex);
    }
} */
