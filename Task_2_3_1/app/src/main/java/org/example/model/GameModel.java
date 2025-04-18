package org.example.model;

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
    private Set<Point> freeCells;

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
        this.freeCells = new HashSet<>();
        this.behavior = config.getBehavior();

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                freeCells.add(new Point(x, y));
            }
        }
        obstacleCount = config.getObstacleCount();
        updateOccupiedCells();
        IntStream.range(0, config.getObstacleCount()).forEach(i -> addObstacle());
        IntStream.range(0, config.getEnemyCount()).forEach(i -> addEnemy());
        IntStream.range(0, 3).forEach(i -> addFood());
    }

    private void addObstacle() {
        updateOccupiedCells();

        if (!freeCells.isEmpty()) {
            List<Point> freeCellsList = new ArrayList<>(freeCells);
            Point startPoint = freeCellsList.get(rand.nextInt(freeCellsList.size()));

            if (rand.nextBoolean()) {
                obstacles.add(new MovingObstacle(startPoint, width, height));
            } else {
                int obstacleSize = rand.nextInt(5) + 1;
                for (int i = 0; i < obstacleSize; i++) {
                    Point newPoint = new Point(startPoint.x + i, startPoint.y);
                    if (freeCells.contains(newPoint)) {
                        obstacles.add(new StaticWall(newPoint));
                        freeCells.remove(newPoint);
                    } else {
                        break;
                    }
                }
            }
        }
    }

    private void addEnemy() {
        updateOccupiedCells();

        if (!freeCells.isEmpty()) {
            List<Point> freeCellsList = new ArrayList<>(freeCells);
            Point newPoint = freeCellsList.get(rand.nextInt(freeCellsList.size()));
            enemySnakes.add(new EnemySnake(newPoint, 200, this.behavior));
        }
    }

    private void updateOccupiedCells() {
        freeCells.clear();

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                freeCells.add(new Point(x, y));
            }
        }

        playerSnake.getBody().forEach(freeCells::remove);
        if (secondSnakeEnabled) {
            playerSnake2.getBody().forEach(freeCells::remove);
        }
        foods.forEach(food -> freeCells.remove(food.getPosition()));
        obstacles.forEach(obstacle -> freeCells.remove(obstacle.getPosition()));
        enemySnakes.forEach(enemy -> enemy.getBody().forEach(freeCells::remove));
    }

    void addFood() {
        updateOccupiedCells();

        if (!freeCells.isEmpty()) {
            List<Point> freeCellsList = new ArrayList<>(freeCells);
            Point newPoint = freeCellsList.get(rand.nextInt(freeCellsList.size()));
            foods.add(new Food(newPoint));
        }
    }

    public void update() {
        if (gameOver || gameWon) return;

        obstacles.forEach(Obstacle::update);

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
                || obstacles.stream().anyMatch(o -> o.getPosition().equals(newHead))
                || enemySnakes.stream().anyMatch(e -> e.getBody().contains(newHead))
                || (snake == playerSnake
                        && secondSnakeEnabled
                        && playerSnake2.getBody().contains(newHead))
                || (snake == playerSnake2 && playerSnake.getBody().contains(newHead))) {
            gameOver = true;
            return;
        }

        if (obstacles.stream().anyMatch(o -> !o.isStatic() && o.getPosition().equals(newHead))) {
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

        enemySnakes.forEach(
                enemy -> {
                    enemy.updateDirection(this);
                    enemy.move();
                    checkEnemyCollisions(enemy);
                });

        enemySnakes.forEach(
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
    }

    private void checkEnemyCollisions(EnemySnake enemy) {
        Point enemyHead = enemy.getHead();

        if (obstacles.stream().anyMatch(o -> o.getPosition().equals(enemyHead))) {
            enemySnakes.remove(enemy);
            return;
        }

        enemySnakes.forEach(
                other -> {
                    if (other != enemy && other.getBody().contains(enemyHead)) {
                        enemySnakes.remove(enemy);
                    }
                });

        if (playerSnake.getBody().contains(enemyHead)
                || (secondSnakeEnabled && playerSnake2.getBody().contains(enemyHead))) {
            enemySnakes.remove(enemy);
        }

        if (enemyHead.equals(playerSnake.getHead())
                || (secondSnakeEnabled && enemyHead.equals(playerSnake2.getHead()))) {
            gameOver = true;
            return;
        }

        if (enemy.isSelfCollision()) {
            enemySnakes.remove(enemy);
        }

        if (isOutOfBounds(enemyHead)) {
            enemySnakes.remove(enemy);
        }
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
}
