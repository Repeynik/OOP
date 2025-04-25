package org.example.model;

import org.example.model.additionalModels.Food;
import org.example.model.additionalModels.Point;
import org.example.model.configModel.LevelConfig;
import org.example.model.managers.EnemyCollisionHandler;
import org.example.model.managers.FieldManager;
import org.example.model.obstaclesModel.MovingObstacle;
import org.example.model.obstaclesModel.Obstacle;
import org.example.model.obstaclesModel.StaticWall;
import org.example.model.snakesModel.EnemySnake;
import org.example.model.snakesModel.Snake;

import java.util.*;
import java.util.stream.IntStream;

public class GameModel {
    private int width;
    private int height;
    private Snake playerSnake;
    private Snake playerSnake2;
    private List<Food> foods;
    private int targetLength;
    private boolean gameOver;
    private boolean gameWon;
    private String behavior;
    private List<Obstacle> obstacles = new ArrayList<>();
    private Random rand = new Random();
    private int obstacleCount;
    private boolean secondSnakeEnabled;

    private List<EnemySnake> enemySnakes = new ArrayList<>();
    private FieldManager fieldManager;

    private EnemyCollisionHandler enemyCollisionHandler;

    public GameModel(int width, int height, LevelConfig config) {
        this.width = width;
        this.height = height;
        this.targetLength = config.getTargetLength() + 1;
        this.playerSnake = new Snake(new Point(width / 2, height / 2), config.getSnakeSpeed());
        this.secondSnakeEnabled = config.isSecondSnakeEnabled();
        if (secondSnakeEnabled) {
            this.playerSnake2 = new Snake(new Point(width / 4, height / 4), config.getSnakeSpeed());
        }
        this.foods = new ArrayList<>();
        this.behavior = config.getBehavior();
        this.enemyCollisionHandler = new EnemyCollisionHandler();
        this.fieldManager = new FieldManager(width, height);

        obstacleCount = config.getObstacleCount();
        fieldManager.updateOccupiedCells(
                playerSnake, playerSnake2, foods, obstacles, enemySnakes, secondSnakeEnabled);
        IntStream.range(0, config.getObstacleCount()).forEach(i -> addObstacle());
        IntStream.range(0, config.getEnemyCount()).forEach(i -> addEnemy());
        IntStream.range(0, 3).forEach(i -> addFood());
    }

    private void addObstacle() {
        fieldManager.updateOccupiedCells(
                playerSnake, playerSnake2, foods, obstacles, enemySnakes, secondSnakeEnabled);

        if (!fieldManager.getFreeCells().isEmpty()) {
            List<Point> freeCellsList = new ArrayList<>(fieldManager.getFreeCells());
            Point startPoint = freeCellsList.get(rand.nextInt(freeCellsList.size()));

            if (rand.nextBoolean()) {
                obstacles.add(new MovingObstacle(startPoint, width, height));
            } else {
                int obstacleSize = rand.nextInt(5) + 1;
                for (int i = 0; i < obstacleSize; i++) {
                    Point newPoint = new Point(startPoint.x + i, startPoint.y);
                    if (fieldManager.getFreeCells().contains(newPoint)) {
                        obstacles.add(new StaticWall(newPoint));
                        fieldManager.getFreeCells().remove(newPoint);
                    } else {
                        break;
                    }
                }
            }
        }
    }

    private void addEnemy() {
        fieldManager.updateOccupiedCells(
                playerSnake, playerSnake2, foods, obstacles, enemySnakes, secondSnakeEnabled);

        if (!fieldManager.getFreeCells().isEmpty()) {
            List<Point> freeCellsList = new ArrayList<>(fieldManager.getFreeCells());
            Point newPoint = freeCellsList.get(rand.nextInt(freeCellsList.size()));
            enemySnakes.add(new EnemySnake(newPoint, 200, this.behavior));
        }
    }

    public void addFood() {
        fieldManager.updateOccupiedCells(
                playerSnake, playerSnake2, foods, obstacles, enemySnakes, secondSnakeEnabled);

        if (!fieldManager.getFreeCells().isEmpty()) {
            List<Point> freeCellsList = new ArrayList<>(fieldManager.getFreeCells());
            Point newPoint = freeCellsList.get(rand.nextInt(freeCellsList.size()));
            foods.add(new Food(newPoint));
        }
    }

