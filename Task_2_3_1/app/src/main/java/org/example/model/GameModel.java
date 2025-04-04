package org.example.model;

import java.util.*;
import java.util.stream.IntStream;

public class GameModel {
    private int width;
    private int height;
    private Snake playerSnake;
    private List<Food> foods;
    private int targetLength;
    private boolean gameOver;
    private boolean gameWon;
    private List<Obstacle> obstacles = new ArrayList<>();
    private static final int STATIC_WALLS = 3;
    private static final int MOVING_OBSTACLES = 2;
    private Random rand = new Random();

    private List<EnemySnake> enemySnakes = new ArrayList<>();

    public GameModel(int width, int height, int initialFoodCount, int targetLength) {
        this.width = width;
        this.height = height;
        this.targetLength = targetLength;
        this.playerSnake = new Snake(new Point(width / 2, height / 2));
        this.foods = new ArrayList<>();
        IntStream.range(0, initialFoodCount).forEach(i -> addFood());
        generateObstacles();

        enemySnakes.add(new EnemySnake(new Point(5, 5), new EnemySnake.ChaseAI()));
        enemySnakes.add(
                new EnemySnake(new Point(width - 5, height - 5), new EnemySnake.RandomAI()));
    }

    private void generateObstacles() {
        obstacles.clear();

        for (int i = 0; i < STATIC_WALLS; i++) {
            Point pos;
            do {
                pos = new Point(rand.nextInt(width - 5) + 2, rand.nextInt(height - 5) + 2);
            } while (isOccupied(pos));

            for (int j = -1; j <= 1; j++) {
                obstacles.add(new StaticWall(new Point(pos.x + j, pos.y)));
            }
        }
    }

    private void addFood() {
        Point newPoint;
        do {
            newPoint = new Point(rand.nextInt(width), rand.nextInt(height));
        } while (isOccupied(newPoint));
        foods.add(new Food(newPoint));
    }

    private boolean isOccupied(Point point) {
        return playerSnake.getBody().contains(point)
                || foods.stream().anyMatch(f -> f.getPosition().equals(point))
                || obstacles.stream().anyMatch(o -> o.getPosition().equals(point));
    }

    public void update() {
        if (gameOver || gameWon) return;

        playerSnake.move();
        Point head = playerSnake.getHead();

        if (isOutOfBounds(head) || playerSnake.isSelfCollision() || obstacles.contains(head)) {
            gameOver = true;
            return;
        }

        obstacles.forEach(Obstacle::update);

        if (obstacles.stream().anyMatch(o -> o.getPosition().equals(head))) {
            gameOver = true;
        }

        enemySnakes.forEach(
                enemy -> {
                    enemy.updateDirection(this);
                    enemy.move();
                    checkEnemyCollisions(enemy);
                });

        Point playerHead = playerSnake.getHead();
        if (enemySnakes.stream().anyMatch(e -> e.getBody().contains(playerHead))) {
            gameOver = true;
        }

        Optional<Food> eatenFood =
                foods.stream().filter(f -> f.getPosition().equals(head)).findFirst();

        eatenFood.ifPresent(
                food -> {
                    playerSnake.grow();
                    foods.remove(food);
                    addFood();
                });

        if (playerSnake.getBody().size() >= targetLength) gameWon = true;
    }

    private void checkEnemyCollisions(EnemySnake enemy) {

        if (obstacles.stream().anyMatch(o -> o.getPosition().equals(enemy.getHead()))) {
            enemySnakes.remove(enemy);
            return;
        }

        enemySnakes.forEach(
                other -> {
                    if (other != enemy && other.getBody().contains(enemy.getHead())) {
                        enemySnakes.remove(enemy);
                    }
                });

        if (playerSnake.getBody().contains(enemy.getHead())) {
            enemySnakes.remove(enemy);
        }
        if (enemy.isSelfCollision()) {
            enemySnakes.remove(enemy);
        }
        if (isOutOfBounds(enemy.getHead())) {
            enemySnakes.remove(enemy);
        }
    }

    private boolean isOutOfBounds(Point p) {
        return p.x < 0 || p.x >= width || p.y < 0 || p.y >= height;
    }

    public void reset(int width, int height) {
        this.width = width;
        this.height = height;
        this.playerSnake = new Snake(new Point(width / 2, height / 2));
        this.foods.clear();
        IntStream.range(0, 3).forEach(i -> addFood());
        generateObstacles();
        this.gameOver = false;
        this.gameWon = false;
    }

    public Snake getPlayerSnake() {
        return playerSnake;
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
}
