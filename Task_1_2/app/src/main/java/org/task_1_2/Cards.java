package org.task_1_2;

import java.util.ArrayList;
import java.util.List;

/*
 * Класс для перечисления масти карт.
 */
enum Suit {
    Clubs("Трефы"),
    Diamonds("Бубны"),
    Hearts("Червы"),
    Spades("Пики");

    private final String title;

    Suit(String title) {
        this.title = title;
    }

    String getTitle() {
        return title;
    }
}

/*
 * Класс для перечисления значения карты.
 */
enum Value {
    Ace("Туз", 11),
    Jack("Валет", 10),
    Queen("Дама", 10),
    King("Король", 10),
    Two("Двойка", 2),
    Three("Тройка", 3),
    Four("Четверка", 4),
    Five("Пятерка", 5),
    Six("Шестёрка", 6),
    Seven("Семёрка", 7),
    Eight("Восьмёрка", 8),
    Nine("Девятка", 9),
    Ten("Десятка", 10);

    private final String title;
    private final int value;

    Value(String title, int value) {
        this.title = title;
        this.value = value;
    }

    public String getTitle() {
        return title;
    }

    public int getValue() {
        return value;
    }
}

/*
 *  Класс для создания доски с картами с мастью и значением.
 */
public class Cards {
    private final Suit suit;
    private final Value value;

    Cards(Suit suit, Value value) {
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

    @Override
    public String toString() {
        return value.getTitle() + " " + suit.getTitle();
    }
}
