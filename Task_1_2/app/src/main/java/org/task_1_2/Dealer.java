package org.task_1_2;

import java.util.ArrayList;
import java.util.List;

public class Dealer extends Base {
    private List<Cards> dealerCards;
    private boolean isStanding;

    public Dealer() {
        super();
        this.dealerCards = new ArrayList<>();
        this.isStanding = false;
    }

    public void hit() {
        dealCard(dealerCards);
    }

    public void stand() {
        isStanding = true;
    }

    public int getDealerScore() {
        return getSumm(dealerCards.toArray(new Cards[0]));
    }

    public boolean isBusted() {
        return gameSituation(dealerCards.toArray(new Cards[0])) == -1;
    }

    public boolean isBlackjack() {
        return gameSituation(dealerCards.toArray(new Cards[0])) == 1;
    }

    public boolean isStanding() {
        return isStanding;
    }

    public void playRound() {
        while (getDealerScore() < 17) {
            hit();
        }
        stand();
    }

    public List<Cards> getDealerCards() {
        return dealerCards;
    }
}
