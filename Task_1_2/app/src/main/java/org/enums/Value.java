package org.enums;

/*
 * Класс для перечисления значения карты.
 */
public enum Value {
    Ace("Туз", 11),
    Ace_with_1("Туз",1),
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
    private int value;

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

    public void setAceValue() {
        Value.Ace.value = 1;
    }
}
