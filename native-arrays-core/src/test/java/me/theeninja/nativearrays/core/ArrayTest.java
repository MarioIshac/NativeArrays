package me.theeninja.nativearrays.core;

import org.junit.jupiter.api.Test;

import java.util.function.BiFunction;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertFalse;

abstract class ArrayTest<T extends Array<T, TVC, TIVC, TA>, TVC, TIVC, TA, AT extends Number> {
    abstract T getNativeArray();

    abstract ArrayGetter<T, AT> getNativeArrayGetter();
    abstract ArraySetter<T, AT> getNativeArraySetter();

    abstract AT getTestValue();
    abstract long getTestIndex();

    private static final int INT_ARRAY_SIZE = 3;

    @Test
    void testGetAndSet() {
        final AT observedValue = getNativeArrayGetter().get(getNativeArray(), getTestIndex());

        assertEquals(observedValue, 2);
    }

    @Test
    void testSearchForwards() {
        final long observedIndex = getIntArray().searchForwards(1);

        assertEquals(0, observedIndex);
    }

    @Test
    void testSearchBackwards() {
        final Object observedIndex = getIntArray().searchBackwards(1);

        assertEquals(2, observedIndex);
    }

    @Test
    void testCount() {
        final long count = getIntArray().count(1);

        assertEquals(2, count);
    }

    @Test
    void testContains() {
        final boolean contains1 = getIntArray().contains(1);

        assertTrue(contains1);

        final boolean contains2 = getIntArray().contains(2);

        assertFalse(contains2);
    }

    private static boolean areEqual(IntArray first, IntArray second) {
        if (first.getSize() != second.getSize()) {
            return false;
        }

        for (int index = 0; index < first.getSize(); index++) {
            final int firstValue = first.get(index);
            final int secondValue = second.get(index);

            if (firstValue != secondValue) {
                return false;
            }
        }

        return true;
    }

    private final IntArray unsortedIntArray = IntArray.fromJavaArray(3, 2, 1);
    private final IntArray sortedIntArray = IntArray.fromJavaArray(1, 2, 3);

    @Test
    void testSort() {
        getUnsortedIntArray().sort();

        final boolean areEqual = areEqual(getUnsortedIntArray(), getSortedIntArray());

        assertTrue(areEqual);
    }

    private static final int FILL_VALUE = 3;

    @Test
    void testFill() {
        IntArray intArray = IntArray.fromJavaArray(1, 2, 3);

        intArray.fill(FILL_VALUE);

        for (int index = 0; index < intArray.getSize(); index++) {
            final int observedValue = intArray.get(index);

            assertEquals(FILL_VALUE, observedValue);
        }
    }

    @Test
    void testFromJavaArray() {
        final IntArray intArray = IntArray.fromJavaArray(1, 0, 1);

        for (int index = 0; index < INT_ARRAY_SIZE; index++) {
            final int expectedValue = getIntArray().get(index);
            final int observedValue = intArray.get(index);

            assertEquals(expectedValue, observedValue);
        }
    }

    private static void assertIntJavaArraysEquals(IntArray intArray, int[] javaArray) {
        for (int index = 0; index < INT_ARRAY_SIZE; index++) {
            final int expectedValue = intArray.get(index);
            final int observedValue = javaArray[index];

            assertEquals(expectedValue, observedValue);
        }
    }

    @Test
    void testToJavaArray() {
        final int[] javaArray = getIntArray().toJavaArray();

        assertEquals(INT_ARRAY_SIZE, javaArray.length);
        assertIntJavaArraysEquals(getIntArray(), javaArray);
    }

    @Test
    void testIntoJavaArray() {
        final int[] javaArray = new int[INT_ARRAY_SIZE];

        getIntArray().intoJavaArray(javaArray);

        assertIntJavaArraysEquals(getIntArray(), javaArray);
    }

    @Test
    void testForEachValue() {
        int[] mutableIndex = new int[1];

        getIntArray().forEachValue(observedValue -> {
            final int index = mutableIndex[0]++;

            final int expectedValue = getIntArray().get(index);

            assertEquals(expectedValue, observedValue);
        });
    }

    @Test
    void testForEachIndexValuePair() {
        int[] mutableIndex = new int[1];

        getIntArray().forEachIndexValuePair((observedIndex, observedValue) -> {
            final int expectedIndex = mutableIndex[0]++;

            assertEquals(expectedIndex, observedIndex);

            final int expectedValue = getIntArray().get(observedIndex);

            assertEquals(expectedValue, observedValue);
        });
    }

    @Test
    void testCopy() {
        IntArray copiedIntArray = getIntArray().copy();

        for (int index = 0; index < INT_ARRAY_SIZE; index++) {
            final int expectedValue = getIntArray().get(index);
            final int observedValue = copiedIntArray.get(index);

            assertEquals(expectedValue, observedValue);
        }
    }

    @Test
    void testSize() {
        assertEquals(getIntArray().getSize(), INT_ARRAY_SIZE);
    }

    private final IntArray firstEqualArray = IntArray.fromJavaArray(4, 5, 6);
    private final IntArray secondEqualArray = IntArray.fromJavaArray(4, 5, 6);
    private final IntArray unEqualArray = IntArray.fromJavaArray(4, 5, 7);

    @Test
    void testEquals() {
        final boolean equalArraysEqual = areEqual(getFirstEqualArray(), getSecondEqualArray());

        assertTrue(equalArraysEqual);

        final boolean unequalArraysEqual = areEqual(getFirstEqualArray(), getUnEqualArray());

        assertFalse(unequalArraysEqual);
    }

    private IntArray getIntArray() {
        return intArray;
    }

    private IntArray getUnsortedIntArray() {
        return unsortedIntArray;
    }

    private IntArray getSortedIntArray() {
        return sortedIntArray;
    }

    private IntArray getFirstEqualArray() {
        return firstEqualArray;
    }

    private IntArray getSecondEqualArray() {
        return secondEqualArray;
    }

    private IntArray getUnEqualArray() {
        return unEqualArray;
    }
}
