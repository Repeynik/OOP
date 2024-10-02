package org.task_1_2;

import org.enums.Suit;
import org.enums.Value;

/*
 *  Класс для создания доски с картами с мастью и значением.
 */
public class Cards {
    private final Suit suit;
    private final Value value;

    public Cards(Suit suit, Value value) {
        this.suit = suit;
        this.value = value;
    }

    public Suit getSuit() {
        return suit;
    }

    public Value getValue() {
        return value;
    }

    public int getIntValue() {
        return value.getValue();
    }

    public void setAceValue() {
        value.setAceValue();
    }

    @Override
    public String toString() {
        return value.getTitle() + " " + suit.getTitle();
    }
}
