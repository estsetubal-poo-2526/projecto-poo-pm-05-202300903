package com.example.projectopoopm05202300903.models;

import com.example.projectopoopm05202300903.models.Card.UnitCard;
import com.example.projectopoopm05202300903.models.Player.HumanPlayer;
import com.example.projectopoopm05202300903.models.Player.Player;

import java.util.ArrayList;
import java.util.List;

public class Board {
    private List<UnitCard> playerUnits;
    private List<UnitCard> pcUnits;

    public Board() {
        this.playerUnits = new ArrayList<>();
        this.pcUnits = new ArrayList<>();
    }

    public void addUnit(Player player, UnitCard unit) {
        playerUnits.add(unit);
    }

    public void removeDeadUnits() {
        playerUnits.removeIf(unitCard -> !unitCard.isAlive());
    }

    public List<UnitCard> getPlayerUnits() {return List.copyOf(playerUnits); }

    public List<UnitCard> getPCUnits() {
        return List.copyOf(pcUnits);
    }
}
