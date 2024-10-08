package org.enums;

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
}
