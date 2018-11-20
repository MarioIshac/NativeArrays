package me.theeninja.nativearrays.jmh;

import org.openjdk.jmh.annotations.*;

import java.util.function.Supplier;

@State(Scope.Benchmark)
public abstract class ArrayOperation<T, R> {
    public static final int ARRAY_LENGTH = Integer.MAX_VALUE / 10;

    private R result;

    abstract T newValue();

    public R getResult() {
        return result;
    }

    public void setResult(R result) {
        this.result = result;
    }
}
