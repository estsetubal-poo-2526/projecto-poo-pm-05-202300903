package com.example.projectopoopm05202300903.models;

public class HumanPlayer extends Player {
    public HumanPlayer(String name, int maxHp, Deck deck) {
        super(name, maxHp, deck);
    }

    @Override
    public void executeTurn() {
        startTurnProcess();
    }
}
