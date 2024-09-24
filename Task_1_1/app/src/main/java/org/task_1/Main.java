package org.task_1;

public class Main {
    public static void main(String[] args) {
        ArrayHelper.printArray(HeapSort.heapSort(new int[] {7, 0, 7, 2, 1}));
        PerformanceTest.main(args);
    }
}
