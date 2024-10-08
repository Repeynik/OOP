package org.enums;

public enum Suit {
    Clubs("Трефы"),
    Diamonds("Бубны"),
    Hearts("Червы"),
    Spades("Пики");

    private final String title;

    Suit(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }
}
