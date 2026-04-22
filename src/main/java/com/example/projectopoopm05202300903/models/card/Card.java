package com.example.projectopoopm05202300903.models.card;

import com.example.projectopoopm05202300903.models.interfaces.ITarget;

public abstract class Card {
    protected final String name;
    protected final int manaCost;
    protected final String description;

    protected Card(String name, int manaCost, String description) {
        this.name = name;
        this.manaCost = manaCost;
        this.description = description;
    }

    public abstract void applyEffect(ITarget target);

    public String getName() { return name; }
    public int getManaCost() { return manaCost; }
    public String getDescription() { return description; }
}
