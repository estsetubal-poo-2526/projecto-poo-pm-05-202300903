package com.example.projectopoopm05202300903.models.player;

import com.example.projectopoopm05202300903.models.enums.PlayerType;

public class AiPlayer extends Player {
    public AiPlayer(String name) {
        super(name, PlayerType.AI);
    }
}