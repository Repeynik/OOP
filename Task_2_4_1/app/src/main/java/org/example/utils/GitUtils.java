package org.example.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.File;
import java.util.logging.Logger;

public class GitUtils {
    private static final Logger logger = Logger.getLogger(GitUtils.class.getName());

    public static void checkGitConfig() {
        try {
            var processBuilder = new ProcessBuilder("git", "config", "--global", "user.name");
            var process = processBuilder.start();
            String userName;
            try (var reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                userName = reader.readLine();
            }
            catch (IOException e) {
                throw new RuntimeException("Error reading Git global user.name: " + e.getMessage(), e);
            }


            if (userName == null || userName.isEmpty()) {
                throw new RuntimeException(
                        "Git global user.name is not configured. Please configure it using 'git config --global user.name \"Your Name\"'.");
            }

            processBuilder = new ProcessBuilder("git", "config", "--global", "user.email");
            process = processBuilder.start();
            String userEmail;
            try (var reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                userEmail = reader.readLine();
            }
            catch (IOException e) {
                throw new RuntimeException("Error reading Git global user.email: " + e.getMessage(), e);
            }

            if (userEmail == null || userEmail.isEmpty()) {
                throw new RuntimeException(
                        "Git global user.email is not configured. Please configure it using 'git config --global user.email \"your.email@example.com\"'.");
            }

            logger.info("Git global configuration is properly set.");
        } catch (IOException e) {
            throw new RuntimeException("Error checking Git global configuration: " + e.getMessage(), e);
        }
    }

    public static void cloneRepository(String repoUrl, String destinationPath) throws IOException {
        checkGitConfig();
        try {
            var processBuilder = new ProcessBuilder("git", "clone", repoUrl, destinationPath);
            processBuilder.inheritIO();
            Process process = processBuilder.start();
            int exitCode = process.waitFor();
            if (exitCode != 0) {
                throw new RuntimeException("Failed to clone repository: " + repoUrl);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            logger.severe("Thread was interrupted during cloning repository: " + repoUrl);
            throw new RuntimeException("Thread was interrupted during cloning repository: " + repoUrl, e);
        }
    }

    public static void pullRepository(String localPath) throws IOException {
        checkGitConfig();
        try {
            var processBuilder = new ProcessBuilder("git", "-C", localPath, "pull");
            processBuilder.inheritIO();
            Process process = processBuilder.start();
            int exitCode = process.waitFor();
            if (exitCode != 0) {
                throw new RuntimeException("Failed to pull repository at: " + localPath);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            logger.severe("Thread was interrupted during pulling repository at: " + localPath);
            throw new RuntimeException("Thread was interrupted during pulling repository at: " + localPath, e);
        }
    }
}