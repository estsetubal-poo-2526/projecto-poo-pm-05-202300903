package com.example.projectopoopm05202300903.models;

import com.example.projectopoopm05202300903.models.card.UnitCard;
import com.example.projectopoopm05202300903.models.card.UnitCardGroup;
import com.example.projectopoopm05202300903.models.enums.PlayerType;

import java.util.List;

public class Board {
    private final UnitCardGroup units = new UnitCardGroup();

    public void addUnit(PlayerType type, UnitCard card) {
        units.addUnit(type, card);
    }

    public void removeDeadUnits() {
        for (PlayerType type : PlayerType.values()) {
            units.getUnits(type).removeIf(UnitCard::isDead);
        }
    }

    public List<UnitCard> getUnits(PlayerType type) {
        return units.getUnits(type);
    }

    public List<UnitCard> getEnemiesOf(PlayerType type) {
        return units.getEnemiesOf(type);
    }

    public List<UnitCard> getAllUnits() {
        return units.getAllUnits();
    }
}