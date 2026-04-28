package com.example.projectopoopm05202300903.models;

import com.example.projectopoopm05202300903.models.card.Card;
import com.example.projectopoopm05202300903.models.exceptions.EmptyDeckException;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.List;

public class Deck {
    private final Deque<Card> cards = new ArrayDeque<>();

    public void addCard(Card card) {
        cards.addLast(card);
    }

    public void shuffle() {
        List<Card> list = new ArrayList<>(cards);
        Collections.shuffle(list);
        cards.clear();
        cards.addAll(list);
    }

    public Card drawCard() throws EmptyDeckException {
        if (cards.isEmpty()) throw new EmptyDeckException("O baralho está vazio!");
        return cards.removeFirst();
    }

    public int size() {
        return cards.size();
    }
}