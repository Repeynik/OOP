package org.example.model.obstaclesModel;

import org.example.model.GameModel;
import org.example.model.additionalModels.Point;

public class StaticWall extends Obstacle {
    public StaticWall(Point position) {
        super(position);
    }

    @Override
    public void update(GameModel model) {
        //  стены не двигаются
    }

    @Override
    public boolean isStatic() {
        return true;
    }
}
