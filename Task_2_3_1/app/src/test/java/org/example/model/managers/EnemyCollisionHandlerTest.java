package org.example.model.managers;

import static org.junit.jupiter.api.Assertions.*;

import org.example.model.GameModel;
import org.example.model.additionalModels.Point;
import org.example.model.configModel.LevelConfig;
import org.example.model.snakesModel.EnemySnake;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

class EnemyCollisionHandlerTest {
    private EnemyCollisionHandler collisionHandler;
    private GameModel gameModel;

    @BeforeEach
    void setUp() {
        collisionHandler = new EnemyCollisionHandler();
        LevelConfig config = new LevelConfig(1, 3, 3, 200, 200, 10, true, "Smart", false);
        gameModel = new GameModel(20, 20, config);
    }

    @Test
    void testEnemyCollisionWithPlayerSnake() {
        List<EnemySnake> enemySnakes = new ArrayList<>(gameModel.getEnemySnakes());
        EnemySnake enemy = new EnemySnake(gameModel.getPlayerSnake().getHead(), 200, "Smart");
        enemySnakes.add(enemy);
        int previousNumber = enemySnakes.size();
        gameModel.getEnemySnakes().getFirst().getBody().add(new Point(5, 5));
        gameModel.getPlayerSnake().getBody().add(new Point(5, 5));
        System.out.println("Player snake body: " + gameModel.getPlayerSnake().getBody());
        System.out.println("Enemy snake body: " + enemy.getBody());
        for (EnemySnake enemySnake : enemySnakes) {
            System.out.println(("number of enemy snakes: " + enemySnakes.size()));
        }

        collisionHandler.checkCollisions(
                enemy,
                gameModel.getPlayerSnake(),
                null,
                gameModel.getObstacles(),
                enemySnakes,
                false,
                gameModel.getFieldManager().getFreeCells(),
                gameModel);
        gameModel.update();
        for (EnemySnake enemySnake : enemySnakes) {
            System.out.println(("number of enemy snakes: " + enemySnakes.size()));
        }

        assertTrue(enemySnakes.size() < previousNumber);
    }
}
