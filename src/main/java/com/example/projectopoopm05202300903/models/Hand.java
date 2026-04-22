package com.example.projectopoopm05202300903.models;

import com.example.projectopoopm05202300903.models.card.Card;

import java.util.ArrayList;
import java.util.List;

public class Hand {
    private final List<Card> cards = new ArrayList<>();

    public void addCard(Card card) { cards.add(card); }
    public void removeCard(Card card) { cards.remove(card); }

    public List<Card> getCards() { return cards; }

    public List<Card> getPlayableCards(int mana) {
        return cards.stream().filter(c -> c.getManaCost() <= mana).toList();
    }
}