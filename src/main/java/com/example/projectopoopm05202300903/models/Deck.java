package com.example.projectopoopm05202300903.models;

import com.example.projectopoopm05202300903.models.card.Card;
import com.example.projectopoopm05202300903.models.exceptions.EmptyDeckException;

import java.util.Collections;
import java.util.Stack;

public class Deck {
    private final Stack<Card> cards = new Stack<>();

    public void addCard(Card card) {
        cards.push(card);
    }

    public void shuffle() {
        Collections.shuffle(cards);
    }

    public Card drawCard() throws EmptyDeckException {
        if (cards.isEmpty()) throw new EmptyDeckException("O baralho está vazio!");
        return cards.pop();
    }

    public boolean isEmpty() { return cards.isEmpty(); }
    public int size() { return cards.size(); }
}
