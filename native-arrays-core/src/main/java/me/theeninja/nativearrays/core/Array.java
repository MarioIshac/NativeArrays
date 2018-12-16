package me.theeninja.nativearrays.core;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * Superclass of all primitive array specializations. Due to the fact that there is no common interface of the primitive types without autoboxing/unboxing penalties, no abstraction is provided
 * for accessing/setting indivudal values through get/set methods. These methods should be implemented in {@code T}.
 * <br>
 *
 * Due to being natively implemented, each instance array holds a lock on certain system resources. In order to free this lock, {@code close} must be called of {@code AutoCloseable}.
 *
 * @implNote This array has two attributes, an address and a size. The address, internally, is used as a pointer to which a certain offset, up to {@code getSize()} is added in order
 *  to determine the value of an element at a certain index.
 *
 * @param <T> The subclass extending {@code Array}
 * @param <TVC> The primitive-specialization for the value consumer.
 * @param <TIVC> The primitive specialization fot the index-value-pair consumer.
 * @param <TA> The correlated java array object (e.g {@code int[]} for {@code IntArray} and {@code float[]} for {@code FloatArray}
 */
public abstract class Array<T extends Array<T, TVC, TIVC, TVU, TVB, TVP, TVS, TA>, TVC, TIVC, TVU, TVB, TVP, TVS, TA> implements AutoCloseable {
    /**
     * The "index" which is returned in the case that a requested-for value is not found in a search.
     */
    public static final int NOT_FOUND = -1;

    /* private fields are not inherited by subclasses, but inheritance of these fields is required
    in JNI code */
    /**
     * The address of the first element in this array.
     */
    protected final long address;

    /**
     * The number of elements this array can hold.
     */
    protected final long size;

    /**
     * Constructs an array with a signed size.
     *
     * @param size The number of elements (signed) that the array can hold.
     */
    protected Array(long size) {
        this.size = size;
        this.address = malloc();
    }

    public abstract T map(TVU mapper);
    public abstract void mapLocally(TVU mapper);

    @SafeVarargs
    public final void mapLocally(TVU... mappers) {
        for (TVU mapper : mappers) {
            mapLocally(mapper);
        }
    }

    public abstract T filter(TVP predicate);
    public abstract int filterLocally(TVP predicate);

    /**
     * Constructs an array with an unsigned size.
     *
     * @param size The number of elements (unsigned, represented as a string) that the array can hold.
     */
    protected Array(String size) {
        this(Long.parseUnsignedLong(size));
    }

    abstract long malloc();

    /**
     * Sorts the internal array using an implementation of merge sort. The sorting is stable.
     */
    public abstract void sort();
    public abstract void sort(TVS comparator);

    /**
     * @return A java array (meaning {@code TYPE[]}) that has the elements of this array copied over. The length of the java array is equivalent to
     * the length of this native array.
     */
    public abstract TA toJavaArray();

    /**
     * Copies the elements of this native array into the java array. If {@code javaArray.length} is less then {@code getSize()}
     * then only the first {@code javaArray.length} elements are copied.
     *
     * @param javaArray The java array to copy elements into.
     */
    public abstract void intoJavaArray(final TA javaArray);

    /**
     * Performs a void action, receiving one element, for each value in this array. These elements are indivudally passed into {@code intConsumer}.
     *
     * @param intConsumer The consumer of each value in this array.
     */
    public abstract void forEachValue(final TVC intConsumer);

    /**
     * Performs a void action, receiving an index and its associated element, for each value in this array. These index-element pairs are indivudally passed into
     * {@code indexValuePairConsumer}
     *
     * @param indexValuePairConsumer The consumer for each index-value pair in this array.
     */
    public abstract void forEachIndexValuePair(final TIVC indexValuePairConsumer);

    /**
     * Constructs a new {@code IntArray} of length {@code getSize()}, then copies every element of this array to the newly constructed one.
     *
     * @return
     */
    public abstract T copy();

    /**
     * @return the numbers of elements this array can hold
     */
    public long getSize() {
        return size;
    }

    /**
     * Performs an action on an array without leaking resources. The array is created, consumed and released (meaning the resources it interally uses are freed)
     * in this method.
     *
     * @param arraySupplier The supplier of the array. This will be called once, at the beginning of the method before consumption.
     * @param arrayConsumer The consumer of the array. This will be called once, post-creation of the array.
     * @param <T> The array primitive specialization that is being created and consumed.
     * @throws Exception a general exception if thrown when creation of the object fails (indicating an exception occurred internally in the supplier)
     */
    public static <T extends Array> void tryAndClose(final Supplier<T> arraySupplier, final Consumer<T> arrayConsumer) throws Exception {
        try (final T array = arraySupplier.get()) {
            arrayConsumer.accept(array);
        }
    }

    /**
     * @param subClass A subclass of {@code Array}
     * @return the library name (name of the .dll file) that {@code subClass} is associated with.
     */
    private static String getLibraryName(final Class<? extends Array> subClass) {
        return subClass.getSimpleName();
    }

    private static final String NATIVE_LIB_DIRECTORY;
    private static final String NATIVE_LIB_SUFFIX;

    static {
        final String operatingSystem = System.getProperty("os.name").toLowerCase();
        System.out.println(operatingSystem);

        if (operatingSystem.startsWith("win")) {
            NATIVE_LIB_DIRECTORY = "windows";
            NATIVE_LIB_SUFFIX = "dll";
        }
        else if (operatingSystem.startsWith("linux")) {
            NATIVE_LIB_DIRECTORY = "linux";
            NATIVE_LIB_SUFFIX = "so";
        }
        else {
            throw new IllegalStateException("Operating system not supported.");
        }
    }

    /**
     * @param subClass The subclass of {@code Array}
     * @return a loader of the library associated with {@code subClass}. Actual loading occurs when calling {@code run} on the loader.
     */
    static Runnable getLibraryLoader(final Class<? extends Array> subClass) {
        try {
            final String libraryName = getLibraryName(subClass);
            final String relativeLibraryPath = "/" + NATIVE_LIB_DIRECTORY + "/" + libraryName + "." + NATIVE_LIB_SUFFIX;

            final InputStream libraryStream = Array.class.getResourceAsStream(relativeLibraryPath);

            if (libraryStream == null) {
                throw new IllegalStateException("Library path not accessible.");
            }

            final Path tempLibraryDirectory = Files.createTempDirectory("NativeArray-");
            final Path tempLibraryPath = tempLibraryDirectory.resolve(libraryName + "." + NATIVE_LIB_SUFFIX);

            Files.copy(libraryStream, tempLibraryPath);

            final String tempLibraryPathString = tempLibraryPath.toString();
            System.out.println(tempLibraryPathString);

            return () -> System.load(tempLibraryPathString);
        }
        catch (final IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    /**
     * @return the address of the first element in this array
     */
    private long getAddress() {
        return address;
    }
}
