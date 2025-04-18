package org.example.model;

public class StaticWall extends Obstacle {
    public StaticWall(Point position) {
        super(position);
    }

    @Override
    public void update() {}

    @Override
    public boolean isStatic() {
        return true;
    }
}
