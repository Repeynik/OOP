package org.task_1_1_2;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import java.util.List;

class DefaultPlayerTest {

    @Test
    void appGetSummTest() {
        DefaultPlayer player = new DefaultPlayer();
        player.getAbstractCards().add(player.getDeck().get(TestCards.ClubsFive.getIndex()));
        player.getAbstractCards().add(player.getDeck().get(TestCards.ClubsFour.getIndex()));
        assertEquals(9, player.getPlayerScore());
    }

    @Test
    void testAceToOne() {
        DefaultPlayer player = new DefaultPlayer();
        player.getAbstractCards().add(player.getDeck().get(TestCards.ClubsAce.getIndex()));
        player.getAbstractCards().add(player.getDeck().get(TestCards.DiamondsAce.getIndex()));
        assertEquals(12, player.getPlayerScore());
    }

    @Test
    void testGetCard() {
        DefaultPlayer player = new DefaultPlayer();
        Cards card = player.getCard();
        assertNotNull(card);
        assertEquals(51, player.getDeck().size());
    }

    @Test
    void testGenerateDeck() {
        DefaultPlayer player = new DefaultPlayer();
        List<Cards> deck = player.getDeck();
        assertEquals(52, deck.size());
        assertTrue(deck.stream().allMatch(card -> card != null));
    }

    @Test
    void testStand() {
        DefaultPlayer player = new DefaultPlayer();
        player.stand();
        assertTrue(player.isStanding()); // Player should be standing now
    }

    @Test
    void testDealCard() {
        DefaultPlayer player = new DefaultPlayer();
        player.dealCard();
        assertEquals(1, player.getAbstractCards().size());
        assertTrue(player.getDeck().size() < 52); // Deck should have fewer cards
    }

    @Test
    void testIsBlackjack() {
        DefaultPlayer player = new DefaultPlayer();
        player.getAbstractCards().add(player.getDeck().get(13));
        assertFalse(player.isBlackjack());
    }
}
