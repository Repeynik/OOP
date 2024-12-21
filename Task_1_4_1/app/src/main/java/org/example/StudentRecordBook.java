package org.example;

import org.example.enums.Assessment;
import org.example.enums.AssessmentType;
import org.example.enums.FormOfStudy;
import org.example.enums.Grade;
import org.example.enums.Session;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class StudentRecordBook {
    private List<Session> sessions = new ArrayList<>();
    private FormOfStudy formOfStudy;
    private Assessment qualificationWorkAssessment;
    private int currentSemester;
    private String name;
    private String group;
    private String faculty;

    public StudentRecordBook(
            FormOfStudy formOfStudy,
            int currentSemester,
            String name,
            String group,
            String faculty) {
        this.formOfStudy = formOfStudy;
        this.currentSemester = currentSemester;
        this.name = name;
        this.group = group;
        this.faculty = faculty;
        initializeSessions();
    }

    private void initializeSessions() {
        IntStream.range(0, 6).forEach(i -> sessions.add(new Session()));

        sessions.get(0).addAssessment(new Assessment(AssessmentType.EXAM, null, "Mathematics"));
        sessions.get(0)
                .addAssessment(new Assessment(AssessmentType.DIFFERENTIAL_CREDIT, null, "Physics"));

        sessions.get(1).addAssessment(new Assessment(AssessmentType.EXAM, null, "Chemistry"));
        sessions.get(1)
                .addAssessment(new Assessment(AssessmentType.DIFFERENTIAL_CREDIT, null, "Biology"));

        sessions.get(2).addAssessment(new Assessment(AssessmentType.EXAM, null, "History"));
        sessions.get(2)
                .addAssessment(
                        new Assessment(AssessmentType.DIFFERENTIAL_CREDIT, null, "Geography"));
        sessions.get(2)
                .addAssessment(new Assessment(AssessmentType.CONTROL_WORK, null, "Literature"));

        sessions.get(3).addAssessment(new Assessment(AssessmentType.EXAM, null, "Economics"));
        sessions.get(3)
                .addAssessment(
                        new Assessment(AssessmentType.DIFFERENTIAL_CREDIT, null, "Sociology"));

        sessions.get(4).addAssessment(new Assessment(AssessmentType.EXAM, null, "Philosophy"));
        sessions.get(4)
                .addAssessment(
                        new Assessment(AssessmentType.DIFFERENTIAL_CREDIT, null, "Psychology"));

        sessions.get(5)
                .addAssessment(new Assessment(AssessmentType.EXAM, null, "Political Science"));
        sessions.get(5)
                .addAssessment(new Assessment(AssessmentType.DIFFERENTIAL_CREDIT, null, "Law"));
    }

    public void setGrade(int sessionIndex, String subjectName, Grade grade) {
        if (sessionIndex >= 1
                && sessionIndex <= sessions.size()
                && sessionIndex <= currentSemester) {
            var session = sessions.get(sessionIndex - 1);
            boolean subjectFound =
                    session.getAssessments().stream()
                            .anyMatch(
                                    assessment -> assessment.getSubjectName().equals(subjectName));
            if (subjectFound) {
                session.getAssessments().stream()
                        .filter(assessment -> assessment.getSubjectName().equals(subjectName))
                        .forEach(assessment -> assessment.setGrade(grade));
            } else {
                System.err.println(
                        "Error: Subject name '"
                                + subjectName
                                + "' not found in session "
                                + sessionIndex);
            }
        } else {
            System.err.println("Error: Invalid session index " + sessionIndex);
        }
    }

    public void setQualificationWorkGrade(Grade grade, String subjectName) {
        qualificationWorkAssessment =
                new Assessment(AssessmentType.FINAL_QUALIFYING_WORK_DEFENSE, grade, subjectName);
    }

    public double getCurrentAverage() {
        double sum =
                sessions.stream()
                        .flatMap(session -> session.getAssessments().stream())
                        .filter(assessment -> assessment.getGrade() != null)
                        .mapToDouble(assessment -> gradeToValue(assessment.getGrade()))
                        .sum();
        long count =
                sessions.stream()
                        .flatMap(session -> session.getAssessments().stream())
                        .filter(assessment -> assessment.getGrade() != null)
                        .count();
        return count == 0 ? 0.0 : sum / count;
    }

    private double gradeToValue(Grade grade) {
        if (grade == null) {
            return 0.0;
        }
        switch (grade) {
            case EXCELLENT:
                return 5.0;
            case GOOD:
                return 4.0;
            case SATISFACTORY:
                return 3.0;
            default:
                return 0.0;
        }
    }

    private boolean checkPreviousGrades() {
        for (int i = 0; i < currentSemester - 1; i++) {
            var session = sessions.get(i);
            boolean hasNullGrades =
                    session.getAssessments().stream()
                            .anyMatch(assessment -> assessment.getGrade() == null);
            if (hasNullGrades) {
                System.err.println(
                        "Error: Session " + (i + 1) + " has assessments with null grades.");
                return false;
            }
        }
        return true;
    }

    public boolean canSwitchToBudget() {
        if (formOfStudy != FormOfStudy.PAID || currentSemester < 2) {
            return false;
        }

        if (checkPreviousGrades() == false) {
            return false;
        }

        var currentSession = sessions.get(currentSemester - 1);
        var previousSession = sessions.get(currentSemester - 2);

        List<Assessment> currentExamAssessments =
                currentSession.getAssessments().stream()
                        .filter(
                                assessment ->
                                        assessment.getType() == AssessmentType.EXAM
                                                && assessment.getGrade() != null)
                        .collect(Collectors.toList());

        List<Assessment> previousExamAssessments =
                previousSession.getAssessments().stream()
                        .filter(
                                assessment ->
                                        assessment.getType() == AssessmentType.EXAM
                                                && assessment.getGrade() != null)
                        .collect(Collectors.toList());

        if (previousExamAssessments.isEmpty()) {
            System.err.println("Error: Previous session has no valid exam assessments.");
            return false;
        }

        List<Assessment> currentAndPreviousExamAssessments =
                Stream.concat(currentExamAssessments.stream(), previousExamAssessments.stream())
                        .collect(Collectors.toList());

        return currentAndPreviousExamAssessments.stream()
                .noneMatch(assessment -> assessment.getGrade() == Grade.SATISFACTORY);
    }

    public boolean canGetRedDiploma() {
        if (qualificationWorkAssessment != null
                && qualificationWorkAssessment.getGrade() != Grade.EXCELLENT) {
            return false;
        }

        if (sessions.isEmpty()) {
            return false;
        }

        long excellentCount =
                sessions.stream()
                        .flatMap(session -> session.getAssessments().stream())
                        .filter(assessment -> assessment.getGrade() == Grade.EXCELLENT)
                        .count();

        long totalAssessments =
                sessions.stream()
                        .flatMap(session -> session.getAssessments().stream())
                        .filter(assessment -> assessment.getGrade() != null)
                        .count();

        boolean hasSatisfactoryExamOrDifferentialCredit =
                sessions.stream()
                        .flatMap(session -> session.getAssessments().stream())
                        .anyMatch(
                                assessment ->
                                        assessment.getGrade() == Grade.SATISFACTORY
                                                && (assessment.getType() == AssessmentType.EXAM
                                                        || assessment.getType()
                                                                == AssessmentType
                                                                        .DIFFERENTIAL_CREDIT));

        return !hasSatisfactoryExamOrDifferentialCredit
                && ((double) excellentCount / totalAssessments) >= 0.75;
    }

    public boolean canGetIncreasedScholarship() {
        if (currentSemester <= 0 || currentSemester > sessions.size()) {
            return false;
        }
        var currentSession = sessions.get(currentSemester - 1);
        return currentSession.getAssessments().stream()
                .filter(assessment -> assessment.getGrade() != null)
                .allMatch(assessment -> assessment.getGrade() == Grade.EXCELLENT);
    }

    public String getName() {
        return name;
    }

    public String getGroup() {
        return group;
    }

    public String getFaculty() {
        return faculty;
    }

    public FormOfStudy getFormOfStudy() {
        return formOfStudy;
    }

    public int getcCurrentSemester() {
        return currentSemester;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public void setFaculty(String faculty) {
        this.faculty = faculty;
    }

    public void setFormOfStudy(FormOfStudy formOfStudy) {
        this.formOfStudy = formOfStudy;
    }

    public void setCurrentSemester(int currentSemester) {
        this.currentSemester = currentSemester;
    }

    public List<Session> getSessions() {
        return sessions;
    }
}
