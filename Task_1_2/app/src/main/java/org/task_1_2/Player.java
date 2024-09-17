package org.task_1_2;

import java.util.ArrayList;
import java.util.List;

public class Player extends Base {
    private List<Cards> playerCards;
    private boolean isStanding;

    public Player() {
        super();
        this.playerCards = new ArrayList<>();
        this.isStanding = false;
    }

    public void hit() {
        dealCard(playerCards);
    }

    public void stand() {
        isStanding = true;
    }

    public int getPlayerScore() {
        return getSumm(playerCards.toArray(new Cards[0]));
    }

    public boolean isBusted() {
        return gameSituation(playerCards.toArray(new Cards[0])) == -1;
    }

    public boolean isBlackjack() {
        return gameSituation(playerCards.toArray(new Cards[0])) == 1;
    }

    public boolean isStanding() {
        return isStanding;
    }

    public List<Cards> getPlayerCards() {
        return playerCards;
    }
}
