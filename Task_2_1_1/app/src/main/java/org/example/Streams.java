package org.example;

import java.util.Arrays;

public class Streams {

    public static boolean primeNumber(int[] array) {
        return Arrays.stream(array).parallel().anyMatch(num -> BasePrime.isPrime(num));
    }
}
