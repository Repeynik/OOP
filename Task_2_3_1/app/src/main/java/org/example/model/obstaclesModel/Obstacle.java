package org.example.model.obstaclesModel;

import org.example.model.GameModel;
import org.example.model.additionalModels.Point;

public abstract class Obstacle {
    protected Point position;

    protected Obstacle(Point position) {
        this.position = position;
    }

    public Point getPosition() {
        return position;
    }

    public abstract void update(GameModel model);

    public abstract boolean isStatic();
}
