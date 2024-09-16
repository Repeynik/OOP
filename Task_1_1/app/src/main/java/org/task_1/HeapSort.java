package org.task_1;

public class HeapSort {

    private static int[] initHeap(int[] array, int length) {

        for (var currentElem = length / 2 - 1; currentElem >= 0; currentElem--) {
            swiftDown(array, length, currentElem);
        }
        return array;
    }

    /**
     * Просеивание кучи вниз, наибольший элемент массива становится на 0 индекс.
     *
     * @param array массив чисел
     * @param length длина массива
     * @param currentElem текущий корень двоичной кучи
     */
    private static int[] swiftDown(int[] array, int length, int currentElem) {
        var largeElem = currentElem;
        var leftTree = 2 * currentElem + 1;
        var rightTree = 2 * currentElem + 2;

        if (leftTree < length && array[largeElem] < array[leftTree]) {
            largeElem = leftTree;
        }

        if (rightTree < length && array[largeElem] < array[rightTree]) {
            largeElem = rightTree;
        }

        if (largeElem != currentElem) {
            ArrayHelper.swap(array, currentElem, largeElem);
            swiftDown(array, length, largeElem);
        }
        return array;
    }

    public static int[] heapSort(int[] array) {
        int length = array.length;

        var copyArray = array.clone();
        var initArray = initHeap(copyArray, length);

        for (var count = length - 1; count >= 0; count--) {
            ArrayHelper.swap(initArray, 0, count);
            swiftDown(initArray, count, 0);
        }
        return initArray;
    }
}
