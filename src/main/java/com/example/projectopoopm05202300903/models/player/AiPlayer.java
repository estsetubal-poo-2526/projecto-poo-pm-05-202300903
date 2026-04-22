package com.example.projectopoopm05202300903.models.player;

import com.example.projectopoopm05202300903.models.Board;
import com.example.projectopoopm05202300903.models.card.UnitCard;

import java.util.List;

public class AiPlayer extends Player {
    public AiPlayer(String name) {
        super(name);
    }

    @Override
    public void executeTurn() {
        // TODO document why this method is empty
    }

    @Override
    public List<UnitCard> getBoardUnits(Board board) {
        return List.of();
    }
}