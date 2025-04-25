package org.example.controller;

import com.fasterxml.jackson.databind.ObjectMapper;

import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import org.example.model.configModel.LevelConfig;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class LevelEditorController {
    @FXML private TextField levelNumberField;
    @FXML private TextField enemyCountField;
    @FXML private TextField obstacleCountField;
    @FXML private TextField snakeSpeedField;
    @FXML private TextField gameSpeedField;
    @FXML private TextField targetLengthField;
    @FXML private CheckBox accelerationCheckBox;
    @FXML private ChoiceBox<String> snakeBehaviorChoiceBox;
    @FXML private CheckBox secondSnakeCheckBox;

    private static final List<LevelConfig> savedLevels = new ArrayList<>();

    @FXML
    public void initialize() {
        GameController gameController = GameController.getInstance();
        LevelConfig currentConfig = gameController.getCurrentLevelConfig();

        levelNumberField.setText(String.valueOf(currentConfig.getLevelNumber()));
        enemyCountField.setText(String.valueOf(currentConfig.getEnemyCount()));
        obstacleCountField.setText(String.valueOf(currentConfig.getObstacleCount()));
        gameSpeedField.setText(String.valueOf(currentConfig.getGameSpeed()));
        snakeSpeedField.setText(String.valueOf(currentConfig.getSnakeSpeed()));
        targetLengthField.setText(String.valueOf(currentConfig.getTargetLength()));
        accelerationCheckBox.setSelected(currentConfig.isAccelerationEnabled());

        snakeBehaviorChoiceBox.getItems().addAll("Random", "Chase", "Smart");
        snakeBehaviorChoiceBox.setValue("Smart");
    }

    @FXML
    private void handleSaveLevel() {
        try {
            int levelNumber = Integer.parseInt(levelNumberField.getText());
            int enemyCount = Integer.parseInt(enemyCountField.getText());
            int obstacleCount = Integer.parseInt(obstacleCountField.getText());
            int snakeSpeed = Integer.parseInt(snakeSpeedField.getText());
            int gameSpeed = Integer.parseInt(gameSpeedField.getText());
            int targetLength = Integer.parseInt(targetLengthField.getText());
            boolean accelerationEnabled = accelerationCheckBox.isSelected();
            String selectedBehavior = snakeBehaviorChoiceBox.getValue();
            boolean secondSnakeEnabled = secondSnakeCheckBox.isSelected();

            LevelConfig newLevel =
                    new LevelConfig(
                            levelNumber,
                            enemyCount,
                            obstacleCount,
                            snakeSpeed,
                            gameSpeed,
                            targetLength,
                            accelerationEnabled,
                            selectedBehavior,
                            secondSnakeEnabled);
            savedLevels.add(newLevel);

            GameController gameController = GameController.getInstance();
            gameController.setNextLevelConfig(newLevel);

            Stage stage = (Stage) levelNumberField.getScene().getWindow();
            stage.close();
        } catch (NumberFormatException e) {
            System.err.println("Invalid input! Please enter valid numbers.");
        }
    }

    @FXML
    private void handleCancel() {
        Stage stage = (Stage) levelNumberField.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void saveConfigToJson() {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            LevelConfig currentConfig =
                    new LevelConfig(
                            Integer.parseInt(levelNumberField.getText()),
                            Integer.parseInt(enemyCountField.getText()),
                            Integer.parseInt(obstacleCountField.getText()),
                            Integer.parseInt(snakeSpeedField.getText()),
                            Integer.parseInt(gameSpeedField.getText()),
                            Integer.parseInt(targetLengthField.getText()),
                            accelerationCheckBox.isSelected(),
                            snakeBehaviorChoiceBox.getValue(),
                            secondSnakeCheckBox.isSelected());
            objectMapper.writeValue(
                    new File("level_config_" + levelNumberField.getText() + ".json"),
                    currentConfig);
            System.out.println(
                    "Configuration saved successfully to level_config_"
                            + levelNumberField.getText()
                            + ".json");
        } catch (IOException | NumberFormatException e) {
            System.err.println("Failed to save configuration: " + e.getMessage());
        }
    }

    @FXML
    private void loadConfigFromFile() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Configuration File");
        fileChooser
                .getExtensionFilters()
                .add(new FileChooser.ExtensionFilter("JSON Files", "*.json"));
        File selectedFile = fileChooser.showOpenDialog(levelNumberField.getScene().getWindow());

        if (selectedFile != null) {
            ObjectMapper objectMapper = new ObjectMapper();
            try {
                LevelConfig config = objectMapper.readValue(selectedFile, LevelConfig.class);
                levelNumberField.setText(String.valueOf(config.getLevelNumber()));
                enemyCountField.setText(String.valueOf(config.getEnemyCount()));
                obstacleCountField.setText(String.valueOf(config.getObstacleCount()));
                snakeSpeedField.setText(String.valueOf(config.getSnakeSpeed()));
                gameSpeedField.setText(String.valueOf(config.getGameSpeed()));
                targetLengthField.setText(String.valueOf(config.getTargetLength()));
                accelerationCheckBox.setSelected(config.isAccelerationEnabled());
                snakeBehaviorChoiceBox.setValue(config.getBehavior());
                secondSnakeCheckBox.setSelected(config.isSecondSnakeEnabled());
                System.out.println(
                        "Configuration loaded successfully from " + selectedFile.getName());
            } catch (IOException e) {
                System.err.println("Failed to load configuration: " + e.getMessage());
            }
        }
    }

    public static List<LevelConfig> getSavedLevels() {
        return savedLevels;
    }
}
