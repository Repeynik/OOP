package org.example.models;

public class Grading {
    private SemesterGrading firstSemester;
    private SemesterGrading secondSemester;

    public SemesterGrading getFirstSemester() {
        return firstSemester;
    }

    public void setFirstSemester(SemesterGrading firstSemester) {
        this.firstSemester = firstSemester;
    }

    public SemesterGrading getSecondSemester() {
        return secondSemester;
    }

    public void setSecondSemester(SemesterGrading secondSemester) {
        this.secondSemester = secondSemester;
    }
}