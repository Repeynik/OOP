package org.example.model.managers;

import org.example.model.additionalModels.Food;
import org.example.model.additionalModels.Point;
import org.example.model.obstaclesModel.Obstacle;
import org.example.model.snakesModel.EnemySnake;
import org.example.model.snakesModel.Snake;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class FieldManager {
    private final int width;
    private final int height;
    private final Set<Point> freeCells = new HashSet<>();

    public FieldManager(int width, int height) {
        this.width = width;
        this.height = height;
        initializeFreeCells();
    }

    private void initializeFreeCells() {
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                freeCells.add(new Point(x, y));
            }
        }
    }

    public void updateOccupiedCells(
            Snake playerSnake,
            Snake playerSnake2,
            List<Food> foods,
            List<Obstacle> obstacles,
            List<EnemySnake> enemySnakes,
            boolean secondSnakeEnabled) {
        // Очищаем список свободных клеток
        freeCells.clear();
        initializeFreeCells();

        // Удаляем занятые клетки
        playerSnake.getBody().forEach(freeCells::remove);
        if (secondSnakeEnabled) {
            playerSnake2.getBody().forEach(freeCells::remove);
        }
        foods.forEach(food -> freeCells.remove(food.getPosition()));
        obstacles.forEach(obstacle -> freeCells.remove(obstacle.getPosition()));
        enemySnakes.forEach(enemy -> enemy.getBody().forEach(freeCells::remove));
    }

    public Set<Point> getFreeCells() {
        return new HashSet<>(freeCells);
    }
}
