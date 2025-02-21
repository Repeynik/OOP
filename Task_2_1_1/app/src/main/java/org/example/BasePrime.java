package org.example;

public abstract class BasePrime {
    public static boolean isPrime(int num) {
        if (num < 2) return false;
        for (int j = 2; j <= Math.sqrt(num); j++) {
            if (num % j == 0) return true;
        }
        return false;
    }

    public static boolean isPrime(int[] array) {
        for (int num : array) {
            if (isPrime(num)) {
                return true;
            }
        }
        return false;
    }
}
