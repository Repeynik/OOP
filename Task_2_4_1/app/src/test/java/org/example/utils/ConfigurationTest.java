package org.example.utils;

import static org.junit.jupiter.api.Assertions.*;

import org.example.models.*;
import org.junit.jupiter.api.Test;

import java.util.List;

class ConfigurationTest {

    @Test
    void testLoadFromGroovyFile() {
        Configuration config = new Configuration();
        config.loadFromGroovyFile("src/test/resources/courseConfig.groovy");

        assertNotNull(config.getTasks());
        assertEquals(2, config.getTasks().size());

        assertNotNull(config.getGroups());
        assertEquals(1, config.getGroups().size());

        assertNotNull(config.getCheckpoints());
        assertEquals(2, config.getCheckpoints().size());

        assertNotNull(config.getActivityScoring());
        assertEquals(1, config.getActivityScoring().getActiveCommitThreshold());
        assertEquals(1, config.getActivityScoring().getBonusPointsPerActiveWeek());

        assertNotNull(config.getGrading());
        assertNotNull(config.getGrading().getFirstSemester());
        assertEquals(13, config.getGrading().getFirstSemester().getExellent());
    }

    @Test
    void testGetWeeklyDates() {
        Configuration config = new Configuration();
        config.setWeeklyStartDate("2025-01-01");

        List<String> weeklyDates = config.getWeeklyDates();
        assertFalse(weeklyDates.isEmpty());
        assertEquals("2025-01-01", weeklyDates.get(0));
    }
}
