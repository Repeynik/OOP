package org.example;

public class MyAtomicBoolean {
    private volatile boolean value;

    public synchronized boolean get() {
        return value;
    }

    public synchronized void set(boolean value) {
        this.value = value;
    }

    public MyAtomicBoolean(boolean value) {
        this.value = value;
    }
}
