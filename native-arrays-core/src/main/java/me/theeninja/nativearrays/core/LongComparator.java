package me.theeninja.nativearrays.core;

@FunctionalInterface
public interface LongComparator {
    int compare(long firstValue, long secondValue);
}