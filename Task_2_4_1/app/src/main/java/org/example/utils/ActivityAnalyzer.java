package org.example.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.logging.Logger;

public class ActivityAnalyzer {
    private static final Logger logger = Logger.getLogger(ActivityAnalyzer.class.getName());

    public static int analyzeWeeklyActivity(String repoPath, String startDate, String endDate)
            throws IOException, InterruptedException {
        ProcessBuilder processBuilder = new ProcessBuilder(
                "git", "log", "--since=" + startDate, "--until=" + endDate, "--pretty=format:%H");
        processBuilder.directory(new File(repoPath));
        Process process = processBuilder.start();

        int commitCount = 0;
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
            while (reader.readLine() != null) {
                commitCount++;
            }
        }

        int exitCode = process.waitFor();
        if (exitCode != 0) {
            logger.severe("Failed to analyze activity for repository: " + repoPath);
            throw new RuntimeException("Failed to analyze activity for repository: " + repoPath);
        }

        return commitCount;
    }

    public static String getLastCommitDate(String repoPath) throws IOException, InterruptedException {
        var processBuilder = new ProcessBuilder(
                "git", "log", "-1", "--pretty=format:%ci");
        processBuilder.directory(new File(repoPath));
        Process process = processBuilder.start();

        String lastCommitDate;
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
            lastCommitDate = reader.readLine();
        }

        int exitCode = process.waitFor();
        if (exitCode != 0) {
            logger.severe("Failed to get last commit date for repository: " + repoPath);
            throw new RuntimeException("Failed to get last commit date for repository: " + repoPath);
        }

        return lastCommitDate;
    }
}