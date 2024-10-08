package org.task_1_1_2;

import java.util.Scanner;

public class UserInterface {
    public void playerInput(Scanner scanner, DefaultPlayer player, DefaultPlayer dealer) {
        while (!player.isStanding() && !player.isBusted()) {
            System.err.println("Ваш ход\n-----");
            System.out.println("Ваш счет: " + player.getPlayerScore());
            System.out.println("Введите '1', чтобы взять карту, и '0', чтобы остановиться ...");

            // Проверяем наличие следующей строки
            if (scanner.hasNextLine()) {
                String input = scanner.nextLine();
                if (input.equalsIgnoreCase("1")) {
                    player.dealCard();
                    System.out.println(
                            "Вы открыли карту "
                                    + player.getPlayerCards()
                                            .get(
                                                    player.getPlayerCards()
                                                                    .size()
                                                            - 1)
                                    + " ("
                                    + player.getPlayerCards()
                                            .get(
                                                    player.getPlayerCards()
                                                                    .size()
                                                            - 1)
                                            .getIntValue()
                                    + ")");
                    getScore(0, player, dealer);
                } else if (input.equalsIgnoreCase("0")) {
                    break;
                } else {
                    System.out.println("Некорректный ввод. Пожалуйста, введите '1' или '0'.");
                }
            } else {
                System.out.println("Ошибка чтения ввода. Пожалуйста, попробуйте снова.");
                // Завершение или перезапуск цикла
                break;
            }
        }
    }

    public void dealerStart(DefaultPlayer player, DefaultPlayer dealer) {
        System.out.println("Ход дилера\n-----");
        System.out.println(
                "Дилер открывает закрытую карту "
                        + dealer.getPlayerCards()
                                .get(dealer.getPlayerCards().size() - 1)
                        + "("
                        + dealer.getPlayerCards()
                                .get(dealer.getPlayerCards().size() - 1)
                                .getIntValue()
                        + ")");
        dealerPlay(player, dealer);
    }

    public void dealerPlay(DefaultPlayer player, DefaultPlayer dealer) {
        System.out.println(
                "Карты дилера: "
                        + printPlayerList(dealer)
                        + " => "
                        + dealer.getPlayerScore());
        while (dealer.getPlayerScore() < 17) {
            dealer.dealCard();
            System.out.println(
                    "Дилер открывает карту "
                            + dealer.getPlayerCards()
                                    .get(dealer.getPlayerCards().size() - 1)
                            + "("
                            + dealer.getPlayerCards()
                                    .get(dealer.getPlayerCards().size() - 1)
                                    .getIntValue()
                            + ")");
            getScore(1, player, dealer);
        }
    }

    public String printPlayerList(DefaultPlayer player) {
        var playerCards = player.getPlayerCards();
        String lineOfCards = "[";
        for (Cards cards : playerCards) {
            lineOfCards += cards.toString();
            lineOfCards += " (";
            lineOfCards += cards.getIntValue();
            lineOfCards += ")";
            lineOfCards += ", ";
        }
        lineOfCards = removeLastChar(lineOfCards);
        lineOfCards += "]";
        return lineOfCards;
    }

    public String removeLastChar(String str) {
        return str.substring(0, str.length() - 2);
    }

    public void getScore(int openDealer, DefaultPlayer player, DefaultPlayer dealer) {
        System.out.println(
                "\tВаши карты: "
                        + printPlayerList(player)
                        + " => "
                        + player.getPlayerScore());
        if (openDealer == 1) {
            System.out.println(
                    "\tКарты дилера: "
                            + printPlayerList(dealer)
                            + " => "
                            + dealer.getPlayerScore()
                            + "\n");
        } else {
            System.out.println(
                    "\tКарты дилера: ["
                            + dealer.getPlayerCards().get(0)
                            + " ("
                            + dealer.getPlayerCards().get(0).getIntValue()
                            + ")"
                            + ", <закрытая карта>]"
                            + "\n");
        }
    }

    public void gameOverText(DefaultPlayer player, DefaultPlayer dealer) {
        System.out.println("Итоговый счет:");
        System.out.println("Игрок: " + player.getPlayerScore());
        System.out.println("Дилер: " + dealer.getPlayerScore());
    }
}
