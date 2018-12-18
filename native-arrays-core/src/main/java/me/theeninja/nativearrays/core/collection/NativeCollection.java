package me.theeninja.nativearrays.core.collection;

/**
 *
 *
 * @param <T> Abstract sub type of array
 * @param <TVC> Consumer specialized for associated primitive
 * @param <TIVC> Index-value-pair consumer specialized for associated primitive
 * @param <TVU> Unary operator specialized with associated primitive
 * @param <TVP> Predicate specialized with associated primitive
 * @param <TVS> Comparator/Sorter specialized with associated primitive
 * @param <TA> Java array type specialized with associated primitive
 */
public interface NativeCollection<
    T,
    UT,
    TVC,
    TIVC,
    TVU,
    TVP,
    TVS,
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


    /**
     * Sorts the internal array using an implementation of merge sort. The sorting is stable.
     */
    void sort();
    void sort(TVS comparator);
}
