package org.example.model;

import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class EnemySnake extends Snake {
    private final Random rand = new Random();
    private String behavior;

    public EnemySnake(Point startPosition, int initialSpeed, String behavior) {
        super(startPosition, initialSpeed);
        this.behavior = behavior;
    }

    public void updateDirection(GameModel model) {
        switch (behavior) {
            case "Random":
                new RandomAI().updateDirection(this, model);
                break;
            case "Chase":
                new ChaseAI().updateDirection(this, model);
                break;
            case "Smart":
                new SmartAI().updateDirection(this, model);
                break;
            default:
                throw new IllegalArgumentException("Unknown behavior: " + behavior);
        }
    }

    public void update(GameModel model) {
        super.move();

        model.getFoods().stream()
                .filter(food -> food.getPosition().equals(getHead()))
                .findFirst()
                .ifPresent(
                        food -> {
                            grow();
                            model.getFoods().remove(food);
                            model.addFood();
                        });

        if (model.getObstacles().stream()
                .anyMatch(o -> !o.isStatic() && o.getPosition().equals(getHead()))) {
            model.getEnemySnakes().remove(this);
        }
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
            Set<Direction> nearEdges = getNearEdges(enemyHead, gameWidth, gameHeight);
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
                return;
            }
        }

        private Set<Direction> getNearEdges(Point position, int gameWidth, int gameHeight) {
            Set<Direction> edges = new HashSet<>();
            if (position.x <= 3) edges.add(Direction.LEFT);
            if (position.x >= gameWidth - 3) edges.add(Direction.RIGHT);
            if (position.y <= 3) edges.add(Direction.UP);
            if (position.y >= gameHeight - 3) edges.add(Direction.DOWN);
            return edges;
        }
    }

    public static class SmartAI implements AIStrategy {
        private final Random rand = new Random();

        @Override
        public void updateDirection(EnemySnake snake, GameModel model) {
            Point enemyHead = snake.getHead();
            List<Food> foods = model.getFoods();
            List<Obstacle> obstacles = model.getObstacles();
            int gameWidth = model.getWidth();
            int gameHeight = model.getHeight();

            Set<Direction> nearEdges = getNearEdges(enemyHead, gameWidth, gameHeight);
            Direction currentDirection = snake.getDirection();
            if (!nearEdges.isEmpty()) {
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
                return;
            }

            if (rand.nextInt(100) < 30 && !foods.isEmpty()) {
                Food closestFood =
                        foods.stream()
                                .min(
                                        Comparator.comparingInt(
                                                food -> distance(enemyHead, food.getPosition())))
                                .orElse(null);

                if (closestFood != null) {
                    Point foodPosition = closestFood.getPosition();
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
            } else {
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
        }

        private int distance(Point a, Point b) {
            return Math.abs(a.x - b.x) + Math.abs(a.y - b.y);
        }

        private boolean isObstacleInDirection(
                Point position, Direction direction, List<Obstacle> obstacles) {
            Point nextPosition = new Point(position.x + direction.dx, position.y + direction.dy);
            return obstacles.stream()
                    .anyMatch(obstacle -> obstacle.getPosition().equals(nextPosition));
        }

        private Set<Direction> getNearEdges(Point position, int gameWidth, int gameHeight) {
            Set<Direction> edges = new HashSet<>();
            if (position.x <= 3) edges.add(Direction.LEFT);
            if (position.x >= gameWidth - 3) edges.add(Direction.RIGHT);
            if (position.y <= 3) edges.add(Direction.UP);
            if (position.y >= gameHeight - 3) edges.add(Direction.DOWN);
            return edges;
        }
    }
}
