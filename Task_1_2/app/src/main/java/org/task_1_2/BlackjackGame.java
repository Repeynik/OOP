package org.task_1_2;

import java.util.Scanner;

public class BlackjackGame extends Base {
    private Player player;
    private Dealer dealer;
    private boolean gameOver;
    private boolean playerWin;

    public BlackjackGame() {
        super();
        this.player = new Player();
        this.dealer = new Dealer();
        this.gameOver = false;
        this.playerWin = false;
    }

    public boolean playRound(Scanner scanner) {
        // Вывод результата
        checkGameState();
        if (gameOver == true) {
            System.out.println("Итоговый счет:");
            System.out.println("Игрок: " + player.getPlayerScore());
            System.out.println("Дилер: " + dealer.getDealerScore());
            checkGameState();

        } else {
            // Раздача карт
            dealCard(player.getPlayerCards());
            dealCard(player.getPlayerCards());
            dealCard(dealer.getDealerCards());
            dealCard(dealer.getDealerCards());

            // Вывод карт игроков
            getScore(0);
            // Ход игрока
            while (!player.isStanding() && !player.isBusted()) {
                System.err.println("Ваш ход\n-----");
                System.out.println("Ваш счет: " + player.getPlayerScore());
                System.out.print("Введите '1', чтобы взять карту, и '0', чтобы остановиться ...\n");

                // Проверяем наличие следующей строки
                if (scanner.hasNextLine()) {
                    String input = scanner.nextLine();
                    if (input.equalsIgnoreCase("1")) {
                        playerHit();
                        System.out.println(
                                "Вы открыли карту "
                                        + player.getPlayerCards()
                                                .get(player.getPlayerCards().size() - 1)
                                        + "("
                                        + player.getPlayerCards()
                                                .get(player.getPlayerCards().size() - 1)
                                                .getIntValue()
                                        + ")");
                        getScore(0);
                    } else if (input.equalsIgnoreCase("0")) {
                        playerStand();
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

            // Ход дилера
            dealerPlay();

            // Проверка результата
            checkGameState();
        }
        return playerWin;
    }

    public void playerHit() {
        player.hit();
    }

    public void playerStand() {
        player.stand();
        System.out.println("Ход дилера\n-----");
        System.out.println(
                "Дилер открывает закрытую карту "
                        + dealer.getDealerCards().get(dealer.getDealerCards().size() - 1)
                        + "("
                        + dealer.getDealerCards()
                                .get(dealer.getDealerCards().size() - 1)
                                .getIntValue()
                        + ")");
        dealerPlay();
    }

    private void dealerPlay() {
        System.out.println(
                "Карты дилера: " + dealer.getDealerCards() + " => " + dealer.getDealerScore());
        while (dealer.getDealerScore() < 17) {
            dealer.hit();
            System.out.println(
                    "Дилер открывает карту "
                            + dealer.getDealerCards().get(dealer.getDealerCards().size() - 1)
                            + "("
                            + dealer.getDealerCards()
                                    .get(dealer.getDealerCards().size() - 1)
                                    .getIntValue()
                            + ")");
            getScore(1);
        }
    }

    private void getScore(int openDealer) {
        System.out.println(
                "\tВаши карты: " + player.getPlayerCards() + " => " + player.getPlayerScore());
        if (openDealer == 1) {
            System.out.println(
                    "\tКарты дилера: "
                            + dealer.getDealerCards()
                            + " => "
                            + dealer.getDealerScore()
                            + "\n");
        } else {
            System.out.println(
                    "\tКарты дилера: ["
                            + dealer.getDealerCards().get(0)
                            + ", <закрытая карта>]"
                            + "\n");
        }
    }

    private void checkGameState() {
        if (player.isBusted()) {
            gameOver = true;
        } else if (player.isBlackjack()) {
            System.out.println("Игрок получил Блэкджек!");
            gameOver = true;
            playerWin = true;
        } else if (player.isStanding() && dealer.isBusted()) {
            gameOver = true;
            playerWin = true;
        } else if (player.isStanding() && dealer.getDealerScore() > player.getPlayerScore()) {
            gameOver = true;
        } else if (player.isStanding() && dealer.getDealerScore() < player.getPlayerScore()) {
            gameOver = true;
            playerWin = true;
        } else if (player.isStanding() && dealer.getDealerScore() == player.getPlayerScore()) {
            System.out.println("Ничья.");
            gameOver = true;
        }
    }

    public boolean isGameOver() {
        return gameOver;
    }
}
