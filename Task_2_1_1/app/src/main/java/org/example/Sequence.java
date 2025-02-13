package org.example;

public class Sequence {
    public static boolean primeNumber(int[] num) {
        for (int i = 0; i < num.length; i++) {
            if (num[i] < 2) continue;
            for (int j = 2; j <= Math.sqrt(num[i]); j++) {
                if (num[i] % j == 0) return true;
            }
        }
        return false;
    }
}
