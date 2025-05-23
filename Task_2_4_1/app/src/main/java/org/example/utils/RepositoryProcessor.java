package org.example.utils;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import org.example.checkers.BuildUtils;
import org.example.checkers.DocumentationGenerator;
import org.example.checkers.StyleChecker;
import org.example.checkers.TestRunner;
import org.example.models.Group;
import org.example.models.Student;
import org.example.models.StudentResult;
import org.example.models.StudentsToCheck;
import org.example.models.Task;
import org.example.models.TestResults;

public class RepositoryProcessor {
    private static final Logger logger = Logger.getLogger(RepositoryProcessor.class.getName());

    public static List<StudentResult> processRepositories(Configuration config, boolean pullRepositories,
            boolean recheckOnly, List<Student> studentsToCheck) {
        List<StudentResult> results = new ArrayList<>();
        ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

        for (Group group : config.getGroups()) {
            for (Student student : group.getStudents()) {
                if (!studentsToCheck.contains(student)) {
                    continue;
                }
                results.addAll(processStudentRepository(config, student, pullRepositories, recheckOnly, executor));
            }
        }
        executor.shutdown();
        return results;
    }

    private static List<StudentResult> processStudentRepository(Configuration config, Student student, boolean pullRepositories, boolean recheckOnly, ExecutorService executor) {
        List<StudentResult> results = new ArrayList<>();
        String repoUrl = student.getRepositoryUrl();
        String localPath = "./repos/" + student.getGithubUsername();
        try {
            if (!recheckOnly) {
                File repoDir = new File(localPath);
                if (!repoDir.exists()) {
                    GitUtils.cloneRepository(repoUrl, localPath);
                } else if (pullRepositories) {
                    GitUtils.pullRepository(localPath);
                }
            }

            File repoDir = new File(localPath);
            File[] taskDirs = repoDir.listFiles(file -> file.isDirectory() && !file.getName().startsWith("."));

            if (taskDirs == null || taskDirs.length == 0) {
                logger.warning("No tasks found in repository for: " + student.getFullName());
                results.add(new StudentResult(student, "None", false, false, false, new TestResults(0, 0, 0), 0, "N/A"));
                return results;
            }

            List<Future<StudentResult>> futures = new ArrayList<>();
            for (File taskDir : taskDirs) {
                futures.add(executor.submit(() -> processStudentTask(config, student, taskDir)));
            }
            for (Future<StudentResult> future : futures) {
                try {
                    results.add(future.get());
                } catch (Exception e) {
                    logger.severe("Error retrieving task result: " + e.getMessage());
                }
            }
        } catch (Exception e) {
            logger.severe("Error processing repository for: " + student.getFullName() + ". " + e.getMessage());
            results.add(new StudentResult(student, "None", false, false, false, new TestResults(0, 0, 0), 0, "N/A"));
        }
        return results;
    }

    private static StudentResult processStudentTask(Configuration config, Student student, File taskDir) {
        String taskName = taskDir.getName();
        Task task = config.getTasks().stream()
                .filter(t -> t.getName().equals(taskName))
                .findFirst()
                .orElse(null);
        if (task == null) {
            logger.warning("Task " + taskName + " is not recognized in configuration for: " + student.getFullName());
            return new StudentResult(student, taskName, false, false, false, new TestResults(0, 0, 0), 0, "N/A");
        }
        boolean buildSuccess = false;
        boolean docSuccess = false;
        boolean styleSuccess = false;
        TestResults testResults = new TestResults(0, 0, 0);
        int totalActivityScore = 0;
        String lastCommitDate = "N/A";
        try {
            buildSuccess = BuildUtils.compileProject(taskDir.getAbsolutePath());
            if (!buildSuccess) {
                logger.warning("Build failed for task " + taskName + " for: " + student.getFullName());
            }
            docSuccess = DocumentationGenerator.generate(taskDir.getAbsolutePath());
            if (!docSuccess) {
                logger.warning("Documentation generation failed for task " + taskName + " for: " + student.getFullName());
            }
            try {
                StyleChecker.checkStyle(taskDir.getAbsolutePath());
                styleSuccess = true;
            } catch (Exception e) {
                logger.warning("Style check failed for task " + taskName + " for: " + student.getFullName() + ". " + e.getMessage());
            }
            if (buildSuccess) {
                testResults = TestRunner.runTests(taskDir.getAbsolutePath());
            }
            try {
                for (String weekStart : config.getWeeklyDates()) {
                    String weekEnd = calculateWeekEnd(weekStart);
                    totalActivityScore += ActivityAnalyzer.analyzeWeeklyActivity(taskDir.getAbsolutePath(), weekStart, weekEnd);
                }
                lastCommitDate = ActivityAnalyzer.getLastCommitDate(taskDir.getAbsolutePath());
            } catch (Exception e) {
                logger.warning("Error analyzing activity for task " + taskName + " for: " + student.getFullName());
            }
        } catch (Exception e) {
            logger.severe("Error processing task " + taskName + " for: " + student.getFullName() + ". " + e.getMessage());
        }
        return new StudentResult(student, taskName, buildSuccess, docSuccess, styleSuccess, testResults, totalActivityScore, lastCommitDate);
    }

    private static String calculateWeekEnd(String weekStart) {
        LocalDate start = LocalDate.parse(weekStart);
        LocalDate end = start.plusDays(6);
        return end.toString();
    }

    public static List<Student> filterStudentsToCheck(Configuration config) {
        List<StudentsToCheck> studentsToCheck = config.getStudentsToCheck();
        if (studentsToCheck == null || studentsToCheck.isEmpty()) {
            return config.getGroups().stream()
                .flatMap(group -> group.getStudents().stream())
                .collect(Collectors.toList());
        }

        return config.getGroups().stream()
            .flatMap(group -> group.getStudents().stream())
            .filter(student -> studentsToCheck.stream()
                .anyMatch(st -> st.getName().equals(student.getFullName())))
            .collect(Collectors.toList());
    }
}