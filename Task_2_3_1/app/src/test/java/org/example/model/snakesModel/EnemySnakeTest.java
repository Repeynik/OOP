package org.example.model.snakesModel;

import static org.junit.jupiter.api.Assertions.*;

import org.example.model.GameModel;
import org.example.model.additionalModels.Point;
import org.example.model.configModel.LevelConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class EnemySnakeTest {
    private EnemySnake enemySnake;
    private GameModel gameModel;

    @BeforeEach
    void setUp() {
        enemySnake = new EnemySnake(new Point(5, 5), 200, "Random");
        LevelConfig config = new LevelConfig(1, 3, 3, 200, 200, 10, true, "Smart", false);
        gameModel = new GameModel(20, 20, config);
    }

    @Test
    void testUpdateDirection() {
        enemySnake.updateDirection(gameModel);
        assertNotNull(enemySnake.getDirection());
    }

    @Test
    void testGrow() {
        int initialSize = enemySnake.getBody().size();
        enemySnake.grow();
        enemySnake.move();
        assertEquals(initialSize + 1, enemySnake.getBody().size());
    }

    @Test
    void testNearEdges() {
        enemySnake = new EnemySnake(new Point(0, 0), 200, "SmartAi");
        assertTrue(
                enemySnake
                        .getNearEdges(new Point(0, 0), 20, 20)
                        .contains(org.example.model.additionalModels.Direction.LEFT));
    }
}
