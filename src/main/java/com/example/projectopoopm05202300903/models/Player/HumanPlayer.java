package com.example.projectopoopm05202300903.models.Player;

import com.example.projectopoopm05202300903.models.Board;
import com.example.projectopoopm05202300903.models.Card.UnitCard;
import com.example.projectopoopm05202300903.models.Deck;

import java.util.List;

public class HumanPlayer extends Player {
    public HumanPlayer(String name, int maxHp, Deck deck) {
        super(name, maxHp, deck);
    }

    @Override
    public void executeTurn() {
        startTurnProcess();
    }

    @Override
    public List<UnitCard> getBoardUnits(Board board) {
        return board.getPlayerUnits();
    }
}
