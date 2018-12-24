package me.theeninja.nativearrays.core.array.unfiltered;

import me.theeninja.nativearrays.core.array.filtered.FilteredCollection;
import me.theeninja.nativearrays.core.collection.NativeCollection;

public interface UnfilteredCollection<
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
> {}
