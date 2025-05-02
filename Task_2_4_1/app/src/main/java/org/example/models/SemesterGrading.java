package org.example.models;

public class SemesterGrading {
    private int exellent;
    private int good;
    private int satisfactory;

    public SemesterGrading(int exellent, int good, int satisfactory) {
        this.exellent = exellent;
        this.good = good;
        this.satisfactory = satisfactory;
    }

    public int getExellent() {
        return exellent;
    }

    public int getGood() {
        return good;
    }

    public int getSatisfactory() {
        return satisfactory;
    }

    public String calculateSemesterGrade(int totalPoints) {
        if (totalPoints >= exellent) {
            return "Отлично";
        } else if (totalPoints >= good) {
            return "Хорошо";
        } else if (totalPoints >= satisfactory) {
            return "Удовлетворительно";
        } else {
            return "Неудовлетворительно";
        }
    }
}