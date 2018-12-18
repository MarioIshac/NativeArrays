package me.theeninja.nativearrays.core.predicates;

import java.util.function.IntPredicate;

@FunctionalInterface
public interface ShortPredicate {
    boolean test(short value);

    default ShortPredicate and(ShortPredicate predicate) {
        return value -> test(value) && predicate.test(value);
    }

    default ShortPredicate or(ShortPredicate predicate) {
        return value -> test(value) || predicate.test(value);
    }

    default ShortPredicate negate() {
        return value -> !test(value);
    }
}
