package org.task_1;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class HeapTest {
    @Test
    void testHeapSort1() {
        int[] input = {4, 2, 8, 5, 4, 4, 2, 0};
        int[] output = HeapSort.heapSort(input);
        assertArrayEquals(new int[] {0, 2, 2, 4, 4, 4, 5, 8}, output);
    }

    @Test
    void testHeapSort2() {
        int[] input = {};
        int[] output = HeapSort.heapSort(input);
        assertArrayEquals(new int[] {}, output);
    }

    @Test
    void testHeapSort3() {
        int[] input = {1, 1, 1, 1};
        int[] output = HeapSort.heapSort(input);
        assertArrayEquals(new int[] {1, 1, 1, 1}, output);
    }

    @Test
    void testHeapSort4() {
        int[] input = {10, 9, 8, 7, 6, 5, 4, 3, 2, 1};
        int[] output = HeapSort.heapSort(input);
        assertArrayEquals(new int[] {1, 2, 3, 4, 5, 6, 7, 8, 9, 10}, output);
    }
}
