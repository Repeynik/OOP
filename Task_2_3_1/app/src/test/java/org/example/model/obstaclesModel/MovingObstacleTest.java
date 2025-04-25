package org.example.model.obstaclesModel;

import static org.junit.jupiter.api.Assertions.*;

import org.example.model.GameModel;
import org.example.model.additionalModels.Point;
import org.example.model.configModel.LevelConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

class MovingObstacleTest {
    private MovingObstacle movingObstacle;

    @BeforeEach
    void setUp() {
        movingObstacle = new MovingObstacle(new Point(5, 5), 20, 20);
    }

    @Test
    void testMovingObstaclePosition() {
        assertEquals(new Point(5, 5), movingObstacle.getPosition());
    }

    @Test
    void testMovingObstacleIsNotStatic() {
        assertFalse(movingObstacle.isStatic());
    }

    @Test
    void testMovingObstacleUpdate() {
        LevelConfig currentLevelConfig =
                new LevelConfig(1, 3, 3, 10, 200, 10, true, "Smart", false);

        GameModel model = new GameModel(20, 20, currentLevelConfig);
        movingObstacle.update(model);
        assertNotNull(movingObstacle.getPosition());
    }

    @Test
    void testGetOccupiedPoints() {
        List<Point> occupiedPoints = movingObstacle.getOccupiedPoints();
        assertNotNull(occupiedPoints);
        assertFalse(occupiedPoints.isEmpty());
    }
}
