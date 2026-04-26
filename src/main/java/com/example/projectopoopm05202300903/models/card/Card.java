package com.example.projectopoopm05202300903.models.card;

import com.example.projectopoopm05202300903.models.Board;
import com.example.projectopoopm05202300903.models.player.Player;

public abstract class Card {
    protected final String name;
    protected final int manaCost;
    protected final String description;

    protected Card(String name, int manaCost, String description) {
        this.name = name;
        this.manaCost = manaCost;
        this.description = description;
    }

    public abstract String play(Player caster, Player opponent, Board board);

    public abstract String[][] getCardAppearance(boolean onBoard);

    public String getName() { return this.name; }
    public int getManaCost() { return this.manaCost; }
    public String getDescription() { return this.description; }
}