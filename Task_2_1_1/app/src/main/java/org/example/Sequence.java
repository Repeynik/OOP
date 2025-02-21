package org.example;

public class Sequence {
    private Sequence() {
        throw new IllegalStateException("Utility class");
    }

    public static boolean primeNumber(int[] num) {
        return BasePrime.isPrime(num);
    }
}
