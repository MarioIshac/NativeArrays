package me.theeninja.nativearrays.core;

@FunctionalInterface
public interface IndexLongPairConsumer {
    void accept(long index, long value);
}
