package me.theeninja.nativearrays.core.collection;

import me.theeninja.nativearrays.core.array.filtered.FilteredCollection;
import me.theeninja.nativearrays.core.array.unfiltered.UnfilteredCollection;

/**
 *
 *
 * @param <T> Abstract sub type of array
 * @param <TVC> Consumer specialized for associated primitive
 * @param <TIVC> Index-value-pair consumer specialized for associated primitive
 * @param <TVU> Unary operator specialized with associated primitive
 * @param <TVP> Predicate specialized with associated primitive
 * @param <TA> Java array type specialized with associated primitive
 */
public interface NativeCollection<
    T extends NativeCollection<
        T,
        FT,
        UT,
        TVC,
        TIVC,
        TVU,
        TVB,
        TVP,
        TA
    >,
    FT extends NativeCollection<
        T,
        FT,
        UT,
        TVC,
        TIVC,
        TVU,
        TVB,
        TVP,
        TA
    > & FilteredCollection<
        T,
        FT,
        UT
    >,
    UT extends NativeCollection<
        T,
        FT,
        UT,
        TVC,
        TIVC,
        TVU,
        TVB,
        TVP,
        TA
    > & UnfilteredCollection<
        T,
        FT,
        UT
    >,
    TVC,
    TIVC,
    TVU,
    TVB,
    TVP,
    TA
> {
    UT rFilter(TVP predicate);

    /**
     *
     * @param mapper
     * @return
     */
    UT rMap(TVU mapper);

    /**
     *
     * @param mapper
     */
    void iMap(TVU mapper);

    /**
     * @return A java array (meaning {@code TYPE[]}) that has the elements of this array copied over. The length of the java array is equivalent to
     * the length of this native array.
     */
    TA toJavaArray();

    /**
     * Copies the elements of this native array into the java array. If {@code javaArray.length} is less then {@code getSize()}
     * then only the first {@code javaArray.length} elements are copied.
     *
     * @param javaArray The java array to copy elements into.
     */
    void intoJavaArray(final TA javaArray);

    /**
     * Performs a void action, receiving one element, for each value in this array. These elements are indivudally passed into {@code intConsumer}.
     *
     * @param intConsumer The consumer of each value in this array.
     */
    void forEachValue(final TVC intConsumer);

    /**
     * Performs a void action, receiving an index and its associated element, for each value in this array. These index-element pairs are indivudally passed into
     * {@code indexValuePairConsumer}
     *
     * @param indexValuePairConsumer The consumer for each index-value pair in this array.
     */
    void forEachIndexValuePair(final TIVC indexValuePairConsumer);

    /**
     * Constructs a new {@code IntArray} of length {@code getSize()}, then copies every element of this array to the newly constructed one.
     *
     * @return
     */
    UT copy(long newSize);

    void copyInto(UT nativeArray, long offset, long newSize);

    /**
     * @return the numbers of elements this array can hold
     */
    long getSize();

    void sort(TVB comparator, long startIndex, long endIndex);

    default void sort(TVB comparator) {
        sort(comparator, 0, getSize() - 1);
    }
}
