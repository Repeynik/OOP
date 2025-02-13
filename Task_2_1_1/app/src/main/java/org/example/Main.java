package org.example;

public class Main {
    public static void main(String[] args) {
        boolean hasNonPrime;

        int[] primes = NumbersGen.genPrimeNumbes(100000);

        long startTime = System.currentTimeMillis();
        try {
            hasNonPrime = Parallel.primeNumber(primes, 8);
            System.out.println("Contains non-prime: " + hasNonPrime);
        } catch (InterruptedException e) {
            System.err.println("Thread was interrupted: " + e.getMessage());
        }
        long endTime = System.currentTimeMillis();

        long timeElapsed1 = endTime - startTime;

        System.out.println("Parralels Time taken: " + timeElapsed1 + " milliseconds");

        startTime = System.currentTimeMillis();
        try {
            hasNonPrime = Parallel.primeNumber(primes, 4);
            System.out.println("Contains non-prime: " + hasNonPrime);
        } catch (InterruptedException e) {
            System.err.println("Thread was interrupted: " + e.getMessage());
        }
        endTime = System.currentTimeMillis();

        long timeElapsed2 = endTime - startTime;

        System.out.println("Parralels Time taken: " + timeElapsed2 + " milliseconds");

        startTime = System.currentTimeMillis();

        hasNonPrime = Streams.primeNumber(primes);

        endTime = System.currentTimeMillis();

        long timeElapsed3 = endTime - startTime;
        System.out.println("Contains non-prime: " + hasNonPrime);

        System.out.println("Streams Time taken: " + timeElapsed3 + " milliseconds");

        startTime = System.currentTimeMillis();

        hasNonPrime = Sequence.primeNumber(primes);

        endTime = System.currentTimeMillis();

        long timeElapsed4 = endTime - startTime;
        System.out.println("Contains non-prime: " + hasNonPrime);

        System.out.println("Sequence Time taken: " + timeElapsed4 + " milliseconds");

        ExecutionTimeChart.main(
                (int) timeElapsed1, (int) timeElapsed2, (int) timeElapsed3, (int) timeElapsed4);
    }
}
