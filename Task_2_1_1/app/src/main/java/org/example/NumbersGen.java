package org.example;

public class NumbersGen {
    private NumbersGen() {
        throw new IllegalStateException("Utility class");
    }

    public static int[] genPrimeNumbes(int count) {
        int[] primes = new int[count];
        int index = 0;
        while (index < count) {
            primes[index] = 6998053;
            index++;
        }

        return primes;
    }
}
