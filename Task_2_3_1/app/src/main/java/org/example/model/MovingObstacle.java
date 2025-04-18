package org.example.model;

import java.util.Random;

public class MovingObstacle extends Obstacle {
    private Direction direction;
    private final Random rand = new Random();
    private final int gameWidth;
    private final int gameHeight;

    public MovingObstacle(Point position, int gameWidth, int gameHeight) {
        super(position);
        this.gameWidth = gameWidth;
        this.gameHeight = gameHeight;
        this.direction = Direction.values()[rand.nextInt(4)];
    }

    @Override
    public void update() {
        position = new Point(position.x + direction.dx, position.y + direction.dy);

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
}
