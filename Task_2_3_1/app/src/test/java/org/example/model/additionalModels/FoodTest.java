package org.example.model.additionalModels;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class FoodTest {
    @Test
    void testGetPosition() {
        Point position = new Point(5, 5);
        Food food = new Food(position);
        assertEquals(position, food.getPosition());
    }
}
