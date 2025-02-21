package org.example;

public class Parallel {

    public static boolean primeNumber(int[] array, int numThreads) throws InterruptedException {
        MyAtomicBoolean found = new MyAtomicBoolean(false);
        Thread[] threads = new Thread[numThreads];
        int lengthPerThread = array.length / numThreads;

        for (int i = 0; i < numThreads; i++) {
            final int start = i * lengthPerThread;
            final int end = (i == numThreads - 1) ? array.length : start + lengthPerThread;
            threads[i] = new Thread(new ThreadInner(found, start, end, array, threads));
            threads[i].start();
        }

        for (Thread thread : threads) {
            thread.join();
        }

        return found.get();
    }
}
