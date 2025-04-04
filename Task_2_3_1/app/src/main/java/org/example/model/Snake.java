package org.example.model;

import java.util.ArrayList;
import java.util.List;

public class Snake {
    private List<Point> body;
    private Direction direction;
    private boolean growNextMove = false;

    public Snake(Point startPosition) {
        body = new ArrayList<>();
        body.add(startPosition);
        direction = Direction.RIGHT;
    }

    public void setDirection(Direction newDirection) {
        if (direction.isOpposite(newDirection)) return;
        direction = newDirection;
    }

    public void move() {
        Point head = getHead();
        Point newHead = new Point(head.x + direction.dx, head.y + direction.dy);
        body.add(0, newHead);
        if (!growNextMove) {
            body.remove(body.size() - 1);
        } else {
            growNextMove = false;
        }
    }

    public void grow() {
        growNextMove = true;
    }

    public Point getHead() {
        return body.get(0);
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
}
