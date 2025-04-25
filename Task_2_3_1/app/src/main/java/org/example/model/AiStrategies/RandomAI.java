package org.example.model.AiStrategies;

import org.example.model.GameModel;
import org.example.model.additionalModels.Direction;
import org.example.model.snakesModel.EnemySnake;
import org.example.model.snakesModel.EnemySnake.AIStrategy;

public class RandomAI implements AIStrategy {
    @Override
    public void updateDirection(EnemySnake snake, GameModel model) {
        if (snake.rand.nextInt(10) < 3) {
            Direction newDir = Direction.values()[snake.rand.nextInt(4)];
            if (!newDir.isOpposite(snake.getDirection())) {
                snake.setDirection(newDir);
            }
        }
    }
}
