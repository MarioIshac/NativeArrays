package me.theeninja.nativearrays.core.lists.array;

import me.theeninja.nativearrays.core.array.Array;
import me.theeninja.nativearrays.core.collection.NativeCollection;
import me.theeninja.nativearrays.core.consumers.pair.IndexBytePairConsumer;
import me.theeninja.nativearrays.core.consumers.pair.IndexIntPairConsumer;
import me.theeninja.nativearrays.core.consumers.pair.IndexLongPairConsumer;
import me.theeninja.nativearrays.core.consumers.pair.IndexShortPairConsumer;

public abstract class NativeArrayList<
    T extends NativeArrayList<
        T,
        TNA,
        TJA
    >,
    TNA extends Array<TNA, ?, ?, ?, ?, ?, ?, ?, ?, TJA>,
    TJA
> {
    static double DEFAULT_SIZE_CHANGE_FACTOR = 2;

    public abstract void addAll(TNA values);
    public abstract void addAll(TJA values);
}
