package me.theeninja.nativearrays.core;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.function.Consumer;
import java.util.function.Supplier;

public abstract class Array<T extends Array<T, TVC, TIVC, TA>, TVC, TIVC, TA> implements AutoCloseable {
    public static final int NOT_FOUND = -1;

    private final long address;
    private final long size;

    public Array(long size) {
        this.size = size;
        this.address = malloc(size);
    }

    abstract long malloc(long size);

    public abstract void sort();

    public abstract TA toJavaArray();
    public abstract void intoJavaArray(final TA javaArray);

    public abstract void forEachValue(final TVC intConsumer);
    public abstract void forEachIndexValuePair(final TIVC indexValuePairConsumer);

    public abstract T copy();

    public long getSize() {
        return size;
    }

    public static <T extends Array<T, TVC, TIVC, TA>, TVC, TIVC, TA> void tryAndClose(final Supplier<T> arraySupplier, final Consumer<T> arrayConsumer) throws Exception {
        try (final T array = arraySupplier.get()) {
            arrayConsumer.accept(array);
        }
    }

    private static <T extends Array<T, TVC, TIVC, TA>, TVC, TIVC, TA> String getLibraryName(final Class<T> subClass) {
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

    static <T extends Array<T, TVC, TIVC, TA>, TVC, TIVC, TA> Runnable getLibraryLoader(final Class<T> subClass) {
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

    public long getAddress() {
        return address;
    }
}
