package org.example.model.snakesModel;

import org.example.model.AiStrategies.ChaseAI;
import org.example.model.AiStrategies.RandomAI;
import org.example.model.AiStrategies.SmartAI;
import org.example.model.GameModel;
import org.example.model.additionalModels.Direction;
import org.example.model.additionalModels.Point;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class EnemySnake extends Snake {
    public final Random rand = new Random();
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

    public Set<Direction> getNearEdges(Point position, int gameWidth, int gameHeight) {
        Set<Direction> edges = new HashSet<>();
        if (position.x <= 3) edges.add(Direction.LEFT);
        if (position.x >= gameWidth - 3) edges.add(Direction.RIGHT);
        if (position.y <= 3) edges.add(Direction.UP);
        if (position.y >= gameHeight - 3) edges.add(Direction.DOWN);
        return edges;
    }
}
