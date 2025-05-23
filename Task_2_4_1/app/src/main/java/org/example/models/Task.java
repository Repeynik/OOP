package org.example.models;

public class Task {
    private String id;
    private String name;
    private int maxPoints;
    private String softDeadline;
    private String hardDeadline;
    private boolean hasBonusCondition;

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getMaxPoints() {
        return maxPoints;
    }

    public String getSoftDeadline() {
        return softDeadline;
    }

    public String getHardDeadline() {
        return hardDeadline;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setMaxPoints(int maxPoints) {
        this.maxPoints = maxPoints;
    }

    public void setSoftDeadline(String softDeadline) {
        this.softDeadline = softDeadline;
    }

    public void setHardDeadline(String hardDeadline) {
        this.hardDeadline = hardDeadline;
    }

    public void setHasBonusCondition(boolean hasBonusCondition) {
        this.hasBonusCondition = hasBonusCondition;
    }

    public boolean hasBonusCondition() {
        return hasBonusCondition;
    }

    public Task(String id, String name, int maxPoints, String softDeadline, String hardDeadline) {
        this.id = id;
        this.name = name;
        this.maxPoints = maxPoints;
        this.softDeadline = softDeadline;
        this.hardDeadline = hardDeadline;
    }

    public double calculateScore(boolean metSoftDeadline, boolean metHardDeadline, boolean bonusAchieved) {
        double score = maxPoints;
        if (!metHardDeadline) {
            score = 0;
        } else if (!metSoftDeadline) {
            score -= 0.5;
        }
        if (bonusAchieved && hasBonusCondition) {
            score += 1.0;
        }
        return score;
    }
}