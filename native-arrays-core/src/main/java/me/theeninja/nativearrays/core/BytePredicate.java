package me.theeninja.nativearrays.core;

import java.util.function.IntPredicate;

@FunctionalInterface
public interface BytePredicate {
    boolean test(byte value);
}
