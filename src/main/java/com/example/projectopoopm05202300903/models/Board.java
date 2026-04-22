package com.example.projectopoopm05202300903.models;

import com.example.projectopoopm05202300903.models.card.UnitCard;

import java.util.ArrayList;
import java.util.List;

public class Board {
    private final List<UnitCard> playerUnits = new ArrayList<>();
    private final List<UnitCard> pcUnits     = new ArrayList<>();

    public void addPlayerUnit(UnitCard card) { playerUnits.add(card); }
    public void addPcUnit(UnitCard card)     { pcUnits.add(card); }

    public void removeDeadUnits() {
        playerUnits.removeIf(UnitCard::isDead);
        pcUnits.removeIf(UnitCard::isDead);
    }

    public List<UnitCard> getPlayerUnits() { return playerUnits; }
    public List<UnitCard> getPcUnits()     { return pcUnits; }
}