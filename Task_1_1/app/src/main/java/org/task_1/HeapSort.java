package org.task_1;

public class HeapSort { // класс сортировки двоичной кучи

    public int[] initHeap(int[] array, int length) { // инициаизация двоичной кучи

        for (int currentElem = length / 2 - 1; currentElem >= 0; currentElem--) {
            swiftDown(array, length, currentElem);
        }
        return array;
    }

    public void swap(int[] array, int largeElem, int tree) {  // изменение порядка элементов в куче
        int temp = array[largeElem];
        array[largeElem] = array[tree];
        array[tree] = temp;
    }

    public int[] swiftDown(int[] array, int length, int currentElem) { // просеивание кучи вниз 
        int largeElem = currentElem;
        int leftTree = 2 * currentElem + 1;
        int rightTree = 2 * currentElem + 2;

        if (leftTree < length && array[largeElem] < array[leftTree]) {
            largeElem = leftTree;
        }

        if (rightTree < length && array[largeElem] < array[rightTree]) {
            largeElem = rightTree;
        }

        if (largeElem != currentElem){
            swap(array, currentElem, largeElem);
            swiftDown(array, length, largeElem);
        }
        return array;
    }

    

    public int[] heapSort(int[] array) { // пиромидальная сортировка, итоговая
        int length = array.length;

        int[] initArray = initHeap(array, length);

        for (int count = length - 1; count >= 0; count--) {
            swap(initArray, 0, count);
            swiftDown(initArray, count, 0);
        }
        printArray(initArray);
        return (initArray);
    }

    public void printArray(int[] array) { // печать массива
        int length = array.length;
        for (int i = 0; i < length; i++) {
            System.out.print(array[i] + " ");
        }
        System.out.println('\n');
    }

    public static void main(String[] args) { // вызов сортировки 
        HeapSort heap = new HeapSort();
        heap.heapSort(new int[] { 4, 10, 3, 5, 1 });
    }
}
