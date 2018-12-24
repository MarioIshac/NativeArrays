package me.theeninja.nativearrays.core.array;

import me.theeninja.nativearrays.core.array.filtered.FilteredCollection;
import me.theeninja.nativearrays.core.array.unfiltered.UnfilteredCollection;
import me.theeninja.nativearrays.core.collection.NativeCollection;

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
public abstract class Array<
    T extends Array<
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
    FT extends Array<
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
    UT extends Array<
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
> implements AutoCloseable, NativeCollection<
    T,
    FT,
    UT,
    TVC,
    TIVC,
    TVU,
    TVB,
    TVP,
    TA
> {
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

    public abstract FT iFilter(TVP predicate);

    /**
     * Constructs an array with a signed size.
     *
     * @param size The number of elements (signed) that the array can hold.
     */
    public Array(long size) {
        this.size = size;
        this.address = malloc();
    }

    public Array(long size, long address) {
        this.size = size;
        this.address = address;
    }

    @SafeVarargs
    public final void iMap(TVU... mappers) {
        for (TVU mapper : mappers) {
            iMap(mapper);
        }
    }

    @Override
    public UT copy(long newSize) {
        return null;
    }

    public UT copy() {
        return copy(getSize());
    }

    public void copyInto(UT array) {
        copyInto(array, 0, getSize());
    }

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
        return this.address;
    }

    public long getSize() {
        return this.size;
    }

    @Override
    public abstract String toString();
}
