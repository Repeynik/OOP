package org.example.models;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class CheckpointTest {
    @Test
    void testConstructorAndGetters() {
        Checkpoint checkpoint = new Checkpoint("Midterm", "2025-06-01");

        assertEquals("Midterm", checkpoint.getName());
        assertEquals("2025-06-01", checkpoint.getDate());
    }

    @Test
    void testSetters() {
        Checkpoint checkpoint = new Checkpoint("", "");
        checkpoint.setName("Final");
        checkpoint.setDate("2025-07-01");

        assertEquals("Final", checkpoint.getName());
        assertEquals("2025-07-01", checkpoint.getDate());
    }
}
