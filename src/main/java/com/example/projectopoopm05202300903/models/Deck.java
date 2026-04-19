package com.example.projectopoopm05202300903.models;

import com.example.projectopoopm05202300903.models.Exceptions.EmptyDeckException;
import com.example.projectopoopm05202300903.models.Card.Card;

import java.util.Collections;
import java.util.List;
import java.util.Stack;

public class Deck {
    private Stack<Card> cards;

    public Deck() {
        this.cards = new Stack<>();
    }

    public Deck(List<Card> cards) {
        this.cards = new Stack<>();
        this.cards.addAll(cards);
    }

    public void shuffle() {
        Collections.shuffle(cards);
    }

    public Card drawCard() throws EmptyDeckException {
        if (cards.isEmpty()) {
            throw new EmptyDeckException();
        }
        return cards.pop();
    }

    public int getRemainingCardsCount() {
        return cards.size();
    }

    public boolean isEmpty() {
        return cards.isEmpty();
    }

    public void addCard(Card card) {
        cards.push(card);
    }
}
