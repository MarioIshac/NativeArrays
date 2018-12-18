package me.theeninja.nativearrays.core.operators.binary;

import me.theeninja.nativearrays.core.consumers.pair.IndexBytePairConsumer;

@FunctionalInterface
public interface ShortBinaryOperator {
    short applyAsShort(short left, short right);


}
