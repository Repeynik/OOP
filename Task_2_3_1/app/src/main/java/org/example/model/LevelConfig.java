package org.example.model;

public class LevelConfig {
    private final int levelNumber;
    private final int enemyCount;
    private final int obstacleCount;
    private final int snakeSpeed;
    private final int gameSpeed;
    private final int targetLength;
    private final boolean accelerationEnabled;
    private final String behavior;
    private final boolean secondSnakeEnabled;

    public LevelConfig() {
        this.levelNumber = 1;
        this.enemyCount = 0;
        this.obstacleCount = 0;
        this.snakeSpeed = 200;
        this.gameSpeed = 200;
        this.targetLength = 10;
        this.accelerationEnabled = false;
        this.behavior = "Random";
        this.secondSnakeEnabled = false;
    }

    public LevelConfig(
            int levelNumber,
            int enemyCount,
            int obstacleCount,
            int snakeSpeed,
            int gameSpeed,
            int targetLength,
            boolean accelerationEnabled,
            String behavior,
            boolean secondSnakeEnabled) {
        this.levelNumber = levelNumber;
        this.enemyCount = enemyCount;
        this.obstacleCount = obstacleCount;
        this.snakeSpeed = snakeSpeed;
        this.gameSpeed = gameSpeed;
        this.targetLength = targetLength;
        this.accelerationEnabled = accelerationEnabled;
        this.behavior = behavior;
        this.secondSnakeEnabled = secondSnakeEnabled;
    }

    public int getLevelNumber() {
        return levelNumber;
    }

    public int getEnemyCount() {
        return enemyCount;
    }

    public int getObstacleCount() {
        return obstacleCount;
    }

    public int getSnakeSpeed() {
        return snakeSpeed;
    }

    public int getGameSpeed() {
        return gameSpeed;
    }

    public int getTargetLength() {
        return targetLength;
    }

    public boolean isAccelerationEnabled() {
        return accelerationEnabled;
    }

    public String getBehavior() {
        return behavior;
    }

    public boolean isSecondSnakeEnabled() {
        return secondSnakeEnabled;
    }
}
