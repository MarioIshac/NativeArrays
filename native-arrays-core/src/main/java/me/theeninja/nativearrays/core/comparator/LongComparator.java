package me.theeninja.nativearrays.core.comparator;

@FunctionalInterface
public interface LongComparator {
    int compare(long firstValue, long secondValue);
}