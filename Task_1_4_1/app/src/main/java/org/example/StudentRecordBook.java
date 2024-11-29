package org.example;

import org.example.enums.Assessment;
import org.example.enums.AssessmentType;
import org.example.enums.FormOfStudy;
import org.example.enums.Grade;
import org.example.enums.Session;

import java.util.ArrayList;
import java.util.List;

public class StudentRecordBook {
    private List<Session> sessions = new ArrayList<>();
    private FormOfStudy formOfStudy;
    private Assessment qualificationWorkAssessment;
    private int currentSemester;

    public StudentRecordBook(FormOfStudy formOfStudy, int currentSemester) {
        this.formOfStudy = formOfStudy;
        this.currentSemester = currentSemester;
    }

    public void addSession(Session session) {
        sessions.add(session);
    }

    public void setQualificationWorkGrade(Grade grade) {
        qualificationWorkAssessment =
                new Assessment(AssessmentType.FINAL_QUALIFYING_WORK_DEFENSE, grade);
    }

    public double getCurrentAverage() {
        double sum = 0.0;
        int count = 0;
        for (var session : sessions) {
            for (var assessment : session.getAssessments()) {
                sum += gradeToValue(assessment.getGrade());
                count++;
            }
        }
        if (count == 0) {
            return 0.0;
        }
        return sum / count;
    }

    private double gradeToValue(Grade grade) {
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

    public boolean canSwitchToBudget() {
        if (formOfStudy != FormOfStudy.PAID) {
            return false;
        }
        if (sessions.size() < 2) {
            return false;
        }
        int lastIndex = sessions.size() - 1;
        var lastSession = sessions.get(lastIndex);
        var secondLastSession = sessions.get(lastIndex - 1);
        var lastTwoExamAssessments = new ArrayList<>();
        for (var assessment : lastSession.getAssessments()) {
            if (assessment.getType() == AssessmentType.EXAM) {
                lastTwoExamAssessments.add(assessment);
            }
        }
        for (var assessment : secondLastSession.getAssessments()) {
            if (assessment.getType() == AssessmentType.EXAM) {
                lastTwoExamAssessments.add(assessment);
            }
        }
        for (var assessment : lastTwoExamAssessments) {
            if (((Assessment) assessment).getGrade() == Grade.SATISFACTORY) {
                return false;
            }
        }
        return true;
    }

    public boolean canGetRedDiploma() {
        if (qualificationWorkAssessment != null) {
            if (qualificationWorkAssessment.getGrade() != Grade.EXCELLENT) {
                return false;
            }
        }

        if (sessions.isEmpty()) {
            return false;
        }
        int excellentCount = 0;
        int totalAssessments = 0;
        for (var session : sessions) {
            var diplomaSupplementAssessments = new ArrayList<>(session.getAssessments());
            for (var assessment : diplomaSupplementAssessments) {
                if (assessment.getGrade() == Grade.SATISFACTORY
                        && (assessment.getType() == AssessmentType.EXAM
                                || assessment.getType() == AssessmentType.DIFFERENTIAL_CREDIT)) {
                    return false;
                }
            }
            totalAssessments += diplomaSupplementAssessments.size();
            for (var assessment : diplomaSupplementAssessments) {
                if (assessment.getGrade() == Grade.EXCELLENT) {
                    excellentCount++;
                }
            }
        }
        return ((double) excellentCount / totalAssessments) >= 0.75;
    }

    public boolean canGetIncreasedScholarship() {
        if (currentSemester <= 0 || currentSemester > sessions.size()) {
            return false;
        }
        var currentSession = sessions.get(currentSemester - 1);
        var assessments = currentSession.getAssessments();
        if (assessments.isEmpty()) {
            return false;
        }
        for (var assessment : assessments) {
            if (assessment.getGrade() != Grade.EXCELLENT) {
                return false;
            }
        }
        return true;
    }
}
