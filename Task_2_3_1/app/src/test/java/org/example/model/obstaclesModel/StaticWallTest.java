package org.example.model.obstaclesModel;

import static org.junit.jupiter.api.Assertions.*;

import org.example.model.GameModel;
import org.example.model.additionalModels.Point;
import org.junit.jupiter.api.Test;

class StaticWallTest {
    @Test
    void testStaticWallPosition() {
        Point position = new Point(5, 5);
        StaticWall wall = new StaticWall(position);
        assertEquals(position, wall.getPosition());
    }

    @Test
    void testStaticWallIsStatic() {
        StaticWall wall = new StaticWall(new Point(5, 5));
        assertTrue(wall.isStatic());
    }

    @Test
    void testStaticWallUpdate() {
        StaticWall wall = new StaticWall(new Point(5, 5));
        GameModel model = null;
        wall.update(model);
    }
}
