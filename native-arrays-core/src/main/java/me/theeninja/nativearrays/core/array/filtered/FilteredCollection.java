package me.theeninja.nativearrays.core.array.filtered;

import me.theeninja.nativearrays.core.array.unfiltered.UnfilteredCollection;
import me.theeninja.nativearrays.core.collection.NativeCollection;

public interface FilteredCollection<
    T extends NativeCollection<
        T,
        FT,
        UT,
        ?,
        ?,
        ?,
        ?,
        ?,
        ?
    >,
    FT extends NativeCollection<
        T,
        FT,
        UT,
        ?,
        ?,
        ?,
        ?,
        ?,
        ?
    > & FilteredCollection<
        T,
        FT,
        UT
    >,
    UT extends NativeCollection<
        T,
        FT,
        UT,
        ?,
        ?,
        ?,
        ?,
        ?,
        ?
    > & UnfilteredCollection<
        T,
        FT,
        UT
    >
> {
    UT asUnfiltered();
}
