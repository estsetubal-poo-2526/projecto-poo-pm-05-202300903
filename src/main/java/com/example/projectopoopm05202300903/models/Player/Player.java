package com.example.projectopoopm05202300903.models.Player;

import com.example.projectopoopm05202300903.models.Exceptions.EmptyDeckException;
import com.example.projectopoopm05202300903.models.Exceptions.InsufficientManaException;
import com.example.projectopoopm05202300903.models.interfaces.ITarget;
import com.example.projectopoopm05202300903.models.Board;
import com.example.projectopoopm05202300903.models.Card.Card;
import com.example.projectopoopm05202300903.models.Card.UnitCard;
import com.example.projectopoopm05202300903.models.Deck;
import com.example.projectopoopm05202300903.models.Hand;

import java.util.List;

public abstract class Player implements ITarget {
    private String name;
    private int maxHp;
    private int currentHp;
    private int maxMana;
    private int currentMana;
    private Deck deck;
    private Hand hand;

    public Player(String name, int maxHp, Deck deck) {
        this.name = name;
        this.maxHp = maxHp;
        this.currentHp = maxHp;
        this.maxMana = 0;
        this.currentMana = 0;
        this.deck = deck;
        this.hand = new Hand();
    }

    public abstract void executeTurn();

    public abstract List<UnitCard> getBoardUnits(Board board);

    public void startTurnProcess() {
        if (maxMana < 10) {
            maxMana++;
        }
        currentMana = maxMana;

        try {
            Card drawnCard = deck.drawCard();
            hand.addCard(drawnCard);
        } catch (EmptyDeckException e) {
            this.currentHp = 0;
        }
    }

    public void playCard(Card card, ITarget target) throws InsufficientManaException {
        if (card.getManaCost() > currentMana) {
            throw new InsufficientManaException("Mana insuficiente! Necessário: " + card.getManaCost() + ", Disponível: " + currentMana);
        }

        currentMana -= card.getManaCost();
        hand.removeCard(card);
        card.applyEffect(target);
    }

    @Override
    public void receiveDamage(int amount) {
        if (amount > 0) {
            this.currentHp -= amount;
        }
    }

    @Override
    public void receiveHealing(int amount) {
        if (amount > 0) {
            this.currentHp = Math.min(this.currentHp + amount, this.maxHp);
        }
    }

    @Override
    public boolean isAlive() {
        return this.currentHp > 0;
    }

    public String getName() {
        return name;
    }

    public int getMaxHp() {
        return maxHp;
    }

    public int getCurrentHp() {
        return currentHp;
    }

    public int getMaxMana() {
        return maxMana;
    }

    public int getCurrentMana() {
        return currentMana;
    }

    public Deck getDeck() {
        return deck;
    }

    public Hand getHand() {
        return hand;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setMaxHp(int maxHp) {
        this.maxHp = maxHp;
    }

    public void setCurrentHp(int currentHp) {
        this.currentHp = currentHp;
    }

    public void setMaxMana(int maxMana) {
        this.maxMana = maxMana;
    }

    public void setCurrentMana(int currentMana) {
        this.currentMana = currentMana;
    }

    public void setDeck(Deck deck) {
        this.deck = deck;
    }

    public void setHand(Hand hand) {
        this.hand = hand;
    }


    @Override
    public String toString() {
        return name + " [HP: " + currentHp + "/" + maxHp + " | Mana: " + currentMana + "/" + maxMana + "]";
    }
}
