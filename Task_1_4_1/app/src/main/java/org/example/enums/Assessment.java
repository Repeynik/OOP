package org.example.enums;

public class Assessment {
    private AssessmentType type;
    private Grade grade;

    public Assessment(AssessmentType type, Grade grade) {
        this.type = type;
        this.grade = grade;
    }

    public AssessmentType getType() {
        return type;
    }

    public Grade getGrade() {
        return grade;
    }
}
