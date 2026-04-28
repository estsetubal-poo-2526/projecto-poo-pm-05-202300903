package com.example.projectopoopm05202300903.models.player;

import com.example.projectopoopm05202300903.models.enums.PlayerType;

public class HumanPlayer extends Player {
    public HumanPlayer(String name) {
        super(name, PlayerType.PLAYER);
    }
}