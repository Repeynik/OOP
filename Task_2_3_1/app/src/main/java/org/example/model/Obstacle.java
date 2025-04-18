package org.example.model;

public abstract class Obstacle {
    protected Point position;

    public Obstacle(Point position) {
        this.position = position;
    }

    public Point getPosition() {
        return position;
    }

    public abstract void update();

    public abstract boolean isStatic();
}
