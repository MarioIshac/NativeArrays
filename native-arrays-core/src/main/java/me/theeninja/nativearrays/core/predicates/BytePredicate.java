package me.theeninja.nativearrays.core.predicates;

import java.util.function.IntPredicate;
import java.util.function.Predicate;

@FunctionalInterface
public interface BytePredicate {
    boolean test(byte value);

    default BytePredicate and(BytePredicate predicate) {
        return value -> test(value) && predicate.test(value);
    }

    default BytePredicate or(BytePredicate predicate) {
        return value -> test(value) || predicate.test(value);
    }

    default BytePredicate negate() {
        return value -> !test(value);
    }
}
