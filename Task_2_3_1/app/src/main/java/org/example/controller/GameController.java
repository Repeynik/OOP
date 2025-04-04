package org.example.controller;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.util.Duration;

import org.example.model.Direction;
import org.example.model.GameModel;
import org.example.model.Point;

public class GameController {
    @FXML private Canvas gameCanvas;
    @FXML private Label scoreLabel;
    @FXML private VBox gameOverGroup;
    @FXML private VBox victoryGroup;

    private GameModel gameModel;
    private Timeline gameLoop;
    private static final int CELL_SIZE = 20;

    @FXML
    public void initialize() {
        gameModel =
                new GameModel(
                        (int) (gameCanvas.getWidth() / CELL_SIZE),
                        (int) (gameCanvas.getHeight() / CELL_SIZE),
                        3,
                        10);

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
                                gc.setFill(Color.ORANGERED); 
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
    }

    private void resetGame() {
        gameModel =
                new GameModel(
                        (int) (gameCanvas.getWidth() / CELL_SIZE),
                        (int) (gameCanvas.getHeight() / CELL_SIZE),
                        3,
                        10);
        gameOverGroup.setVisible(false);
        victoryGroup.setVisible(false);
        gameLoop.play();
        gameCanvas.requestFocus();
    }

    @FXML
    private void handleRestart(ActionEvent event) {
        resetGame();
    }
}
