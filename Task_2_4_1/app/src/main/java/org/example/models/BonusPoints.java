package org.example.models;

public class BonusPoints {
    private String student;
    private String task;
    private int points;

    public BonusPoints(String student, String task, int points) {
        this.student = student;
        this.task = task;
        this.points = points;
    }

    public String getStudent() {
        return student;
    }

    public int getPoints() {
        return points;
    }

    public String getTask() {
        return task;
    }

    public void setStudent(String student) {
        this.student = student;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public void setTask(String task) {
        this.task = task;
    }
}