package me.theeninja.nativearrays.core;

@FunctionalInterface
public interface ByteComparator {
    int compare(byte firstValue, byte secondValue);
}
