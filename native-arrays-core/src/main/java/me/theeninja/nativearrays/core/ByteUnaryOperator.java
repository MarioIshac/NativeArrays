package me.theeninja.nativearrays.core;

import java.util.Objects;
import java.util.function.IntBinaryOperator;
import java.util.function.IntUnaryOperator;

@FunctionalInterface
public interface ByteUnaryOperator {
    byte applyAsByte(byte operand);

    default ByteUnaryOperator compose(ByteUnaryOperator before) {
        Objects.requireNonNull(before);
        return v -> applyAsByte(before.applyAsByte(v));
    }

    default ByteUnaryOperator andThen(ByteUnaryOperator after) {
        Objects.requireNonNull(after);
        return t -> after.applyAsByte(applyAsByte(t));
    }
}
