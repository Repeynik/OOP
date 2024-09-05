package org.task_1;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class HeapTest {
    HeapSort classUnderTest = new HeapSort();

    @Test
    void initArr() {
        int[] input = {4, 10, 3, 5, 1};
        int[] output = classUnderTest.initHeap(input.clone(), input.length);
        assertArrayEquals(new int[] {10, 5, 3, 4, 1}, output);
    }

    @Test
    void testSwap() {
        int[] outputTestArray = {0, 1};
        classUnderTest.swap(outputTestArray, 1, 0);
        assertArrayEquals(new int[] {1, 0}, outputTestArray);
    }

    @Test
    void testSwiftDown() {
        int[] input = {10, 4, 3, 5, 1};
        classUnderTest.swiftDown(input, 5, 1);
        assertArrayEquals(new int[] {10, 5, 3, 4, 1}, input);
    }

    @Test
    void testHeapSort1() {
        int[] input = {4, 2, 8, 5, 4, 4, 2, 0};
        int[] output = classUnderTest.heapSort(input);
        assertArrayEquals(new int[] {0, 2, 2, 4, 4, 4, 5, 8}, output);
    }

    @Test
    void testHeapSort2() {
        int[] input = {};
        int[] output = classUnderTest.heapSort(input);
        assertArrayEquals(new int[] {}, output);
    }

    @Test
    void testHeapSort3() {
        int[] input = {1, 1, 1, 1};
        int[] output = classUnderTest.heapSort(input);
        assertArrayEquals(new int[] {1, 1, 1, 1}, output);
    }

    @Test
    void testHeapSort4() {
        int[] input = {10, 9, 8, 7, 6, 5, 4, 3, 2, 1};
        int[] output = classUnderTest.heapSort(input);
        assertArrayEquals(new int[] {1, 2, 3, 4, 5, 6, 7, 8, 9, 10}, output);
    }
}
