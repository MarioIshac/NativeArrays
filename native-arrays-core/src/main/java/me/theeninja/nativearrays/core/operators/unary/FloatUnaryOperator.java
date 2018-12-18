package me.theeninja.nativearrays.core.operators.unary;

import java.util.Objects;

@FunctionalInterface
public interface FloatUnaryOperator {
    float applyAsFloat(float operand);

    default FloatUnaryOperator compose(FloatUnaryOperator before) {
        Objects.requireNonNull(before);
        return v -> applyAsFloat(before.applyAsFloat(v));
    }

    default FloatUnaryOperator andThen(FloatUnaryOperator after) {
        Objects.requireNonNull(after);
        return t -> after.applyAsFloat(applyAsFloat(t));
    }
}
