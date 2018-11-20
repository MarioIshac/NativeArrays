package me.theeninja.nativearrays.core;

public class IntArrayParameters {
    private static final int DEFAULT_HASHMAP_SIZE = 1;
    private static int hashMapSize = DEFAULT_HASHMAP_SIZE;

    public static void setHashmapSize(int hashmapSize) {
        IntArrayParameters.hashMapSize = hashmapSize;
    }

    public static int getHashMapSize() {
        return IntArrayParameters.hashMapSize;
    }
}
