package org.example.checkers;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.logging.Logger;

import org.example.models.BonusPoints;
import org.example.models.Checkpoint;
import org.example.models.Group;
import org.example.models.Student;
import org.example.models.StudentResult;
import org.example.models.Task;
import org.example.models.TestResults;
import org.example.utils.ActivityAnalyzer;
import org.example.utils.Configuration;

public class ReportGenerator {
    private static final Logger logger = Logger.getLogger(ReportGenerator.class.getName());

    public static void generateHtmlReport(Configuration config, List<StudentResult> studentResults, String outputPath)
            throws IOException {
        var html = new StringBuilder();
        html.append("<html><head><title>Course Report</title></head><body>");
        html.append("<h1>Course Report</h1>");

        for (Group group : config.getGroups()) {
            html.append("<h2>Group: ").append(group.getName()).append("</h2>");
            for (Task task : config.getTasks()) {
                html.append("<h3>Task: ").append(task.getName()).append(" (ID: ").append(task.getId()).append(")</h3>");
                html.append("<table border='1'>");
                html.append(
                        "<tr><th>Студент</th><th>Сборка</th><th>Документация</th><th>Style guide</th><th>Тесты</th><th>Доп. балл</th><th>Общий балл</th></tr>");

                for (StudentResult result : studentResults) {
                    if (!result.getTaskName().equals(task.getName())) {
                        continue;
                    }

                    html.append("<tr>");
                    html.append("<td>").append(result.getStudent().getFullName()).append("</td>");
                    html.append("<td>").append(result.isBuildSuccess() ? "Успех" : "Ошибка").append("</td>");
                    html.append("<td>").append(result.isDocSuccess() ? "Сгенерирована" : "Ошибка").append("</td>");
                    html.append("<td>").append(result.isStyleSuccess() ? "Пройден" : "Ошибка").append("</td>");
                    html.append("<td>").append(result.getTestResults().getPassed()).append("/")
                            .append(result.getTestResults().getFailed()).append("/")
                            .append(result.getTestResults().getSkipped()).append("</td>");

                    int bonusPoints = config.getPoints().stream()
                            .filter(bp -> bp.getStudent().equals(result.getStudent().getFullName())
                                    && bp.getTask().equals(task.getName()))
                            .mapToInt(BonusPoints::getPoints)
                            .sum();
                    int totalPoints = calculateTotalPoints(task, result.getTestResults(),
                            result.getTotalActivityScore(), config) + bonusPoints;

                    html.append("<td>").append(bonusPoints).append("</td>");
                    html.append("<td>").append(totalPoints).append("</td>");
                    html.append("</tr>");
                }

                html.append("</table>");
            }
        }

        html.append("</body></html>");

        try (FileWriter writer = new FileWriter(outputPath)) {
            writer.write(html.toString());
        } catch (IOException e) {
            logger.severe("Error writing report to file: " + e.getMessage());
            throw e;
        }
    }

    public static void generateGroupSummary(Configuration config, List<StudentResult> studentResults, String outputPath)
            throws IOException {
        var html = new StringBuilder();
        html.append("<html><head><title>Group Summary</title></head><body>");
        html.append("<h1>Group Summary</h1>");

        for (Group group : config.getGroups()) {
            html.append("<h2>Group: ").append(group.getName()).append("</h2>");
            html.append("<table border='1'>");
            html.append("<tr><th>Student</th>");

            for (Task task : config.getTasks()) {
                html.append("<th>").append(task.getName()).append("</th>");
            }

            html.append("<th>Total Points</th><th>Activity</th><th>Grade</th></tr>");

            for (Student student : group.getStudents()) {
                html.append("<tr>");
                html.append("<td>").append(student.getFullName()).append("</td>");

                int totalPoints = 0;
                int totalCommits = 0;
                int maxCommits = 0;

                for (Task task : config.getTasks()) {
                    StudentResult result = studentResults.stream()
                            .filter(r -> r.getStudent().equals(student) && r.getTaskName().equals(task.getName()))
                            .findFirst()
                            .orElse(null);

                    if (result != null) {
                        int taskPoints = calculateTotalPoints(task, result.getTestResults(),
                                result.getTotalActivityScore(), config);
                        int bonusPoints = config.getPoints().stream()
                                .filter(bp -> bp.getStudent().equals(student.getFullName())
                                        && bp.getTask().equals(task.getName()))
                                .mapToInt(BonusPoints::getPoints)
                                .sum();
                        taskPoints += bonusPoints;
                        totalPoints += taskPoints;
                        totalCommits += result.getTotalActivityScore();
                        maxCommits = Math.max(maxCommits, result.getTotalActivityScore());
                        html.append("<td>").append(taskPoints).append("</td>");
                    } else {
                        html.append("<td>0</td>");
                    }
                }

                int totalWeeks = config.getWeeklyDates().size();
                int activeWeeks = Math.min(totalCommits / config.getActivityScoring().getActiveCommitThreshold(), totalWeeks);
                int activityPercentage = (int) ((double) activeWeeks / totalWeeks * 100);

                String grade = config.getGrading().getFirstSemester().calculateSemesterGrade(totalPoints);

                html.append("<td>").append(totalPoints).append("</td>");
                html.append("<td>").append(activityPercentage).append("%</td>");
                html.append("<td>").append(grade).append("</td>");
                html.append("</tr>");
            }

            html.append("</table>");
        }

        html.append("</body></html>");

        try (FileWriter writer = new FileWriter(outputPath)) {
            writer.write(html.toString());
        } catch (IOException e) {
            logger.severe("Error writing group summary to file: " + e.getMessage());
            throw e;
        }
    }

