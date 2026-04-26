package com.example.projectopoopm05202300903.models.player;

import com.example.projectopoopm05202300903.models.Deck;
import com.example.projectopoopm05202300903.models.Hand;
import com.example.projectopoopm05202300903.models.card.Card;
import com.example.projectopoopm05202300903.models.enums.PlayerType;
import com.example.projectopoopm05202300903.models.exceptions.EmptyDeckException;
import com.example.projectopoopm05202300903.models.exceptions.InsufficientManaException;
import com.example.projectopoopm05202300903.models.interfaces.ITarget;

public abstract class Player implements ITarget {
    private static final int MAX_HP  = 20;
    private static final int MAX_MANA = 10;

    private final String name;
    private final PlayerType playerType;
    private final Deck deck = new Deck();
    private final Hand hand = new Hand();
    private int currentHp = MAX_HP;
    private int maxMana = 0;
    private int currentMana = 0;

    protected Player(String name, PlayerType playerType) {
        this.name = name;
        this.playerType = playerType;
    }

    public PlayerType getPlayerType() { return playerType; }

    public void startTurn() throws EmptyDeckException {
        if (maxMana < MAX_MANA) maxMana++;
        currentMana = maxMana;
        hand.addCard(deck.drawCard());
    }

    public void playCard(Card card) throws InsufficientManaException {
        if (card.getManaCost() > currentMana)
            throw new InsufficientManaException("Mana insuficiente para jogar " + card.getName());
        currentMana -= card.getManaCost();
        hand.removeCard(card);
    }

    @Override
    public void receiveDamage(int amount) {
        currentHp = Math.max(0, currentHp - amount);
    }

    @Override
    public void takeAttackFrom(int incomingDamage, ITarget attacker) {
        receiveDamage(incomingDamage);
    }

    public void receiveHealing(int amount) {
        currentHp = Math.min(MAX_HP, currentHp + amount);
    }

    public boolean isDead() {
        return currentHp <= 0;
    }

    public String getName() {
        return name;
    }

    public int getMaxHp()       { return MAX_HP; }
    public int getCurrentHp()   { return currentHp; }
    public int getMaxMana()     { return maxMana; }
    public int getCurrentMana() { return currentMana; }
    public Deck getDeck()       { return deck; }
    public Hand getHand()       { return hand; }
}
