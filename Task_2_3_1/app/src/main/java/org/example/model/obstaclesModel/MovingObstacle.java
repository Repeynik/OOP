package org.example.model.obstaclesModel;

import org.example.model.GameModel;
import org.example.model.additionalModels.Direction;
import org.example.model.additionalModels.Point;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MovingObstacle extends Obstacle {
    private Direction direction;
    private final Random rand = new Random();
    private final int gameWidth;
    private final int gameHeight;
    private final int length;

    public MovingObstacle(Point position, int gameWidth, int gameHeight) {
        super(position);
        this.gameWidth = gameWidth;
        this.gameHeight = gameHeight;
        this.direction = Direction.values()[rand.nextInt(4)];

        this.length = rand.nextInt(3) + 1;
    }

    @Override
    public void update(GameModel model) {
        for (int i = 0; i < length; i++) {
            Point nextPosition = new Point(position.x + direction.dx, position.y + direction.dy);

            if (isPositionBlocked(model, nextPosition)) {
                direction = direction.opposite();
                break;
            } else {
                position = nextPosition;
            }

            if (isOutOfBounds()) {
                adjustDirectionForBounds();
                break;
            }
        }
    }

    private boolean isPositionBlocked(GameModel model, Point nextPosition) {
        return model.getObstacles().stream()
                        .anyMatch(obstacle -> obstacle.getPosition().equals(nextPosition))
                || model.getFoods().stream()
                        .anyMatch(food -> food.getPosition().equals(nextPosition));
    }

    private boolean isOutOfBounds() {
        return position.x <= 0
                || position.x >= gameWidth - 1
                || position.y <= 0
                || position.y >= gameHeight - 1;
    }

    private void adjustDirectionForBounds() {
        if (position.x <= 0 || position.x >= gameWidth - 1) {
            direction = direction.isHorizontal() ? direction.opposite() : direction;
        }
        if (position.y <= 0 || position.y >= gameHeight - 1) {
            direction = direction.isVertical() ? direction.opposite() : direction;
        }
    }

    @Override
    public boolean isStatic() {
        return false;
    }

    public List<Point> getOccupiedPoints() {
        List<Point> occupiedPoints = new ArrayList<>();
        for (int i = 0; i < length; i++) {
            occupiedPoints.add(
                    new Point(position.x + i * direction.dx, position.y + i * direction.dy));
        }
        return occupiedPoints;
    }
}
