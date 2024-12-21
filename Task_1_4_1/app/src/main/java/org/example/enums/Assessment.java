package org.example.enums;

public class Assessment {
    private AssessmentType type;
    private Grade grade;
    private String subjectName;

    public Assessment(AssessmentType type, Grade grade, String subjectName) {
        this.type = type;
        this.grade = grade;
        this.subjectName = subjectName;
    }

    public AssessmentType getType() {
        return type;
    }

    public Grade getGrade() {
        return grade;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public void setGrade(Grade grade2) {
        this.grade = grade2;
    }
}
