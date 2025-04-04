package org.example.model;


public class LevelConfig {
    private final int levelNumber;
    private final int enemyCount;
    private final int obstacleCount;
    private final int snakeSpeed;
    private final int targetLength;

    public LevelConfig(int levelNumber, int enemyCount, 
                      int obstacleCount, int snakeSpeed, 
                      int targetLength) {
        this.levelNumber = levelNumber;
        this.enemyCount = enemyCount;
        this.obstacleCount = obstacleCount;
        this.snakeSpeed = snakeSpeed;
        this.targetLength = targetLength;
    }

    // Геттеры
    public int getLevelNumber() { return levelNumber; }
    public int getEnemyCount() { return enemyCount; }
    public int getObstacleCount() { return obstacleCount; }
    public int getSnakeSpeed() { return snakeSpeed; }
    public int getTargetLength() { return targetLength; }
}