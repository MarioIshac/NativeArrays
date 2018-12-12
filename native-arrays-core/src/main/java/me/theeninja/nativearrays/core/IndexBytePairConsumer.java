package me.theeninja.nativearrays.core;

@FunctionalInterface
public interface IndexBytePairConsumer {
    void accept(long index, byte value);
}
