package org.example;

import java.util.concurrent.atomic.AtomicBoolean;

public class Parallel {
    private static boolean isPrime(int num) {
        if (num < 2) return false;
        for (int j = 2; j <= Math.sqrt(num); j++) {
            if (num % j == 0) return true;
        }
        return false;
    }

    public static boolean primeNumber(int[] array, int numThreads) throws InterruptedException {
        AtomicBoolean found = new AtomicBoolean(false);
        Thread[] threads = new Thread[numThreads];
        int lengthPerThread = array.length / numThreads;

        for (int i = 0; i < numThreads; i++) {
            final int start = i * lengthPerThread;
            final int end = (i == numThreads - 1) ? array.length : start + lengthPerThread;
            threads[i] =
                    new Thread(
                            () -> {
                                for (int j = start; j < end; j++) {
                                    if (found.get()) break;
                                    if (isPrime(array[j])) {
                                        found.set(true);
                                        interruptThreads(threads);
                                        break;
                                    }
                                }
                            });
            threads[i].start();
        }

        for (Thread thread : threads) {
            thread.join();
        }

        return found.get();
    }

    private static void interruptThreads(Thread[] threads) {
        for (Thread thread : threads) {
            if (thread != null && thread.isAlive()) {
                thread.interrupt();
            }
        }
    }
}
