package com.example.projectopoopm05202300903.models;

import com.example.projectopoopm05202300903.models.card.UnitCard;
import com.example.projectopoopm05202300903.models.exceptions.EmptyDeckException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DeckTest {

    private Deck deck;

    @BeforeEach
    void setUp() {
        deck = new Deck();
    }

    @Test
    void addCard_increasesSize() {
        deck.addCard(new UnitCard("A", 1, "", 1, 1));
        assertEquals(1, deck.size());
    }

    @Test
    void drawCard_returnsAddedCard() throws EmptyDeckException {
        UnitCard card = new UnitCard("A", 1, "", 1, 1);
        deck.addCard(card);
        assertSame(card, deck.drawCard());
    }

    @Test
    void drawCard_decreasesSize() throws EmptyDeckException {
        deck.addCard(new UnitCard("A", 1, "", 1, 1));
        deck.drawCard();
        assertEquals(0, deck.size());
    }

    @Test
    void drawCard_onEmptyDeck_throwsException() {
        assertThrows(EmptyDeckException.class, deck::drawCard);
    }

    @Test
    void shuffle_doesNotChangeSize() {
        for (int i = 0; i < 10; i++) deck.addCard(new UnitCard("C" + i, 1, "", 1, 1));
        deck.shuffle();
        assertEquals(10, deck.size());
    }

    @Test
    void size_returnsCorrectCount() {
        assertEquals(0, deck.size());
        deck.addCard(new UnitCard("A", 1, "", 1, 1));
        deck.addCard(new UnitCard("B", 1, "", 1, 1));
        assertEquals(2, deck.size());
    }
}