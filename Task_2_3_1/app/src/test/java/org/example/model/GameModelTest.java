package org.example.model;

import static org.junit.jupiter.api.Assertions.*;

import org.example.model.additionalModels.Point;
import org.example.model.configModel.LevelConfig;
import org.example.model.snakesModel.Snake;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class GameModelTest {
    private GameModel gameModel;

    @BeforeEach
    void setUp() {
        LevelConfig config = new LevelConfig(1, 3, 3, 200, 200, 10, true, "Smart", false);
        gameModel = new GameModel(20, 20, config);
    }

    @Test
    void testAddFood() {
        int initialFoodCount = gameModel.getFoods().size();
        gameModel.addFood();
        assertEquals(initialFoodCount + 1, gameModel.getFoods().size());
    }

    @Test
    void testUpdateSnakeMovement() {
        Snake playerSnake = gameModel.getPlayerSnake();
        Point initialHead = playerSnake.getHead();
        playerSnake.setDirection(org.example.model.additionalModels.Direction.RIGHT);
        playerSnake.move();
        gameModel.update();
        Point newHead = playerSnake.getHead();
        assertNotEquals(initialHead, newHead);
    }

    @Test
    void testVictoryCondition() {
        Snake playerSnake = gameModel.getPlayerSnake();
        for (int i = 0; i <= 10; i++) {
            playerSnake.grow();
            playerSnake.move();
        }
        System.out.println("Snake length: " + playerSnake.getBody().size());
        gameModel.update();
        assertTrue(gameModel.isGameWon());
    }
}
