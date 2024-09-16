package org.task_1;

public class Main {
    /**
     * Точка входа программы.
     *
     * @param args стандартные аргументы
     */
    public static void main(String[] args) {
        HeapSort heap = new HeapSort();
        heap.printArray(heap.heapSort(new int[] {4, 10, 3, 5, 1}));
        heap.testPerformance();
    }
}
