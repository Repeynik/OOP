package org.task_1_1_2;

import org.enums.GameState;

import java.util.Scanner;

public class BlackjackGame {
    private DefaultPlayer player;
    private DefaultPlayer dealer;
    private GameState gameState;
    private UserInterface userInterface;
    private boolean gameOver;

    public BlackjackGame() {
        this.gameState = GameState.Defeat; // Устанавливаем начальное состояние игры
        this.player = new DefaultPlayer(); // Передаем gameState в конструктор
        this.dealer = new DefaultPlayer(); // Передаем gameState в конструктор
        this.userInterface = new UserInterface();
        this.gameOver = false;
    }

    public int playRound(Scanner scanner) {
        // Вывод результата
        checkFinishGameState();
        if (gameOver == true) {

            userInterface.gameOverText(player, dealer);
            checkFinishGameState();

        } else {
            // Раздача карт
            player.dealCard();
            player.dealCard();
            dealer.dealCard();
            dealer.dealCard();

            // Вывод карт игроков
            userInterface.getScore(0, player, dealer);
            // Ход игрока
            userInterface.playerInput(scanner, player, dealer);
            player.stand();

            // Ход дилера
            userInterface.dealerStart(player, dealer);
            userInterface.dealerPlay(player, dealer);

            // Проверка результата
            checkFinishGameState();
        }
        return gameState.getState();
    }

    private void checkFinishGameState() {
        if (player.isBusted()) {
            gameOver = true;
        } else if (player.isBlackjack()) {
            gameOver = true;
            gameState = GameState.Victory;
        } else if (player.isStanding() && dealer.isBusted()) {
            gameOver = true;
            gameState = GameState.Victory;
        } else if (player.isStanding()
                && dealer.getPlayerScore()
                        > player.getPlayerScore()) {
            gameOver = true;
        } else if (player.isStanding()
                && dealer.getPlayerScore()
                        < player.getPlayerScore()) {
            gameOver = true;
            gameState = GameState.Victory;
        } else if (player.isStanding()
                && dealer.getPlayerScore()
                        == player.getPlayerScore()) {
            gameOver = true;
            gameState = GameState.Draw;
        }
    }

    public boolean isGameOver() {
        return gameOver;
    }
}
