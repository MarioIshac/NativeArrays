package me.theeninja.nativearrays.core.comparator;

@FunctionalInterface
public interface ByteComparator {
    int compare(byte firstValue, byte secondValue);
}
