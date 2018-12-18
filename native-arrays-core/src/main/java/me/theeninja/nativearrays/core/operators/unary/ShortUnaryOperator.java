package me.theeninja.nativearrays.core.operators.unary;

import java.util.Objects;

public interface ShortUnaryOperator {
    short applyAsShort(short operand);
    
    default ShortUnaryOperator compose(ShortUnaryOperator before) {
        Objects.requireNonNull(before);
        return v -> applyAsShort(before.applyAsShort(v));
    }

    default ShortUnaryOperator andThen(ShortUnaryOperator after) {
        Objects.requireNonNull(after);
        return t -> after.applyAsShort(applyAsShort(t));
    }
}