    public void update() {
        if (gameOver || gameWon) return;

        obstacles.forEach(
                obstacle -> {
                    if (obstacle.isStatic()) {
                        return;
                    }
                    obstacle.update(this);
                });

        updateSnake(playerSnake);

        if (secondSnakeEnabled) {
            updateSnake(playerSnake2);
        }

        updateEnemySnakes();

        if (playerSnake.getBody().size() >= targetLength
                || (secondSnakeEnabled && playerSnake2.getBody().size() >= targetLength)) {
            gameWon = true;
        }
    }

    private void updateSnake(Snake snake) {
        Point previousHead = snake.getHead();
        Point newHead =
                new Point(
                        previousHead.x + snake.getDirection().dx,
                        previousHead.y + snake.getDirection().dy);

        if (isOutOfBounds(newHead)
                || snake.getBody().contains(newHead)
                || enemySnakes.stream().anyMatch(e -> e.getBody().contains(newHead))
                || (snake == playerSnake
                        && secondSnakeEnabled
                        && playerSnake2.getBody().contains(newHead))
                || (snake == playerSnake2 && playerSnake.getBody().contains(newHead))) {
            gameOver = true;
            return;
        }

        // Проверка столкновения с двигающимся препятствием по всей длине
        boolean collisionWithMovingObstacle =
                obstacles.stream()
                        .filter(o -> !o.isStatic() && o instanceof MovingObstacle)
                        .flatMap(o -> ((MovingObstacle) o).getOccupiedPoints().stream())
                        .anyMatch(p -> p.equals(newHead) || snake.getBody().contains(p));

        if (collisionWithMovingObstacle) {
            snake.shrink();

            if (snake.getBody().isEmpty() && snake == playerSnake) {
                gameOver = true;
            }
            return;
        }

        if (obstacles.stream().anyMatch(o -> o.getPosition().equals(newHead))) {
            gameOver = true;
            return;
        }

        snake.move();

        Optional<Food> eatenFood =
                foods.stream().filter(f -> f.getPosition().equals(snake.getHead())).findFirst();
        eatenFood.ifPresent(
                food -> {
                    snake.grow();
                    snake.incrementFruitsEaten();
                    foods.remove(food);
                    addFood();
                });
    }

    public void updateEnemySnakes() {
        if (gameOver || gameWon) return;

        List<EnemySnake> enemySnakesCopy = new ArrayList<>(enemySnakes);

        enemySnakesCopy.forEach(
                enemy -> {
                    foods.stream()
                            .filter(food -> food.getPosition().equals(enemy.getHead()))
                            .findFirst()
                            .ifPresent(
                                    food -> {
                                        enemy.grow();
                                        enemy.incrementFruitsEaten();
                                        foods.remove(food);
                                        addFood();
                                    });
                });

        enemySnakesCopy.forEach(
                enemy -> {
                    enemy.updateDirection(this);
                    enemy.move();
                    checkEnemyCollisions(enemy);
                });
    }

    private void checkEnemyCollisions(EnemySnake enemy) {
        enemyCollisionHandler.checkCollisions(
                enemy,
                playerSnake,
                playerSnake2,
                obstacles,
                enemySnakes,
                secondSnakeEnabled,
                fieldManager.getFreeCells(),
                this);
    }

    private boolean isOutOfBounds(Point p) {
        return p.x < 0 || p.x >= width || p.y < 0 || p.y >= height;
    }

    public void reset(int width, int height) {
        this.width = width;
        this.height = height;
        this.playerSnake = new Snake(new Point(width / 2, height / 2), 200);
        if (secondSnakeEnabled) {
            this.playerSnake2 = new Snake(new Point(width / 4, height / 4), 200);
        }
        this.foods.clear();
        IntStream.range(0, 3).forEach(i -> addFood());
        IntStream.range(0, 3).forEach(i -> addObstacle());
        this.gameOver = false;
        this.gameWon = false;
    }

    public Snake getPlayerSnake() {
        return playerSnake;
    }

    public Snake getPlayerSnake2() {
        return playerSnake2;
    }

    public List<Food> getFoods() {
        return Collections.unmodifiableList(foods);
    }

    public boolean isGameOver() {
        return gameOver;
    }

    public boolean isGameWon() {
        return gameWon;
    }

    public List<Obstacle> getObstacles() {
        return Collections.unmodifiableList(obstacles);
    }

    public List<EnemySnake> getEnemySnakes() {
        return Collections.unmodifiableList(enemySnakes);
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getObstacleCount() {
        return obstacleCount;
    }

    public boolean isSecondSnakeEnabled() {
        return secondSnakeEnabled;
    }

    public void setGameOver(boolean gameOver) {
        this.gameOver = gameOver;
    }

    public FieldManager getFieldManager() {
        return fieldManager;
    }
}
