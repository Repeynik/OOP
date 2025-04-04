package org.example.model;

public enum Direction {
    UP(0, -1),
    DOWN(0, 1),
    LEFT(-1, 0),
    RIGHT(1, 0);

    public final int dx;
    public final int dy;

    Direction(int dx, int dy) {
        this.dx = dx;
        this.dy = dy;
    }

    public boolean isOpposite(Direction other) {
        return this.dx == -other.dx && this.dy == -other.dy;
    }

    boolean isHorizontal() {
        if (this == LEFT || this == RIGHT) {
            return true;
        }
        return false;
    }

    boolean isVertical() {
        if (this == UP || this == DOWN) {
            return true;
        }
        return false;
    }

    Direction opposite() {
        switch (this) {
            case UP:
                return DOWN;
            case DOWN:
                return UP;
            case LEFT:
                return RIGHT;
            case RIGHT:
                return LEFT;
            default:
                throw new IllegalStateException("Unexpected value: " + this);
        }
    }
}
