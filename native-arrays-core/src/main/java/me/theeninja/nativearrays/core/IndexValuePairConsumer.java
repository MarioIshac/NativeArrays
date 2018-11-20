package me.theeninja.nativearrays.core;

@FunctionalInterface
public interface IndexValuePairConsumer {
    void accept(long index, int value);
}
