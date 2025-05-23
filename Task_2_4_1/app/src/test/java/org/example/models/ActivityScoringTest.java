package org.example.models;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class ActivityScoringTest {
    @Test
    void testConstructorAndGetters() {
        ActivityScoring scoring = new ActivityScoring(5, 2);

        assertEquals(5, scoring.getActiveCommitThreshold());
        assertEquals(2, scoring.getBonusPointsPerActiveWeek());
    }
}
