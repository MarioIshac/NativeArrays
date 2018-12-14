package me.theeninja.nativearrays.core;

@FunctionalInterface
public interface ArrayGetter<T extends Array<T, ?, ?, ?>, AT extends Number> {
    AT get(T nativeArray, long index)
}
