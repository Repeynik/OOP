package org.example.model.snakesModel;

import org.example.model.additionalModels.Direction;
import org.example.model.additionalModels.Point;

import java.util.ArrayList;
import java.util.List;

public class Snake {
    private List<Point> body;
    private Direction direction;
    private boolean growNextMove = false;
    private int initialSpeed = 200;
    private int speed;
    private long lastMoveTime = System.currentTimeMillis();
    private int fruitsEaten = 0;
    private boolean accelerationEnabled = false;
    private boolean alive = true;

    public Snake(Point startPosition, int initialSpeed) {
        this.initialSpeed = initialSpeed;
        body = new ArrayList<>();
        body.add(startPosition);
        direction = Direction.RIGHT;
        alive = true;
    }

    public void setDirection(Direction newDirection) {
        if (direction.isOpposite(newDirection) || isTurningBack(newDirection)) return;
        direction = newDirection;
    }

    private boolean isTurningBack(Direction newDirection) {
        Point head = getHead();
        Point nextPosition = new Point(head.x + newDirection.dx, head.y + newDirection.dy);
        return body.size() > 1 && nextPosition.equals(body.get(1));
    }

    public void shrink() {
        body.remove(body.size() - 1);
    }

    public void shrinkAll() {
        body.clear();
    }

    public void enableAcceleration(boolean enabled) {
        this.accelerationEnabled = enabled;
    }

    public void move() {
        long currentTime = System.currentTimeMillis();
        if (currentTime - lastMoveTime < speed) {
            return;
        }
        lastMoveTime = currentTime;

        Point head = getHead();
        Point newHead = new Point(head.x + direction.dx, head.y + direction.dy);
        body.add(0, newHead);
        if (!growNextMove) {
            body.remove(body.size() - 1);
        } else {
            growNextMove = false;
        }

        if (accelerationEnabled && fruitsEaten > 0) {
            speed = Math.max(50, initialSpeed - (fruitsEaten * 10));
        }
    }

    public void grow() {
        growNextMove = true;
    }

    public void incrementFruitsEaten() {
        fruitsEaten++;
    }

    public Point getHead() {
        return body.get(0);
    }

    public Point getTail() {
        return body.get(body.size() - 1);
    }

    public List<Point> getBody() {
        return new ArrayList<>(body);
    }

    public boolean isSelfCollision() {
        Point head = getHead();
        for (int i = 1; i < body.size(); i++) {
            if (head.equals(body.get(i))) return true;
        }
        return false;
    }

    public Direction getDirection() {
        return direction;
    }

    public int getInitialSpeed() {
        return initialSpeed;
    }

    public void setInitialSpeed(int initialSpeed) {
        this.initialSpeed = initialSpeed;
    }

    public boolean getAlive() {
        return this.alive;
    }

    public void setAlive(boolean alive) {
        this.alive = alive;
    }
}
