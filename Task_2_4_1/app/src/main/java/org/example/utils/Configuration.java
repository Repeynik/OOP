package org.example.utils;

import java.util.List;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Map;
import java.util.function.Function;

import org.example.models.ActivityScoring;
import org.example.models.BonusPoints;
import org.example.models.Checkpoint;
import org.example.models.Grading;
import org.example.models.Group;
import org.example.models.SemesterGrading;
import org.example.models.Student;
import org.example.models.StudentsToCheck;
import org.example.models.Task;

import groovy.lang.Binding;
import groovy.lang.GroovyShell;
import java.io.File;
import java.io.IOException;

public class Configuration {
    private List<Task> tasks;
    private List<Group> groups;
    private List<Checkpoint> checkpoints;
    private ActivityScoring activityScoring;
    private LocalDate weeklyStartDate = LocalDate.of(2025, 1, 1);
    private Grading grading;
    private List<BonusPoints> points;
    private List<StudentsToCheck> studentsToCheck;

    public void setWeeklyStartDate(String date) {
        weeklyStartDate = LocalDate.parse(date);
    }

    private <T> List<T> parseList(List<Map<String, Object>> maps, Function<Map<String, Object>, T> mapper) {
        List<T> result = new ArrayList<>();
        if (maps != null) {
            for (var map : maps) {
                result.add(mapper.apply(map));
            }
        }
        return result;
    }

    public void loadFromGroovyFile(String filePath) {
        File file = new File(filePath);
        if (!file.exists()) {
            throw new RuntimeException("Configuration file not found: " + filePath);
        }
        var binding = new Binding();
        var shell = new GroovyShell(binding);
        try {
            shell.evaluate(new File(filePath));
        } catch (IOException e) {
            throw new RuntimeException("Error evaluating Groovy file: " + filePath, e);
        }

        tasks = parseList((List<Map<String, Object>>) binding.getVariable("tasks"), taskMap -> 
            new Task(
                (String) taskMap.get("id"),
                (String) taskMap.get("name"),
                (int) taskMap.get("maxPoints"),
                (String) taskMap.get("softDeadline"),
                (String) taskMap.get("hardDeadline")
            )
        );

        studentsToCheck = parseList((List<Map<String, Object>>) binding.getVariable("studentsToCheck"), studentToCheckMap -> 
            new StudentsToCheck((String) studentToCheckMap.get("name"))
        );

        points = parseList((List<Map<String, Object>>) binding.getVariable("bonusPoints"), pointsMap -> 
            new BonusPoints(
                (String) pointsMap.get("student"),
                (String) pointsMap.get("task"),
                (int) pointsMap.get("points")
            )
        );

        groups = parseList((List<Map<String, Object>>) binding.getVariable("groups"), groupMap -> {
            var students = parseList((List<Map<String, Object>>) groupMap.get("students"), studentMap -> 
                new Student(
                    (String) studentMap.get("githubUsername"),
                    (String) studentMap.get("fullName"),
                    (String) studentMap.get("repositoryUrl")
                )
            );
            return new Group((String) groupMap.get("name"), students);
        });

        checkpoints = parseList((List<Map<String, Object>>) binding.getVariable("checkpoints"), checkpointMap -> 
            new Checkpoint(
                (String) checkpointMap.get("name"),
                (String) checkpointMap.get("date")
            )
        );

        var activityScoringMap = (Map<String, Object>) binding.getVariable("activityScoring");
        if (activityScoringMap != null) {
            activityScoring = new ActivityScoring(
                (int) activityScoringMap.get("activeCommitThreshold"),
                (int) activityScoringMap.get("bonusPointsPerActiveWeek")
            );
        }

        var gradingMap = (Map<String, Object>) binding.getVariable("grading");
        if (gradingMap != null) {
            Grading grading = new Grading();

            var firstSemester = (Map<String, Integer>) gradingMap.get("firstSemester");
            if (firstSemester != null) {
                grading.setFirstSemester(new SemesterGrading(
                    firstSemester.get("exellent"),
                    firstSemester.get("good"),
                    firstSemester.get("satisfactory")
                ));
            }

            var secondSemester = (Map<String, Integer>) gradingMap.get("secondSemester");
            if (secondSemester != null) {
                grading.setSecondSemester(new SemesterGrading(
                    secondSemester.get("exellent"),
                    secondSemester.get("good"),
                    secondSemester.get("satisfactory")
                ));
            }

            setGrading(grading);
        }

        String weeklyStartDateStr = (String) binding.getVariable("weeklyStartDate");
        if (weeklyStartDateStr != null) {
            setWeeklyStartDate(weeklyStartDateStr);
        }

        if (tasks == null) tasks = new ArrayList<>();
        if (groups == null) groups = new ArrayList<>();
        if (checkpoints == null) checkpoints = new ArrayList<>();
        if (activityScoring == null) activityScoring = new ActivityScoring(0, 0);
        if (points == null) points = new ArrayList<>();
    }

    public List<Task> getTasks() {
        return tasks;
    }

    public List<Group> getGroups() {
        return groups;
    }

    public List<Checkpoint> getCheckpoints() {
        return checkpoints;
    }

    public ActivityScoring getActivityScoring() {
        return activityScoring;
    }

    public List<StudentsToCheck> getStudentsToCheck() {
        return studentsToCheck;
    }

    public void setActivityScoring(ActivityScoring activityScoring) {
        this.activityScoring = activityScoring;
    }

    public Grading getGrading() {
        return grading;
    }

    public void setGrading(Grading grading) {
        this.grading = grading;
    }

    public List<BonusPoints> getPoints() {
        return points;
    }

    public void setPoints(List<BonusPoints> points) {
        this.points = points;
    }

    public List<String> getWeeklyDates() {
        List<String> weeklyDates = new ArrayList<>();
        LocalDate startDate = weeklyStartDate;
        LocalDate currentDate = LocalDate.now();

        while (!startDate.isAfter(currentDate)) {
            weeklyDates.add(startDate.toString());
            startDate = startDate.plusWeeks(1);
        }

        return weeklyDates;
    }
}