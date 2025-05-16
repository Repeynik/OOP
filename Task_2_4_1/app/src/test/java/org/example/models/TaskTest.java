package org.example.models;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class TaskTest {
    @Test
    void testConstructorAndGetters() {
        Task task = new Task("1", "Task 1", 10, "2025-05-10", "2025-05-15");

        assertEquals("1", task.getId());
        assertEquals("Task 1", task.getName());
        assertEquals(10, task.getMaxPoints());
        assertEquals("2025-05-10", task.getSoftDeadline());
        assertEquals("2025-05-15", task.getHardDeadline());
    }

    @Test
    void testCalculateScore() {
        Task task = new Task("1", "Task 1", 10, "2025-05-10", "2025-05-15");
        task.setHasBonusCondition(true);

        assertEquals(11.0, task.calculateScore(true, true, true));
        assertEquals(10.0, task.calculateScore(true, true, false));
        assertEquals(9.5, task.calculateScore(false, true, false));
        assertEquals(0.0, task.calculateScore(false, false, false));
    }
}
