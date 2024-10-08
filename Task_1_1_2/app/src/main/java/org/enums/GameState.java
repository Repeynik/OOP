package org.enums;

import org.task_1_1_2.Cards;

public enum GameState {
    Victory(1),
    Defeat(-1),
    Draw(0),
    BlackJack(2);

    private int state;

    GameState(int state) {
        this.state = state;
    }

    public int getState() {
        return state;
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
            return BlackJack.state;
        } else if (getSumm(cards) == 21) {
            return Victory.state;
        } else if (getSumm(cards) > 21) {

            return Defeat.state;
        }
        return Draw.state;
    }
}
