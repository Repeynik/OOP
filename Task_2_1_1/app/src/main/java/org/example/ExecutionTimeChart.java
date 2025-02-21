package org.example;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.ChartUtils;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;

import java.awt.*;
import java.io.File;
import java.io.IOException;

import javax.swing.*;

public class ExecutionTimeChart extends JFrame {

    public ExecutionTimeChart(String title, int time1, int time2, int time3, int time4) {
        super(title);
        CategoryDataset dataset = createDataset(time1, time2, time3, time4);
        JFreeChart chart =
                ChartFactory.createBarChart(
                        "Execution Time Comparison",
                        "Implementation",
                        "Time (ms)",
                        dataset,
                        PlotOrientation.VERTICAL,
                        true,
                        true,
                        false);

        ChartPanel panel = new ChartPanel(chart);
        panel.setPreferredSize(new Dimension(800, 600));
        setContentPane(panel);

        saveChartAsImage(chart, "ExecutionTimeChart.png");
    }

    private void saveChartAsImage(JFreeChart chart, String filePath) {
        try {
            ChartUtils.saveChartAsPNG(new File(filePath), chart, 800, 600);
            System.out.println("Chart saved as " + filePath);
        } catch (IOException e) {
            System.err.println("Error saving chart: " + e.getMessage());
        }
    }

    private CategoryDataset createDataset(int time1, int time2, int time3, int time4) {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        dataset.addValue(time1, "Parallel (4 threads)", "No1");
        dataset.addValue(time2, "Parallel (8 threads)", "No1");
        dataset.addValue(time3, "Parallel Stream", "No2");
        dataset.addValue(time4, "Sequential", "No3");

        return dataset;
    }

    public static void main(int time1, int time2, int time3, int time4) {
        SwingUtilities.invokeLater(
                () -> {
                    ExecutionTimeChart example =
                            new ExecutionTimeChart(
                                    "Execution Time Chart", time1, time2, time3, time4);
                    example.setSize(800, 600);
                    example.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
                    example.setLocationRelativeTo(null);
                    example.setVisible(true);
                });
    }
}
