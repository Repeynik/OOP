package org.example.models;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class BonusPointsTest {
    @Test
    void testConstructorAndGetters() {
        BonusPoints bonus = new BonusPoints("Student One", "Task 1", 5);

        assertEquals("Student One", bonus.getStudent());
        assertEquals("Task 1", bonus.getTask());
        assertEquals(5, bonus.getPoints());
    }

    @Test
    void testSetters() {
        BonusPoints bonus = new BonusPoints("", "", 0);
        bonus.setStudent("Student Two");
        bonus.setTask("Task 2");
        bonus.setPoints(10);

        assertEquals("Student Two", bonus.getStudent());
        assertEquals("Task 2", bonus.getTask());
        assertEquals(10, bonus.getPoints());
    }
}
