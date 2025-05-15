package org.example.model.AiStrategies;

import org.example.model.GameModel;
import org.example.model.additionalModels.*;
import org.example.model.obstaclesModel.Obstacle;
import org.example.model.snakesModel.EnemySnake;
import org.example.model.snakesModel.EnemySnake.AIStrategy;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

public class SmartAI implements AIStrategy {

    @Override
    public void updateDirection(EnemySnake snake, GameModel model) {
        Point enemyHead = snake.getHead();
        List<Food> foods = model.getFoods();
        List<Obstacle> obstacles = model.getObstacles();
        int gameWidth = model.getWidth();
        int gameHeight = model.getHeight();

        Set<Direction> nearEdges = snake.getNearEdges(enemyHead, gameWidth, gameHeight);
        Direction currentDirection = snake.getDirection();

        if (!nearEdges.isEmpty()) {
            handleNearEdges(snake, enemyHead, obstacles, nearEdges, currentDirection);
            return;
        }

        if (shouldChaseFood(snake, foods)) {
            chaseClosestFood(snake, enemyHead, foods, obstacles);
        } else {
            avoidObstacles(snake, enemyHead, currentDirection, obstacles);
        }
    }

    private void handleNearEdges(
            EnemySnake snake,
            Point enemyHead,
            List<Obstacle> obstacles,
            Set<Direction> nearEdges,
            Direction currentDirection) {
        Direction newDirection =
                Arrays.stream(Direction.values())
                        .filter(
                                dir ->
                                        !dir.isOpposite(currentDirection)
                                                && !nearEdges.contains(dir)
                                                && !isObstacleInDirection(
                                                        enemyHead, dir, obstacles))
                        .findFirst()
                        .orElse(currentDirection);

        snake.setDirection(newDirection);
    }

    private boolean shouldChaseFood(EnemySnake snake, List<Food> foods) {
        return snake.rand.nextInt(100) < 30 && !foods.isEmpty();
    }

    private void chaseClosestFood(
            EnemySnake snake, Point enemyHead, List<Food> foods, List<Obstacle> obstacles) {
        Food closestFood =
                foods.stream()
                        .min(
                                Comparator.comparingInt(
                                        food -> distance(enemyHead, food.getPosition())))
                        .orElse(null);

        if (closestFood != null) {
            determineDirectionTowardsFood(snake, enemyHead, closestFood.getPosition(), obstacles);
        }
    }

    private void determineDirectionTowardsFood(
            EnemySnake snake, Point enemyHead, Point foodPosition, List<Obstacle> obstacles) {
        int dx = Integer.compare(foodPosition.x, enemyHead.x);
        int dy = Integer.compare(foodPosition.y, enemyHead.y);

        if (dx != 0
                && !isObstacleInDirection(
                        enemyHead, Direction.values()[dx > 0 ? 3 : 2], obstacles)) {
            snake.setDirection(dx > 0 ? Direction.RIGHT : Direction.LEFT);
        } else if (dy != 0
                && !isObstacleInDirection(
                        enemyHead, Direction.values()[dy > 0 ? 1 : 0], obstacles)) {
            snake.setDirection(dy > 0 ? Direction.DOWN : Direction.UP);
        }
    }

    private void avoidObstacles(
            EnemySnake snake,
            Point enemyHead,
            Direction currentDirection,
            List<Obstacle> obstacles) {
        if (isObstacleInDirection(enemyHead, currentDirection, obstacles)) {
            Direction newDirection =
                    Arrays.stream(Direction.values())
                            .filter(
                                    dir ->
                                            !dir.isOpposite(currentDirection)
                                                    && !isObstacleInDirection(
                                                            enemyHead, dir, obstacles))
                            .findFirst()
                            .orElse(currentDirection);
            snake.setDirection(newDirection);
        }
    }

    private int distance(Point a, Point b) {
        return Math.abs(a.x - b.x) + Math.abs(a.y - b.y);
    }

    private boolean isObstacleInDirection(
            Point position, Direction direction, List<Obstacle> obstacles) {
        Point nextPosition = new Point(position.x + direction.dx, position.y + direction.dy);
        return obstacles.stream().anyMatch(obstacle -> obstacle.getPosition().equals(nextPosition));
    }
}
