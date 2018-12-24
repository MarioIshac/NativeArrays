/* package me.theeninja.nativearrays.core.lists.array;

import me.theeninja.nativearrays.core.array.IntArray;
import me.theeninja.nativearrays.core.array.filtered.FilteredIntArray;
import me.theeninja.nativearrays.core.array.unfiltered.UnfilteredCollection;
import me.theeninja.nativearrays.core.array.unfiltered.UnfilteredIntArray;

public class UnfilteredIntArrayList extends IntArrayList<
    FilteredIntArrayList,
    FilteredIntArray,
    UnfilteredIntArrayList,
    UnfilteredIntArray,
    UnfilteredIntArray
> implements UnfilteredCollection<> {
    public UnfilteredIntArrayList(long size, double sizeChangeFactor) {
        super(size, sizeChangeFactor);
    }

    @Override
    UnfilteredIntArray newUnderlyingArray(long newSize) {
        return new UnfilteredIntArray(newSize);
    }
} */
