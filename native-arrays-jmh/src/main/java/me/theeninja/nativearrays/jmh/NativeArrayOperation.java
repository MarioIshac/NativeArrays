package me.theeninja.nativearrays.jmh;

import me.theeninja.nativearrays.core.IntArray;

public class NativeArrayOperation extends ArrayOperation<IntArray, Long> {
    @Override
    IntArray newValue() {
        return new IntArray(ARRAY_LENGTH);
    }
}
