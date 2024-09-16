package org.task_1;

public class ArrayHelper {

    public static void swap(int[] array, int largeElem, int tree) {
        int temp = array[largeElem];
        array[largeElem] = array[tree];
        array[tree] = temp;
    }

    public static void printArray(int[] array) {
        int length = array.length;
        for (int i = 0; i < length; i++) {
            System.out.print(array[i] + " ");
        }
        System.out.println('\n');
    }
}
