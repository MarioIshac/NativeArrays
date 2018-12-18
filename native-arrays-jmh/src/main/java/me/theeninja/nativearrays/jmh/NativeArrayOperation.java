package me.theeninja.nativearrays.jmh;

import me.theeninja.nativearrays.core.array.IntArray;
import me.theeninja.nativearrays.core.array.unfiltered.UnfilteredIntArray;

public class NativeArrayOperation extends ArrayOperation<UnfilteredIntArray, Long> {
    @Override
    UnfilteredIntArray newValue() {
        return new UnfilteredIntArray(ARRAY_LENGTH);
    }
}
