package org.example.model.additionalModels;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class PointTest {
    @Test
    void testEquals() {
        Point p1 = new Point(5, 5);
        Point p2 = new Point(5, 5);
        Point p3 = new Point(6, 5);
        assertEquals(p1, p2);
        assertNotEquals(p1, p3);
    }
}
