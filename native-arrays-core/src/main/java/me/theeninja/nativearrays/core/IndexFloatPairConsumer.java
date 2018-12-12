package me.theeninja.nativearrays.core;

@FunctionalInterface
public interface IndexFloatPairConsumer{
    void accept(long index, float value);

    default IndexFloatPairConsumer of(IndexDoublePairConsumer $) {
        return $::accept;
    }
}
