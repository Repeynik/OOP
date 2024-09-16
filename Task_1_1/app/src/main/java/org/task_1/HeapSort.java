package org.task_1;

/** Класс для реализации пирамидальной сортировки. */
public class HeapSort {
    /**
     * инициализация двоичной кучи.
     *
     * @param array массив чисел для сортировки
     * @param length длина массива
     */
    public int[] initHeap(int[] array, int length) {

        for (int currentElem = length / 2 - 1; currentElem >= 0; currentElem--) {
            swiftDown(array, length, currentElem);
        }
        return array;
    }

    /**
     * Метод для перемещения местами двух элементов массива.
     *
     * @param array массив чисел
     * @param largeElem индекс числа для перемещения
     * @param tree индекса числа для перемещения
     */
    public void swap(int[] array, int largeElem, int tree) {
        int temp = array[largeElem];
        array[largeElem] = array[tree];
        array[tree] = temp;
    }

    /**
     * Просеивание кучи вниз, наибольший элемент массива становится на 0 индекс.
     *
     * @param array массив чисел
     * @param length длина массива
     * @param currentElem текущий корень двоичной кучи
     */
    public int[] swiftDown(int[] array, int length, int currentElem) {
        int largeElem = currentElem;
        int leftTree = 2 * currentElem + 1;
        int rightTree = 2 * currentElem + 2;

        if (leftTree < length && array[largeElem] < array[leftTree]) {
            largeElem = leftTree;
        }

        if (rightTree < length && array[largeElem] < array[rightTree]) {
            largeElem = rightTree;
        }

        if (largeElem != currentElem) {
            swap(array, currentElem, largeElem);
            swiftDown(array, length, largeElem);
        }
        return array;
    }

    /**
     * Сортировка массива с помощью пирамидальной сортировки на двоичной куче.
     *
     * @param array массив чисел
     */
    public int[] heapSort(int[] array) {
        int length = array.length;

        int[] initArray = initHeap(array, length);

        for (int count = length - 1; count >= 0; count--) {
            swap(initArray, 0, count);
            swiftDown(initArray, count, 0);
        }
        // printArray(initArray);
        return (initArray);
    }

    /**
     * Метод для печати массива чисел.
     *
     * @param array массив чисел
     */
    public void printArray(int[] array) {
        int length = array.length;
        for (int i = 0; i < length; i++) {
            System.out.print(array[i] + " ");
        }
        System.out.println('\n');
    }

    /** Метод для сравнения практической и теоретической сложности алгоритма */
    public void testPerformance() {
        int[] sizes = {100, 200, 500, 1000, 2000}; // Размеры массивов
        double C = 7;
        for (int size : sizes) {
            int[] array = new int[size];
            for (int i = 0; i < size; i++) {
                array[i] = (int) (Math.random() * 100); // Заполнение случайными числами
            }

            long totalDuration = 0;
            int iterations = 50; // Количество итераций для усреднения
            for (int j = 0; j < iterations; j++) {
                int[] testArray = array.clone(); // Клонирование массива для каждой итерации
                long startTime = System.nanoTime();
                heapSort(testArray);
                long endTime = System.nanoTime();
                totalDuration += (endTime - startTime);
            }

            long averageDuration = totalDuration / iterations; // Среднее время выполнения

            // Вычисление теоретического времени
            double theoreticalTime =
                    C * size * Math.log(size) / Math.log(2); // Логарифм по основанию 2

            // Вывод результатов
            System.out.printf(
                    "Size: %d, Average Practical Time: %d ns, Theoretical Time: %.2f%n",
                    size, averageDuration, theoreticalTime);
        }
    }
}
