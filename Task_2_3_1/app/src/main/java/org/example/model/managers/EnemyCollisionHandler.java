package org.example.model.managers;

import org.example.model.GameModel;
import org.example.model.additionalModels.Point;
import org.example.model.obstaclesModel.MovingObstacle;
import org.example.model.obstaclesModel.Obstacle;
import org.example.model.snakesModel.EnemySnake;
import org.example.model.snakesModel.Snake;

import java.util.List;
import java.util.Set;

public class EnemyCollisionHandler {

    public void checkCollisions(
            EnemySnake enemy,
            Snake playerSnake,
            Snake playerSnake2,
            List<Obstacle> obstacles,
            List<EnemySnake> enemySnakes,
            boolean secondSnakeEnabled,
            Set<Point> freeCells,
            GameModel gameModel) {
        Point enemyHead = enemy.getHead();

        if (checkCollisionWithMovingObstacles(enemy, enemyHead, obstacles, enemySnakes)) return;
        if (checkCollisionWithStaticObstacles(enemyHead, obstacles, enemySnakes)) return;
        if (checkCollisionWithOtherSnakes(enemy, enemyHead, enemySnakes)) return;
        if (checkCollisionWithPlayers(
                enemy,
                enemyHead,
                playerSnake,
                playerSnake2,
                secondSnakeEnabled,
                enemySnakes,
                gameModel)) return;
        if (checkSelfCollision(enemy, enemySnakes)) return;
        if (checkOutOfBounds(enemyHead, freeCells, enemySnakes)) return;
    }

    private boolean checkCollisionWithMovingObstacles(
            EnemySnake enemy,
            Point enemyHead,
            List<Obstacle> obstacles,
            List<EnemySnake> enemySnakes) {
        boolean collisionWithMovingObstacle =
                obstacles.stream()
                        .filter(o -> !o.isStatic() && o instanceof MovingObstacle)
                        .flatMap(o -> ((MovingObstacle) o).getOccupiedPoints().stream())
                        .anyMatch(p -> p.equals(enemyHead) || enemy.getBody().contains(p));

        if (collisionWithMovingObstacle) {
            enemy.shrink();
            if (enemy.getBody().isEmpty()) {
                enemySnakes.remove(enemy);
            }
            return true;
        }
        return false;
    }

    private boolean checkCollisionWithStaticObstacles(
            Point enemyHead, List<Obstacle> obstacles, List<EnemySnake> enemySnakes) {
        if (obstacles.stream().anyMatch(o -> o.getPosition().equals(enemyHead))) {
            enemySnakes.removeIf(enemy -> enemy.getHead().equals(enemyHead));
            return true;
        }
        return false;
    }

    private boolean checkCollisionWithOtherSnakes(
            EnemySnake enemy, Point enemyHead, List<EnemySnake> enemySnakes) {
        for (EnemySnake other : enemySnakes) {
            if (other != enemy && other.getBody().contains(enemyHead)) {
                enemySnakes.remove(enemy);
                return true;
            }
        }
        return false;
    }

    private boolean checkCollisionWithPlayers(
            EnemySnake enemy,
            Point enemyHead,
            Snake playerSnake,
            Snake playerSnake2,
            boolean secondSnakeEnabled,
            List<EnemySnake> enemySnakes,
            GameModel gameModel) {
        if (playerSnake.getBody().contains(enemyHead)
                || (secondSnakeEnabled && playerSnake2.getBody().contains(enemyHead))) {
            enemySnakes.remove(enemy);
            return true;
        }

        if (enemyHead.equals(playerSnake.getHead())
                || (secondSnakeEnabled && enemyHead.equals(playerSnake2.getHead()))) {
            gameModel.setGameOver(true);
            return true;
        }
        return false;
    }

    private boolean checkSelfCollision(EnemySnake enemy, List<EnemySnake> enemySnakes) {
        if (enemy.isSelfCollision()) {
            enemySnakes.remove(enemy);
            return true;
        }
        return false;
    }

    private boolean checkOutOfBounds(
            Point enemyHead, Set<Point> freeCells, List<EnemySnake> enemySnakes) {
        if (!freeCells.contains(enemyHead)) {
            enemySnakes.removeIf(enemy -> enemy.getHead().equals(enemyHead));
            return true;
        }
        return false;
    }
}
