package org.example.model;

import java.util.Random;

public class EnemySnake extends Snake {
    private AIStrategy strategy;
    private final Random rand = new Random();

    public EnemySnake(Point startPosition, AIStrategy strategy) {
        super(startPosition);
        this.strategy = strategy;
    }

    public void updateDirection(GameModel model) {
        strategy.updateDirection(this, model);
    }

    public interface AIStrategy {
        void updateDirection(EnemySnake snake, GameModel model);
    }

    public static class RandomAI implements AIStrategy {
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

    public static class ChaseAI implements AIStrategy {
        @Override
        public void updateDirection(EnemySnake snake, GameModel model) {
            Point playerHead = model.getPlayerSnake().getHead();
            Point enemyHead = snake.getHead();

            int dx = Integer.compare(playerHead.x, enemyHead.x);
            int dy = Integer.compare(playerHead.y, enemyHead.y);

            if (dx != 0 && snake.rand.nextBoolean()) {
                snake.setDirection(dx > 0 ? Direction.RIGHT : Direction.LEFT);
            } else if (dy != 0) {
                snake.setDirection(dy > 0 ? Direction.DOWN : Direction.UP);
            }
        }
    }

    public Direction getDirection() {
        return super.getDirection();
    }
}
