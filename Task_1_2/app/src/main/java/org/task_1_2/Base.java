package org.task_1_2;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


/*
 * Класс реализующий базовую логику игроков.
 */
public abstract class Base {
    protected List<Cards> deck;
    protected List<Cards> playerCards;
    protected List<Cards> dealerCards;

    /*
     * Создание игровой доски и списка карт для игроков.
     */
    public Base() {
        this.deck = generateDeck();
        this.playerCards = new ArrayList<>();
        this.dealerCards = new ArrayList<>();
    }

    /*
     * Генерация игровой доски из 52 карт.
     */
    private List<Cards> generateDeck() {
        List<Cards> deck = new ArrayList<>();
        for (Suit s : Suit.values()) {
            for (Value v : Value.values()) {
                deck.add(new Cards(s, v));
            }
        }
        return deck;
    }

    int getSumm(Cards[] cards) {
        int sum = 0;
        for (Cards card : cards) {
            sum += card.getIntValue();
        }
        return sum;
    }
    /*
     * Проверка статуса игроков во время игры. 
     * При достижении определенного числа карт игра завершается.
     */
    int gameSituation(Cards[] cards) {
        if (getSumm(cards) == 21) {
            return 1;
        }
        if (getSumm(cards) > 21) {
            return -1;
        }
        return 0;
    }
    /*
     * Получение карт для игрока, убираем карту из общей доски.
     */
    Cards getCard() {
        Random random = new Random();
        int index = random.nextInt(deck.size());
        Cards card = deck.get(index);
        deck.remove(index);
        return card;
    }
    /*
     * Добавление карты в руку игрока.
     */
    void dealCard(List<Cards> playerCards2) {
        playerCards2.add(getCard());
    }
}
