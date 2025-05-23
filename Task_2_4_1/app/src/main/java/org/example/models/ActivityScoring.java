package org.example.models;

public class ActivityScoring {
    private int activeCommitThreshold;
    private int bonusPointsPerActiveWeek;

    public ActivityScoring(int activeCommitThreshold, int bonusPointsPerActiveWeek) {
        this.activeCommitThreshold = activeCommitThreshold;
        this.bonusPointsPerActiveWeek = bonusPointsPerActiveWeek;
    }

    public int getActiveCommitThreshold() {
        return activeCommitThreshold;
    }

    public int getBonusPointsPerActiveWeek() {
        return bonusPointsPerActiveWeek;
    }
}