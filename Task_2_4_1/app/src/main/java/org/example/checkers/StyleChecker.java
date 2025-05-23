package org.example.checkers;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class StyleChecker {
    public static void checkStyle(String projectPath) throws IOException, InterruptedException {
        File projectDir = new File(projectPath);
        if (!projectDir.exists() || !projectDir.isDirectory()) {
            throw new FileNotFoundException("Project directory not found: " + projectPath);
        }
        List<File> javaFiles = new ArrayList<>();
        findJavaFilesRecursively(projectDir, javaFiles);

        if (javaFiles.isEmpty()) {
            throw new FileNotFoundException("No Java files found in directory: " + projectPath);
        }

        for (File javaFile : javaFiles) {
            ProcessBuilder processBuilder = new ProcessBuilder(
                "java",
                "-jar",
                "lib/google-java-format.jar",
                "--replace",
                javaFile.getAbsolutePath()
            );
            processBuilder.inheritIO();
            Process process = processBuilder.start();
            int exitCode = process.waitFor();
            if (exitCode != 0) {
                throw new RuntimeException("Style check failed for file: " + javaFile.getAbsolutePath());
            }
        }
    }

    private static void findJavaFilesRecursively(File directory, List<File> javaFiles) {
        File[] files = directory.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    findJavaFilesRecursively(file, javaFiles);
                } else if (file.getName().endsWith(".java")) {
                    javaFiles.add(file);
                }
            }
        }
    }
}