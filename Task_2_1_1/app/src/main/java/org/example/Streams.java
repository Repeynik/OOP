package org.example;

import java.util.Arrays;

public class Streams {
    private Streams() {
        throw new IllegalStateException("Utility class");
    }

    public static boolean primeNumber(int[] array) {
        return Arrays.stream(array).parallel().anyMatch(BasePrime::isPrime);
    }
}
