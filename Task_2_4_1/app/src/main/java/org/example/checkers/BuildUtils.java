package org.example.checkers;

import java.io.File;
import java.io.IOException;

public class BuildUtils {
    public static boolean compileProject(String projectPath) throws IOException, InterruptedException {
        var processBuilder = new ProcessBuilder("./gradlew", "build");
        processBuilder.directory(new File(projectPath));
        processBuilder.inheritIO();
        Process process = processBuilder.start();
        int exitCode = process.waitFor();
        return exitCode == 0;
    }
}