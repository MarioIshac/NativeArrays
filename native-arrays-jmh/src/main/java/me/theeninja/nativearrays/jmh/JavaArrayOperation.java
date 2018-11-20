package me.theeninja.nativearrays.jmh;

public class JavaArrayOperation extends ArrayOperation<int[], Long> {
    @Override
    int[] newValue() {
        return new int[ARRAY_LENGTH];
    }
}
