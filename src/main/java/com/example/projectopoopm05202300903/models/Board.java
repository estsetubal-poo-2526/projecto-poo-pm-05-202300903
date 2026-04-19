package com.example.projectopoopm05202300903.models;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Board {

    private List<UnitCard> playerUnits;
    private List<UnitCard> pcUnits;

    public Board() {
        this.playerUnits = new ArrayList<>();
        this.pcUnits = new ArrayList<>();
    }

    public void addUnit(Player player, UnitCard unit) {
        if (player instanceof HumanPlayer) {
            playerUnits.add(unit);
        } else if (player instanceof AiPlayer) {
            pcUnits.add(unit);
        }
    }

    public void removeDeadUnits() {
        Iterator<UnitCard> playerIterator = playerUnits.iterator();
        while (playerIterator.hasNext()) {
            if (!playerIterator.next().isAlive()) {
                playerIterator.remove();
            }
        }

        Iterator<UnitCard> pcIterator = pcUnits.iterator();
        while (pcIterator.hasNext()) {
            if (!pcIterator.next().isAlive()) {
                pcIterator.remove();
            }
        }
    }

    public List<UnitCard> getUnits(Player player) {
        if (player instanceof HumanPlayer) {
            return playerUnits;
        } else {
            return pcUnits;
        }
    }

    public List<UnitCard> getPlayerUnits() {
        return playerUnits;
    }

    public List<UnitCard> getPcUnits() {
        return pcUnits;
    }
}
