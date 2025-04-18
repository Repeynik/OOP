package org.example.controller;

import com.fasterxml.jackson.databind.ObjectMapper;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;

import org.example.model.Direction;
import org.example.model.GameModel;
import org.example.model.LevelConfig;
import org.example.model.Point;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class GameController {
    private static GameController instance;
    @FXML private Canvas gameCanvas;
    @FXML private Label scoreLabel;
    @FXML private Label obstaclesLabel;
    @FXML private VBox gameOverGroup;
    @FXML private VBox victoryGroup;

    private GameModel gameModel;
    private Timeline gameLoop;
    private static final int CELL_SIZE = 20;

    private LevelConfig currentLevelConfig =
            new LevelConfig(1, 3, 3, 10, 200, 10, true, "Smart", false);

    private LevelConfig nextLevelConfig;

    public LevelConfig getCurrentLevelConfig() {
        return currentLevelConfig;
    }

    public void applyLevelConfig(LevelConfig config) {
        this.currentLevelConfig = config;
        gameModel =
                new GameModel(
                        (int) (gameCanvas.getWidth() / CELL_SIZE),
                        (int) (gameCanvas.getHeight() / CELL_SIZE),
                        config);
        gameLoop.setRate(config.getGameSpeed());
        resetGame();
    }

    public void setNextLevelConfig(LevelConfig config) {
        this.nextLevelConfig = config;
    }

    public LevelConfig getNextLevelConfig() {
        return nextLevelConfig;
    }

    @FXML
    private void initialize() {
        if (gameCanvas == null) {
            throw new IllegalStateException(
                    "Canvas 'gameCanvas' is not properly injected. Check your FXML file.");
        }

        gameCanvas.setFocusTraversable(true);

        gameModel =
                new GameModel(
                        (int) (gameCanvas.getWidth() / CELL_SIZE),
                        (int) (gameCanvas.getHeight() / CELL_SIZE),
                        currentLevelConfig);

        setupGameLoop();
        setupKeyHandlers();
    }

    private void setupGameLoop() {
        gameLoop = new Timeline(new KeyFrame(Duration.millis(200), e -> updateGame()));
        gameLoop.setCycleCount(Timeline.INDEFINITE);
        gameLoop.play();
    }

    private void setupKeyHandlers() {
        gameCanvas.setOnKeyPressed(
                e -> {
                    if (gameModel.isGameOver() || gameModel.isGameWon()) {
                        if (e.getCode() == KeyCode.SPACE || e.getCode() == KeyCode.ENTER) {
                            resetGame();
                        }
                    } else {
                        handleKeyPress(e);
                        gameCanvas.setFocusTraversable(true);
                    }
                });
    }

    private void updateGame() {
        if (gameModel.isGameOver() || gameModel.isGameWon()) {
            gameLoop.stop();
            if (gameModel.isGameOver()) {
                gameOverGroup.setVisible(true);
                victoryGroup.setVisible(false);
            } else {
                victoryGroup.setVisible(true);
                gameOverGroup.setVisible(false);
            }

            return;
        }

        gameModel.update();
        draw();
        updateScore();
        updateObstacles();
    }

    private void draw() {
        GraphicsContext gc = gameCanvas.getGraphicsContext2D();
        gc.setFill(Color.BLACK);
        gc.fillRect(0, 0, gameCanvas.getWidth(), gameCanvas.getHeight());

        gc.setFill(Color.GRAY);
        gameModel
                .getObstacles()
                .forEach(
                        obstacle -> {
                            Point p = obstacle.getPosition();
                            if (obstacle.isStatic()) {
                                gc.setFill(Color.DARKGRAY);
                                gc.fillRect(p.x * CELL_SIZE, p.y * CELL_SIZE, CELL_SIZE, CELL_SIZE);
                            } else {
                                gc.setFill(Color.YELLOW);
                                gc.fillOval(p.x * CELL_SIZE, p.y * CELL_SIZE, CELL_SIZE, CELL_SIZE);
                            }
                        });

        gc.setFill(Color.GREEN);
        gameModel
                .getPlayerSnake()
                .getBody()
                .forEach(
                        p ->
                                gc.fillRect(
                                        p.x * CELL_SIZE,
                                        p.y * CELL_SIZE,
                                        CELL_SIZE - 1,
                                        CELL_SIZE - 1));

        if (gameModel.isSecondSnakeEnabled()) {
            gc.setFill(Color.ORANGE);
            gameModel
                    .getPlayerSnake2()
                    .getBody()
                    .forEach(
                            p ->
                                    gc.fillRect(
                                            p.x * CELL_SIZE,
                                            p.y * CELL_SIZE,
                                            CELL_SIZE - 1,
                                            CELL_SIZE - 1));
        }

        gc.setFill(Color.RED);
        gameModel
                .getFoods()
                .forEach(
                        food -> {
                            Point p = food.getPosition();
                            gc.fillOval(
                                    p.x * CELL_SIZE, p.y * CELL_SIZE, CELL_SIZE - 1, CELL_SIZE - 1);
                        });

        gameModel
                .getEnemySnakes()
                .forEach(
                        enemy -> {
                            gc.setFill(Color.PURPLE);
                            Point head = enemy.getHead();
                            gc.fillRect(
                                    head.x * CELL_SIZE, head.y * CELL_SIZE, CELL_SIZE, CELL_SIZE);

                            gc.setFill(Color.BLUE);
                            enemy.getBody().stream()
                                    .skip(1)
                                    .forEach(
                                            p ->
                                                    gc.fillRect(
                                                            p.x * CELL_SIZE,
                                                            p.y * CELL_SIZE,
                                                            CELL_SIZE - 1,
                                                            CELL_SIZE - 1));
                        });
    }

    private void updateScore() {
        scoreLabel.setText("Score: " + (gameModel.getPlayerSnake().getBody().size() - 1));
    }

    private void updateObstacles() {
        obstaclesLabel.setText("Obstacles: " + gameModel.getObstacleCount());
    }

    private void handleKeyPress(KeyEvent event) {
        if (gameModel.isGameOver() || gameModel.isGameWon()) {
            if (event.getCode() == KeyCode.SPACE) {
                resetGame();
            }
            return;
        }

        switch (event.getCode()) {
            case UP:
                gameModel.getPlayerSnake().setDirection(Direction.UP);
                break;
            case DOWN:
                gameModel.getPlayerSnake().setDirection(Direction.DOWN);
                break;
            case LEFT:
                gameModel.getPlayerSnake().setDirection(Direction.LEFT);
                break;
            case RIGHT:
                gameModel.getPlayerSnake().setDirection(Direction.RIGHT);
                break;
        }

        if (gameModel.isSecondSnakeEnabled()) {
            switch (event.getCode()) {
                case W:
                    gameModel.getPlayerSnake2().setDirection(Direction.UP);
                    break;
                case S:
                    gameModel.getPlayerSnake2().setDirection(Direction.DOWN);
                    break;
                case A:
                    gameModel.getPlayerSnake2().setDirection(Direction.LEFT);
                    break;
                case D:
                    gameModel.getPlayerSnake2().setDirection(Direction.RIGHT);
                    break;
            }
        }
    }

    private void resetGame() {
        gameModel =
                new GameModel(
                        (int) (gameCanvas.getWidth() / CELL_SIZE),
                        (int) (gameCanvas.getHeight() / CELL_SIZE),
                        currentLevelConfig);
        gameOverGroup.setVisible(false);
        victoryGroup.setVisible(false);
        gameLoop.play();
        gameCanvas.requestFocus();
    }

    private void showSuccessMessage() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Settings Applied");
        alert.setHeaderText(null);
        alert.setContentText("Level settings have been successfully applied!");
        alert.showAndWait();
    }

    @FXML
    private void handleRestart(ActionEvent event) {
        if (nextLevelConfig != null) {
            applyLevelConfig(nextLevelConfig);
            nextLevelConfig = null;
        } else {
            resetGame();
        }
        gameLoop.stop();
        gameLoop.play();
    }

    @FXML
    private void loadSavedLevel() {
        List<LevelConfig> levels = LevelEditorController.getSavedLevels();
        if (!levels.isEmpty()) {
            LevelConfig selectedLevel = levels.get(levels.size() - 1);
            gameModel =
                    new GameModel(
                            (int) (gameCanvas.getWidth() / CELL_SIZE),
                            (int) (gameCanvas.getHeight() / CELL_SIZE),
                            selectedLevel);
            gameLoop.setRate(selectedLevel.getGameSpeed());
            resetGame();
            showSuccessMessage();
        }
    }

    @FXML
    private void openLevelEditor() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/level_editor.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Level Editor");
            stage.setScene(new Scene(root, 600, 400));
            stage.setResizable(false);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loadConfigFromJson(String filePath) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            LevelConfig config = objectMapper.readValue(new File(filePath), LevelConfig.class);
            setNextLevelConfig(config);
            System.out.println("Configuration loaded successfully from " + filePath);
        } catch (IOException e) {
            System.err.println("Failed to load configuration: " + e.getMessage());
        }
    }

    public static void setInstance(GameController controller) {
        instance = controller;
    }

    public static GameController getInstance() {
        return instance;
    }
}
