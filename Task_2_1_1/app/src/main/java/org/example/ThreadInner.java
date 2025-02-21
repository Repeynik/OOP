package org.example;

public class ThreadInner implements Runnable {

    private MyAtomicBoolean found;
    private int start;
    private int end;
    private int[] array;
    private Thread[] threads;

    public ThreadInner(MyAtomicBoolean found, int start, int end, int[] array, Thread[] threads) {
        this.found = found;
        this.start = start;

        this.end = end;

        this.array = array;
        this.threads = threads;
    }

    private static void runParallel(
            MyAtomicBoolean found, int start, int end, int[] array, Thread[] threads) {
        for (int j = start; j < end; j++) {
            if (found.get()) break;
            if (BasePrime.isPrime(array[j])) {
                found.set(true);
                interruptThreads(threads);
                break;
            }
        }
    }

    private static void interruptThreads(Thread[] threads) {
        for (Thread thread : threads) {
            if (thread != null && thread.isAlive()) {
                thread.interrupt();
            }
        }
    }

    @Override // +
    public void run() { // +
        runParallel(found, start, end, array, threads); // +
    }
}
