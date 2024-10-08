package org.task_1_1_2;

import org.enums.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/*
 * Класс реализующий базовую логику игроков.
 */
public class DefaultPlayer {
    private List<Cards> deck;
    private List<Cards> abstractCards;

    private boolean isStanding;

    public List<Cards> getAbstractCards(){
        return abstractCards;
    }

    public List<Cards> getDeck(){
        return deck;
    }

    /*
     * Создание игровой доски и списка карт для игроков.
     */
    public DefaultPlayer() {
        this.deck = generateDeck();
        this.abstractCards = new ArrayList<>();
        this.isStanding = false;
    }

    /*
     * Генерация игровой доски из 52 карт (без повторов).
     */
    private List<Cards> generateDeck() {
        List<Cards> deck = new ArrayList<>();
        for (var s : Suit.values()) {
            for (var v : Value.values()) {
                if (v.getValue() != 1) {
                    deck.add(new Cards(s, v));
                }
            }
        }
        return deck;
    }

    /*
     * Получение карт для игрока, убираем карту из общей доски.
     */
    Cards getCard() {
        var random = new Random();
        var index = random.nextInt(deck.size());
        var card = deck.get(index);
        deck.remove(index);
        return card;
    }

    /*
     * Добавление карты в руку игрока.
     */
    void dealCard() {
        abstractCards.add(getCard());
    }

    public List<Cards> getPlayerCards() {
        int sum = getSumm(abstractCards.toArray(new Cards[0]));

        if (sum > 21) {
            for (int i = 0; i < abstractCards.size(); i++) {
                var card = abstractCards.get(i);
                if (card.getIntValue() == 11) {
                    abstractCards.set(i, new Cards(card.getSuit(), Value.Ace_with_1));
                    sum -= 10;
                }
                if (sum <= 21) {
                    break;
                }
            }
        }
        return abstractCards;
    }

    /*
     * Проверка состояния - а не проиграл ли игрок в данный момент.
     */
    public boolean isBusted() {
        return getGameSituation(abstractCards.toArray(new Cards[0]), false) == -1;
    }

    public boolean isBlackjack() {
        return getGameSituation(abstractCards.toArray(new Cards[0]), true) == 1;
    }

    /*
     * Получение состояния о ходе игрока
     */
    public boolean isStanding() {
        return isStanding;
    }

    public void stand() {
        isStanding = true;
    }

    public int getPlayerScore() {
        int sum = getSumm(abstractCards.toArray(new Cards[0]));

        if (sum > 21) {
            for (int i = 0; i < abstractCards.size(); i++) {
                var card = abstractCards.get(i);
                if (card.getIntValue() == 11) {
                    abstractCards.set(i, new Cards(card.getSuit(), Value.Ace_with_1));
                    sum -= 10;
                }
                if (sum <= 21) {
                    break;
                }
            }
        }
        return sum;
    }

        public int getSumm(Cards[] cards) {
        int sum = 0;
        for (var card : cards) {
            // System.out.println(card.getIntValue());
            sum += card.getIntValue();
        }
        return sum;
    }

    /*
     * Проверка статуса игроков во время игры.
     * При достижении определенного числа карт игра завершается.
     */
    public int getGameSituation(Cards[] cards, boolean blackJackState) {
        if (blackJackState) {
            return GameState.BlackJack.getState();
        } else if (getSumm(cards) == 21) {
            return GameState.Victory.getState();
        } else if (getSumm(cards) > 21) {

            return GameState.Defeat.getState();
        }
        return GameState.Draw.getState();
    }
}
