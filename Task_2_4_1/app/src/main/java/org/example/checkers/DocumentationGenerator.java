package org.example.checkers;

import java.io.File;
import java.io.IOException;

public class DocumentationGenerator {

    public static boolean generate(String projectPath) {
        try {
            ProcessBuilder processBuilder = new ProcessBuilder("./gradlew", "javadoc");
            processBuilder.directory(new File(projectPath));
            Process process = processBuilder.start();
            int exitCode = process.waitFor();
            return exitCode == 0;
        } catch (IOException e) {
            System.err.println("Error generating documentation for project: " + projectPath);
            return false;
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.err.println("Error generating documentation for project: " + projectPath);
            return false;
        }
    }
}