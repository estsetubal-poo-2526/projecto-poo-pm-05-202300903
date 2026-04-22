package com.example.projectopoopm05202300903.models.player;

import com.example.projectopoopm05202300903.models.Board;
import com.example.projectopoopm05202300903.models.card.Card;
import com.example.projectopoopm05202300903.models.Deck;
import com.example.projectopoopm05202300903.models.card.UnitCard;
import com.example.projectopoopm05202300903.models.exceptions.EmptyDeckException;
import com.example.projectopoopm05202300903.models.exceptions.InsufficientManaException;
import com.example.projectopoopm05202300903.models.Hand;
import com.example.projectopoopm05202300903.models.interfaces.ITarget;

import java.util.List;

public abstract class Player implements ITarget {
    protected final String name;
    protected static final int MAX_HP = 20;
    protected int currentHp   = 20;
    protected int maxMana     = 0;
    protected int currentMana = 0;
    protected final Deck deck = new Deck();
    protected final Hand hand = new Hand();

    protected Player(String name) {
        this.name = name;
    }

    public void startTurnProcess() throws EmptyDeckException {
        if (maxMana < 10) maxMana++;
        currentMana = maxMana;
        hand.addCard(deck.drawCard());
    }

    public void playCard(Card card) throws InsufficientManaException {
        if (card.getManaCost() > currentMana)
            throw new InsufficientManaException("Mana insuficiente!");
        currentMana -= card.getManaCost();
        hand.removeCard(card);
    }

    @Override public void receiveDamage(int amount) { currentHp = Math.max(0, currentHp - amount); }
    @Override public void receiveHealing(int amount) { currentHp = Math.min(MAX_HP, currentHp + amount); }
    @Override public boolean isDead() { return currentHp <= 0; }

    public String getName()       { return name; }
    public int getMaxHp()         { return MAX_HP; }
    public int getCurrentHp()     { return currentHp; }
    public int getMaxMana()       { return maxMana; }
    public int getCurrentMana()   { return currentMana; }
    public Deck getDeck()         { return deck; }
    public Hand getHand()         { return hand; }

    public abstract void executeTurn();

    public abstract List<UnitCard> getBoardUnits(Board board);
}
