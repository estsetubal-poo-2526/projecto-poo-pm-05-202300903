package com.example.projectopoopm05202300903.models;

import com.example.projectopoopm05202300903.models.card.Card;
import com.example.projectopoopm05202300903.models.card.UnitCard;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class HandTest {

    private Hand hand;
    private UnitCard card;

    @BeforeEach
    void setUp() {
        hand = new Hand();
        card = new UnitCard("A", 1, "", 1, 1);
    }

    @Test
    void addCard_appearsInGetCards() {
        hand.addCard(card);
        assertTrue(hand.getCards().contains(card));
    }

    @Test
    void removeCard_disappearsFromGetCards() {
        hand.addCard(card);
        hand.removeCard(card);
        assertFalse(hand.getCards().contains(card));
    }

    @Test
    void getCards_returnsImmutableCopy() {
        hand.addCard(card);
        List<Card> snapshot = hand.getCards();
        assertThrows(UnsupportedOperationException.class, () -> snapshot.add(card));
    }

    @Test
    void getCards_initiallyEmpty() {
        assertTrue(hand.getCards().isEmpty());
    }

    @Test
    void addMultipleCards_allPresent() {
        UnitCard b = new UnitCard("B", 2, "", 2, 2);
        UnitCard c = new UnitCard("C", 3, "", 3, 3);
        hand.addCard(card);
        hand.addCard(b);
        hand.addCard(c);
        assertEquals(3, hand.getCards().size());
    }
}