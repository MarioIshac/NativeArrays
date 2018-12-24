/* package me.theeninja.nativearrays.core.lists.array;

import me.theeninja.nativearrays.core.array.IntArray;
import me.theeninja.nativearrays.core.array.filtered.FilteredCollection;
import me.theeninja.nativearrays.core.array.filtered.FilteredIntArray;
import me.theeninja.nativearrays.core.array.unfiltered.UnfilteredIntArray;

import java.util.function.IntBinaryOperator;

public class FilteredIntArrayList extends IntArrayList<
    FilteredIntArrayList,
    FilteredIntArray,
    UnfilteredIntArrayList,
    UnfilteredIntArray,
    FilteredIntArray
> implements FilteredCollection<
    IntArrayList<
        FilteredIntArrayList,
        FilteredIntArray,
        UnfilteredIntArrayList,
        UnfilteredIntArray,
        FilteredIntArray
    >,
    FilteredIntArrayList,
    UnfilteredIntArrayList
> {
    public FilteredIntArrayList(long size, double sizeChangeFactor) {
        super(size, sizeChangeFactor);
    }

    @Override
    FilteredIntArray newUnderlyingArray(long newSize) {
        return new UnfilteredIntArray(newSize);
    }

    @Override
    public UnfilteredIntArrayList asUnfiltered() {
        return null;
    }
} */
