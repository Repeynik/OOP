package org.example.checkers;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import org.example.models.TestResults;
import org.example.utils.TestResultsParser;

public class TestRunner {
    public static TestResults runTests(String projectPath) throws IOException, InterruptedException {
        File testReportDir = new File(projectPath, "/app/build/test-results/test");
        if (!testReportDir.exists() || !testReportDir.isDirectory()) {
            throw new RuntimeException("Test report directory not found: " + testReportDir.getAbsolutePath());
        }

        ProcessBuilder processBuilder = new ProcessBuilder("./gradlew", "test");
        processBuilder.directory(new File(projectPath));
        processBuilder.inheritIO();
        Process process = processBuilder.start();
        int exitCode = process.waitFor();

        if (exitCode != 0) {
            System.err.println("Tests failed to execute for project: " + projectPath);
        }

        File[] reportFilesHtml = testReportDir.listFiles((dir, name) -> name.endsWith(".html"));
        File[] reportFilesXml = testReportDir.listFiles((dir, name) -> name.endsWith(".xml"));
        System.out.println("Report files HTML: " + Arrays.toString(reportFilesHtml));
        System.out.println("Report files XML: " + Arrays.toString(reportFilesXml));
        if ((reportFilesHtml == null || reportFilesHtml.length == 0)
                && (reportFilesXml == null || reportFilesXml.length == 0)) {
            throw new RuntimeException("No test report files found in: " + testReportDir.getAbsolutePath());
        }

        int passed = 0;
        int failed = 0;
        int skipped = 0;

        File[] reportFiles = (reportFilesHtml != null && reportFilesHtml.length > 0) ? reportFilesHtml
                : reportFilesXml;
        for (File reportFile : reportFiles) {
            var testResults = TestResultsParser.parseTestResults(reportFile.getAbsolutePath());
            passed += testResults.getPassed();
            failed += testResults.getFailed();
            skipped += testResults.getSkipped();
        }

        return new TestResults(passed, failed, skipped);
    }
}