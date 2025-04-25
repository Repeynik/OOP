package org.example.model.snakesModel;

import static org.junit.jupiter.api.Assertions.*;

import org.example.model.additionalModels.Direction;
import org.example.model.additionalModels.Point;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SnakeTest {
    private Snake snake;

    @BeforeEach
    void setUp() {
        snake = new Snake(new Point(5, 5), 200);
    }

    @Test
    void testMove() {
        snake.setDirection(Direction.RIGHT);
        snake.move();
        assertEquals(new Point(6, 5), snake.getHead());
    }

    @Test
    void testGrow() {
        int initialSize = snake.getBody().size();
        snake.grow();
        snake.move();
        assertEquals(initialSize + 1, snake.getBody().size());
    }

    @Test
    void testShrink() {
        snake.grow();
        snake.move();
        int initialSize = snake.getBody().size();
        snake.shrink();
        assertEquals(initialSize - 1, snake.getBody().size());
    }
}
