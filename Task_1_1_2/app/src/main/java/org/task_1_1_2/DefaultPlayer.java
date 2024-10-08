package org.task_1_1_2;

import org.enums.*;
import org.enums.Suit;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/*
 * Класс реализующий базовую логику игроков.
 */
public class DefaultPlayer {
    protected List<Cards> deck;
    protected List<Cards> abstractCards;

    private boolean isStanding;
    protected GameState gameState;

    /*
     * Создание игровой доски и списка карт для игроков.
     */
    public DefaultPlayer(GameState gameState) {
        this.deck = generateDeck();
        this.abstractCards = new ArrayList<>();
        this.isStanding = false;
        this.gameState = gameState;
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
    void dealCard(List<Cards> abstractCards) {
        abstractCards.add(getCard());
    }

    public List<Cards> getPlayerCards(List<Cards> cards) {
        int sum = gameState.getSumm(cards.toArray(new Cards[0]));

        if (sum > 21) {
            for (int i = 0; i < cards.size(); i++) {
                var card = cards.get(i);
                if (card.getIntValue() == 11) {
                    cards.set(i, new Cards(card.getSuit(), Value.Ace_with_1));
                    sum -= 10;
                }
                if (sum <= 21) {
                    break;
                }
            }
        }
        return cards;
    }

    /*
     * Проверка состояния - а не проиграл ли игрок в данный момент.
     */
    public boolean isBusted() {
        return gameState.getGameSituation(abstractCards.toArray(new Cards[0]), false) == -1;
    }

    public boolean isBlackjack() {
        return gameState.getGameSituation(abstractCards.toArray(new Cards[0]), true) == 1;
    }

    /*
     * Получение состояния о ходе игрока
     */
    public boolean isStanding() {
        return isStanding;
    }

    public void hit() {
        dealCard(abstractCards);
    }

    public void stand() {
        isStanding = true;
    }

    public int getPlayerScore(List<Cards> cards) {
        int sum = gameState.getSumm(cards.toArray(new Cards[0]));

        if (sum > 21) {
            for (int i = 0; i < cards.size(); i++) {
                var card = cards.get(i);
                if (card.getIntValue() == 11) {
                    cards.set(i, new Cards(card.getSuit(), Value.Ace_with_1));
                    sum -= 10;
                }
                if (sum <= 21) {
                    break;
                }
            }
        }
        return sum;
    }
}
