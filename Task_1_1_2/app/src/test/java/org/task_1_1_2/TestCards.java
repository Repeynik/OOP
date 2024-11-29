package org.task_1_1_2;

public enum TestCards {
    ClubsFive(7),
    ClubsFour(6),  
    ClubsAce(0),
    DiamondsAce(13);

    
    private final int index;

    TestCards(int i) {
        this.index = i;
    }

    public int getIndex() {
        return index;
    }
}