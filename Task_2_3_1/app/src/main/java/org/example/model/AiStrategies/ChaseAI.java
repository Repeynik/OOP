package org.example.model.AiStrategies;

import org.example.model.GameModel;
import org.example.model.additionalModels.Direction;
import org.example.model.additionalModels.Point;
import org.example.model.snakesModel.EnemySnake;
import org.example.model.snakesModel.EnemySnake.AIStrategy;

import java.util.Arrays;
import java.util.Set;

public class ChaseAI implements AIStrategy {

    @Override
    public void updateDirection(EnemySnake snake, GameModel model) {
        Point playerHead = model.getPlayerSnake().getHead();
        Point enemyHead = snake.getHead();
        int gameWidth = model.getWidth();
        int gameHeight = model.getHeight();

        int dx = Integer.compare(playerHead.x, enemyHead.x);
        int dy = Integer.compare(playerHead.y, enemyHead.y);

        if (dx != 0 && snake.rand.nextBoolean()) {
            snake.setDirection(dx > 0 ? Direction.RIGHT : Direction.LEFT);
        } else if (dy != 0) {
            snake.setDirection(dy > 0 ? Direction.DOWN : Direction.UP);
        }
        Direction currentDirection = snake.getDirection();
        Set<Direction> nearEdges = snake.getNearEdges(enemyHead, gameWidth, gameHeight);
        if (!nearEdges.isEmpty()) {
            Direction newDirection =
                    Arrays.stream(Direction.values())
                            .filter(
                                    dir ->
                                            !dir.isOpposite(currentDirection)
                                                    && !nearEdges.contains(dir))
                            .findFirst()
                            .orElse(currentDirection);

            snake.setDirection(newDirection);
        }
    }
}
