package org.example.model.managers;

import static org.junit.jupiter.api.Assertions.*;

import org.example.model.additionalModels.Point;
import org.example.model.snakesModel.Snake;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;

class FieldManagerTest {
    private FieldManager fieldManager;

    @BeforeEach
    void setUp() {
        fieldManager = new FieldManager(20, 20);
    }

    @Test
    void testFreeCellsInitialization() {
        assertEquals(400, fieldManager.getFreeCells().size());
    }

    @Test
    void testUpdateOccupiedCells() {
        Snake snake = new Snake(new Point(10, 10), 200);
        fieldManager.updateOccupiedCells(
                snake,
                null,
                Collections.emptyList(),
                Collections.emptyList(),
                Collections.emptyList(),
                false);
        assertFalse(fieldManager.getFreeCells().contains(new Point(10, 10)));
    }
}
