package org.task_1_2;

import java.util.Scanner;

public class App {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        BlackjackGame game = new BlackjackGame();
        var rounds = 0;
        int dealerScore = 0;
        int playerScore = 0;
        System.out.println("Добро пожаловать в Блэкджек!");

        do {
            rounds += 1;
            System.out.println("\nРаунд " + rounds);
            System.out.println("Дилер раздал карты");
            boolean gameState = game.playRound();
            if (gameState) {
                playerScore += 1;
                System.out.println("Вы выиграли раунд!");
            } else {
                dealerScore += 1;
                System.out.println("Дилер выиграл раунд");
            }
            System.out.println("Счет " + playerScore + ":" + dealerScore + "\n");
            System.out.print("Хотите сыграть еще раз? (y/n): ");
            String input = scanner.nextLine();
            if (input.equalsIgnoreCase("y")) {
                game = new BlackjackGame();
            } else {
                System.out.println("Спасибо за игру!");
                break;
            }
        } while (!game.isGameOver());

        scanner.close();
    }
}