    public static void generateCheckpointReports(Configuration config, List<StudentResult> studentResults, String outputPath) throws IOException {
        var html = new StringBuilder();
        html.append("<html><head><title>Checkpoint Reports</title></head><body>");
        html.append("<h1>Checkpoint Reports</h1>");

        for (Checkpoint checkpoint : config.getCheckpoints()) {
            html.append("<h2>Checkpoint: ").append(checkpoint.getName()).append(" (Date: ").append(checkpoint.getDate()).append(")</h2>");

            LocalDate checkpointDate = LocalDate.parse(checkpoint.getDate());
            int maxPointsAtCheckpoint = 0;

            html.append("<table border='1'>");
            html.append("<tr><th>Student</th><th>Task</th><th>Points</th><th>Activity (%)</th></tr>");

            for (Group group : config.getGroups()) {
                for (Student student : group.getStudents()) {
                    int totalPoints = 0;

                    for (Task task : config.getTasks()) {
                        LocalDate hardDeadline = LocalDate.parse(task.getHardDeadline());
                        LocalDate softDeadline = LocalDate.parse(task.getSoftDeadline());
                        LocalDate activityStartDate = softDeadline.minusWeeks(2);

                        if (hardDeadline.isAfter(checkpointDate) || softDeadline.isAfter(checkpointDate)) {
                            continue;
                        }

                        maxPointsAtCheckpoint += task.getMaxPoints();

                        StudentResult result = studentResults.stream()
                                .filter(r -> r.getStudent().equals(student) && r.getTaskName().equals(task.getName()))
                                .findFirst()
                                .orElse(null);

                        if (result != null) {
                            int taskPoints = calculateTotalPoints(task, result.getTestResults(),
                                    result.getTotalActivityScore(), config);
                            int bonusPoints = config.getPoints().stream()
                                    .filter(bp -> bp.getStudent().equals(student.getFullName())
                                            && bp.getTask().equals(task.getName()))
                                    .mapToInt(BonusPoints::getPoints)
                                    .sum();
                            taskPoints += bonusPoints;
                            totalPoints += taskPoints;

                            int taskCommits = 0;
                            try {
                                taskCommits = ActivityAnalyzer.analyzeWeeklyActivity("/home/aly0na/OOP/OOP/Task_2_4_1/app/repos/" + student.getGithubUsername()+ "/" + task.getName(), activityStartDate.toString(), hardDeadline.toString());
                            } catch (InterruptedException e) {
                                Thread.currentThread().interrupt();
                                logger.warning("Thread was interrupted while analyzing weekly activity for task " + task.getName() + " for student: " + student.getFullName());
                            } catch (IOException e) {
                                logger.warning("Error analyzing weekly activity for task " + task.getName() + " for student: " + student.getFullName() + ". " + e.getMessage());
                            }
                            int taskWeeks = 2;
                            int taskActivityPercentage = Math.min((taskCommits * 100) / (taskWeeks * config.getActivityScoring().getActiveCommitThreshold()), 100);

                            html.append("<tr>");
                            html.append("<td>").append(student.getFullName()).append("</td>");
                            html.append("<td>").append(task.getName()).append("</td>");
                            html.append("<td>").append(taskPoints).append("</td>");
                            html.append("<td>").append(taskActivityPercentage).append("%</td>");
                            html.append("</tr>");
                        }
                    }

                    html.append("<tr>");
                    html.append("<td>").append(student.getFullName()).append("</td>");
                    html.append("<td>Total</td>");
                    html.append("<td>").append(totalPoints).append("</td>");
                    html.append("</tr>");
                }
            }

            html.append("</table>");
            html.append("<p>Maximum Points at Checkpoint: ").append(maxPointsAtCheckpoint).append("</p>");
        }

        html.append("</body></html>");

        try (FileWriter writer = new FileWriter(outputPath)) {
            writer.write(html.toString());
        } catch (IOException e) {
            logger.severe("Error writing checkpoint reports to file: " + e.getMessage());
            throw e;
        }
    }

    private static int calculateTotalPoints(Task task, TestResults testResults, int activityScore,
            Configuration config) {
        int baseScore = (int) task.calculateScore(
                testResults.getPassed() > 0,
                testResults.getFailed() == 0,
                activityScore >= config.getActivityScoring().getActiveCommitThreshold());

        int totalWeeks = config.getWeeklyDates().size();
        int activeWeeks = activityScore / config.getActivityScoring().getActiveCommitThreshold();
        return adjustPointsForActivity(baseScore, totalWeeks, activeWeeks);
    }

    private static int adjustPointsForActivity(int totalPoints, int totalWeeks, int activeWeeks) {
        double activityPercentage = (double) activeWeeks / totalWeeks * 100;

        if (activityPercentage >= 80) {
            return totalPoints;
        } else if (activityPercentage >= 60) {
            return totalPoints - 1;
        } else if (activityPercentage >= 40) {
            return totalPoints / 2;
        } else {
            return 0;
        }
    }
}