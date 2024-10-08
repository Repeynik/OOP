package org.task_1_1_2;

import static org.junit.jupiter.api.Assertions.*;

import org.enums.GameState;
import org.junit.jupiter.api.Test;

import java.util.List;

class DefaultPlayerTest {

    @Test
    void appGetSummTest() {
        DefaultPlayer player = new DefaultPlayer(GameState.Draw);
        player.abstractCards.add(player.deck.get(7));
        player.abstractCards.add(player.deck.get(6));
        assertEquals(9, player.getPlayerScore(player.abstractCards));
    }

    @Test
    void testAceToOne() {
        DefaultPlayer player = new DefaultPlayer(GameState.Draw);
        player.abstractCards.add(player.deck.get(13));
        player.abstractCards.add(player.deck.get(0));
        assertEquals(12, player.getPlayerScore(player.abstractCards));
    }

    @Test
    void testGetCard() {
        DefaultPlayer player = new DefaultPlayer(GameState.Draw);
        Cards card = player.getCard();
        assertNotNull(card);
        assertEquals(51, player.deck.size());
    }

    @Test
    void testGenerateDeck() {
        DefaultPlayer player = new DefaultPlayer(GameState.Draw);
        List<Cards> deck = player.deck;
        assertEquals(52, deck.size());
        assertTrue(deck.stream().allMatch(card -> card != null));
    }

    @Test
    void testHit() {
        DefaultPlayer player = new DefaultPlayer(GameState.Draw);
        int initialHandSize = player.abstractCards.size();
        player.hit();
        assertEquals(
                initialHandSize + 1, player.abstractCards.size()); // Hand size should increase by 1
    }

    @Test
    void testStand() {
        DefaultPlayer player = new DefaultPlayer(GameState.Draw);
        player.stand();
        assertTrue(player.isStanding()); // Player should be standing now
    }

    @Test
    void testDealCard() {
        DefaultPlayer player = new DefaultPlayer(GameState.Draw);
        player.dealCard(player.abstractCards);
        assertEquals(1, player.abstractCards.size());
        assertTrue(player.deck.size() < 52); // Deck should have fewer cards
    }

    @Test
    void testIsBlackjack() {
        DefaultPlayer player = new DefaultPlayer(GameState.Draw);
        player.abstractCards.add(player.deck.get(13));
        assertFalse(player.isBlackjack());
    }
}
