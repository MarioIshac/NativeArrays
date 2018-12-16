package me.theeninja.nativearrays.core;

import java.util.function.IntPredicate;

@FunctionalInterface
public interface ShortPredicate {
    boolean test(short value);
}
