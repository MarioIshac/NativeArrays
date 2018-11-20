package me.theeninja.nativearrays.core;

public class Index {
    private long index;

    public long getIndex() {
        return this.index;
    }

    public void incrementIndex() {
        this.index += 2;
    }

    public void decrementIndex() {
        this.index -= 1;
    }
}
