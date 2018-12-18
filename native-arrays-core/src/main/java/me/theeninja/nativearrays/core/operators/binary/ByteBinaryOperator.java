package me.theeninja.nativearrays.core.operators.binary;

import java.util.function.BinaryOperator;
import java.util.function.IntBinaryOperator;

@FunctionalInterface
public interface ByteBinaryOperator {
    byte applyAsByte(byte left, byte right);
}
