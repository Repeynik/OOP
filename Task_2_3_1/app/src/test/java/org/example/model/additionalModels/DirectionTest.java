package org.example.model.additionalModels;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class DirectionTest {
    @Test
    void testIsOpposite() {
        assertTrue(Direction.UP.isOpposite(Direction.DOWN));
        assertFalse(Direction.UP.isOpposite(Direction.LEFT));
    }

    @Test
    void testOpposite() {
        assertEquals(Direction.DOWN, Direction.UP.opposite());
        assertEquals(Direction.LEFT, Direction.RIGHT.opposite());
    }

    @Test
    void testIsHorizontal() {
        assertTrue(Direction.LEFT.isHorizontal());
        assertFalse(Direction.UP.isHorizontal());
    }

    @Test
    void testIsVertical() {
        assertTrue(Direction.UP.isVertical());
        assertFalse(Direction.RIGHT.isVertical());
    }
}
