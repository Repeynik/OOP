package org.task_1;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.ChartUtils;
import org.jfree.chart.JFreeChart;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;

import java.io.File;
import java.io.IOException;

import javax.swing.*;

public class PerformanceTest {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(
                () -> {
                    JFrame frame = new JFrame("Performance Test");
                    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                    frame.add(createChartPanel());
                    frame.pack();
                    frame.setVisible(true);
                });
    }

    private static JPanel createChartPanel() {
        JFreeChart chart = createChart(createDataset());
        saveChartAsPNG(chart);
        return new ChartPanel(chart);
    }

    private static JFreeChart createChart(CategoryDataset dataset) {
        return ChartFactory.createLineChart(
                "Сравнение времени сортировки", "Размер массива", "Время (нс)", dataset);
    }

    private static CategoryDataset createDataset() {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        int[] sizes = {100, 200, 500, 1000, 2000};
        double C = 7;

        for (int size : sizes) {
            int[] array = new int[size];
            for (int i = 0; i < size; i++) {
                array[i] = (int) (Math.random() * 100);
            }

            long totalDuration = 0;
            int iterations = 50;
            for (int j = 0; j < iterations; j++) {
                int[] testArray = array.clone();
                long startTime = System.nanoTime();
                HeapSort.heapSort(testArray);
                long endTime = System.nanoTime();
                totalDuration += (endTime - startTime);
            }

            long averageDuration = totalDuration / iterations;
            double theoreticalTime = C * size * Math.log(size) / Math.log(2);

            dataset.addValue(averageDuration, "Практическое время", String.valueOf(size));
            dataset.addValue(theoreticalTime, "Теоретическое время", String.valueOf(size));
        }

        return dataset;
    }

    private static void saveChartAsPNG(JFreeChart chart) {
        File file = new File("performance_chart.png");
        try {
            ChartUtils.saveChartAsPNG(file, chart, 800, 600);
            System.out.println("График сохранен в " + file.getAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
