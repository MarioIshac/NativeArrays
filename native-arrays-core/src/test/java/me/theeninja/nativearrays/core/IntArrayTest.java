package me.theeninja.nativearrays.core;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class IntArrayTest {
    private static final int TEST_GET_INT_ARRAY_LAST_INDEX = 2;

    private static IntArray createValueArray() {
        final IntArray intArray = new IntArray(3);

        intArray.set(0, 1);
        intArray.set(1, 2);
        intArray.set(2, 3);

        return intArray;
    }

    @Test
    void testGetAndSet() {
        final IntArray intArray = createValueArray();

        final int observedValue = intArray.get(1);

        assertEquals(observedValue, 2);
    }

    private static IntArray createSearchArray() {
        final IntArray intArray = createValueArray();

        intArray.set(0, 1);
        intArray.set(1, 2);
        intArray.set(0, 3);

        return intArray;
    }

    @Test
    void testSearchForwards() {
        final IntArray intArray = createSearchArray();

        final long observedIndex = intArray.searchForwards(1);

        assertEquals(0, observedIndex);
    }

    @Test
    void testSearchBackwards() {
        final IntArray intArray = createSearchArray();

        final long observedIndex = intArray.searchBackwards(1);

        assertEquals(2, observedIndex);
    }

    @Test
    void testCount() {
    }

    @Test
    void testContains() {
    }

    @Test
    void testSort() {
    }

    @Test
    void testFill() {
    }

    @Test
    void testFromJavaArray() {
    }

    @Test
    void testToJavaArray() {
    }

    @Test
    void testIntoJavaArray() {
    }

    @Test
    void testForEachValue() {
    }

    @Test
    void testForEachIndexValuePair() {
    }

    @Test
    void testCopy() {
    }

    @Test
    void testCreate() {
    }

    @Test
    void testSize() {
    }

    @Test
    void testEquals() {
    }

    @Test
    void testHashCode() {
    }

    @Test
    void testToString() {
    }

    @Test
    void testClose() {
    }

    @Test
    void testTryAndClose() {
    }

    @Test
    void testUnload() {
    }
}