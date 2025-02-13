package org.example;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class AppTest {

    @Test
    public void testParallelWithNonPrime() throws InterruptedException {
        int[] array = {6, 8, 7, 13, 5, 9, 4};
        assertTrue(Parallel.primeNumber(array, 4));
    }

    @Test
    public void testParallelWithAllPrimes() throws InterruptedException {
        int[] array = {2, 3, 5, 7, 11, 13, 17};
        assertFalse(Parallel.primeNumber(array, 4));
    }

    @Test
    public void testSequenceWithNonPrime() {
        int[] array = {6, 8, 7, 13, 5, 9, 4};
        assertTrue(Sequence.primeNumber(array));
    }

    @Test
    public void testSequenceWithAllPrimes() {
        int[] array = {2, 3, 5, 7, 11, 13, 17};
        assertFalse(Sequence.primeNumber(array));
    }

    @Test
    public void testStreamsWithNonPrime() {
        int[] array = {6, 8, 7, 13, 5, 9, 4};
        assertTrue(Streams.primeNumber(array));
    }

    @Test
    public void testStreamsWithAllPrimes() {
        int[] array = {2, 3, 5, 7, 11, 13, 17};
        assertFalse(Streams.primeNumber(array));
    }

    @Test
    public void testEmptyArray() throws InterruptedException {
        int[] array = {};
        assertFalse(Parallel.primeNumber(array, 4));
        assertFalse(Sequence.primeNumber(array));
        assertFalse(Streams.primeNumber(array));
    }

    @Test
    public void testSingleElementArrayPrime() throws InterruptedException {
        int[] array = {2};
        assertFalse(Parallel.primeNumber(array, 4));
        assertFalse(Sequence.primeNumber(array));
        assertFalse(Streams.primeNumber(array));
    }

    @Test
    public void testSingleElementArrayNonPrime() throws InterruptedException {
        int[] array = {4};
        assertTrue(Parallel.primeNumber(array, 4));
        assertTrue(Sequence.primeNumber(array));
        assertTrue(Streams.primeNumber(array));
    }

    @Test
    public void testGenArray() throws InterruptedException {
        int[] array = NumbersGen.genPrimeNumbes(100000);
        assertFalse(Parallel.primeNumber(array, 4));
        assertFalse(Sequence.primeNumber(array));
        assertFalse(Streams.primeNumber(array));
    }
}
