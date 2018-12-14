package me.theeninja.nativearrays.core;

@FunctionalInterface
public interface ArraySetter<T extends Array<T, ?, ?, ?>, AT extends Number> {
    void set(T nativeArray, long index, AT autoBoxedValue);
}

