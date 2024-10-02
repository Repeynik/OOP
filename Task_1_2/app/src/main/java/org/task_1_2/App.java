package org.task_1_2;

import java.util.Scanner;

public class App {
    public static void startApp() {
        var scanner = new Scanner(System.in);
        var game = new BlackjackGame();
        int rounds = 0;
        int dealerScore = 0;
        int playerScore = 0;
        System.out.println("Добро пожаловать в Блэкджек!");

        do {
            rounds += 1;
            System.out.println("\nРаунд " + rounds);
            System.out.println("Дилер раздал карты");
            var gameState = game.playRound(scanner);
            if (gameState == 1) {
                playerScore += 1;
                System.out.println("Вы выиграли раунд!");
            } else if (gameState == -1) {
                dealerScore += 1;
                System.out.println("Дилер выиграл раунд");
            } else {
                System.out.println("Ничья. Счёт не изменился");
            }
            System.out.println("Счет " + playerScore + ":" + dealerScore + "\n");

            // Обработка ввода
            System.out.print("Хотите сыграть еще раз? (y/n): ");
            if (scanner.hasNextLine()) { // Проверка на наличие следующей строки
                String input = scanner.nextLine();

                if (input.equalsIgnoreCase("y")) {
                    game = new BlackjackGame();
                } else {
                    System.out.println("Спасибо за игру!");
                    break;
                }
            } else {
                System.out.println("Ошибка: ввод недоступен.");
                break; // Выход из цикла при ошибке
            }

        } while (!game.isGameOver());

        scanner.close();
    }
}
