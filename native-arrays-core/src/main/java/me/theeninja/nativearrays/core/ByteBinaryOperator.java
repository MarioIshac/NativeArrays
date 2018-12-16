package me.theeninja.nativearrays.core;

import java.util.function.IntBinaryOperator;

@FunctionalInterface
public interface ByteBinaryOperator {
    byte applyAsByte(byte left, byte right);
}
