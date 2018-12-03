package me.theeninja.nativearrays.core;

public class ArrayStaticInitiailizerParameters {
    private static final int DEFAULT_HASHMAP_SIZE = 16;
    private static int hashMapSize = DEFAULT_HASHMAP_SIZE;

    public static void setHashmapSize(int hashmapSize) {
        ArrayStaticInitiailizerParameters.hashMapSize = hashmapSize;
    }

    public static int getHashMapSize() {
        return ArrayStaticInitiailizerParameters.hashMapSize;
    }
}
