package org.example;

import java.util.Arrays;

public class Streams {
    private static boolean isPrime(int num) {
        if (num < 2) return false;
        for (int j = 2; j <= Math.sqrt(num); j++) {
            if (num % j == 0) return true;
        }
        return false;
    }

    public static boolean primeNumber(int[] array) {
        return Arrays.stream(array).parallel().anyMatch(num -> isPrime(num));
    }
}
