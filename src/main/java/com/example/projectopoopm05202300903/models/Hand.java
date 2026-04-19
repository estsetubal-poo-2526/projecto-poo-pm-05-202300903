package com.example.projectopoopm05202300903.models;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Hand {
    private List<Card> cards;
    private int maxSize;

    public Hand() {
        this.cards = new ArrayList<>();
        this.maxSize = 10;
    }

    public Hand(int maxSize) {
        this.cards = new ArrayList<>();
        this.maxSize = maxSize;
    }

    public void addCard(Card c) {
        if (cards.size() < maxSize) {
            cards.add(c);
        }
    }

    public void removeCard(Card c) {
        cards.remove(c);
    }

    public List<Card> getPlayableCards(int currentMana) {
        return cards.stream()
                .filter(card -> card.getManaCost() <= currentMana)
                .collect(Collectors.toList());
    }

    public List<Card> getCards() {
        return cards;
    }

    public int getMaxSize() {
        return maxSize;
    }

    public void setMaxSize(int maxSize) {
        this.maxSize = maxSize;
    }

    public boolean isEmpty() {
        return cards.isEmpty();
    }

    public int size() {
        return cards.size();
    }
}
