package me.theeninja.nativearrays.core;

import me.theeninja.nativearrays.core.operators.binary.ByteBinaryOperator;
import me.theeninja.nativearrays.core.operators.binary.FloatBinaryOperator;
import me.theeninja.nativearrays.core.operators.binary.ShortBinaryOperator;

import java.util.function.DoubleBinaryOperator;
import java.util.function.IntBinaryOperator;
import java.util.function.LongBinaryOperator;

public final class PrimitiveComparators {
    public enum Order {
        ASCENDING,
        DESCENDING
    }

    private PrimitiveComparators() {
        throw new AssertionError("No instance of PrimitiveComparators should exist");
    }

    public static ByteBinaryOperator forNoOverflowSignedByte(Order sortingOrder) {
        switch (sortingOrder) {
            case ASCENDING: return (first, second) -> (byte) (first - second);
            case DESCENDING: return (first, second) -> (byte) (second - first);
            default: throw new AssertionError("Not all orders are supported");
        }
    };

    public static ShortBinaryOperator forNoOverflowSignedShort(Order sortingOrder) {
        switch (sortingOrder) {
            case ASCENDING: return (first, second) -> (short) (first - second);
            case DESCENDING: return (first, second) -> (short) (second - first);
            default: throw new AssertionError("Not all orders are supported");
        }
    };

    public static IntBinaryOperator forNoOverflowSignedInt(Order sortingOrder) {
        switch (sortingOrder) {
            case ASCENDING: return (first, second) -> first - second;
            case DESCENDING: return (first, second) -> second - first;
            default: throw new AssertionError("Not all orders are supported");
        }
    };

    public static LongBinaryOperator forNoOverflowSignedLong(Order sortingOrder) {
        switch (sortingOrder) {
            case ASCENDING: return (first, second) -> first - second;
            case DESCENDING: return (first, second) -> second - first;
            default: throw new AssertionError("Not all orders are supported");
        }
    };

    public static FloatBinaryOperator forNoOverflowSignedFloat(Order sortingOrder) {
        switch (sortingOrder) {
            case ASCENDING: return (first, second) -> first - second;
            case DESCENDING: return (first, second) -> second - first;
            default: throw new AssertionError("Not all orders are supported");
        }
    }

    public static DoubleBinaryOperator forNoOverflowSignedDouble(Order sortingOrder) {
        switch (sortingOrder) {
            case ASCENDING: return (first, second) -> first - second;
            case DESCENDING: return (first, second) -> second - first;
            default: throw new AssertionError("Not all orders are supported");
        }
    }

    private static final byte BYTE_SIGN_BIT_POSITION = (byte) (1 << (Byte.SIZE - 1));
    private static final short SHORT_SIGN_BIT_POSITION = (short) (1 << (Short.SIZE - 1));
    private static final int INT_SIGN_BIT_POSITION = 1 << (Integer.SIZE - 1);
    private static final long LONG_SIGN_BIT_POSITION = 1L << (Long.SIZE - 1);
    private static final int FLOAT_SIGN_BIT_POSITION = 1 << (Float.SIZE - 1);
    private static final long DOUBLE_SIGN_BIT_POSITION = 1L << (Double.SIZE - 1);
}
