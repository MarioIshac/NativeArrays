package me.theeninja.nativearrays.core.predicates;

@FunctionalInterface
public interface FloatPredicate {
    boolean test(float value);

    default FloatPredicate and(FloatPredicate predicate) {
        return value -> test(value) && predicate.test(value);
    }

    default FloatPredicate or(FloatPredicate predicate) {
        return value -> test(value) || predicate.test(value);
    }

    default FloatPredicate negate() {
        return value -> !test(value);
    }
}
