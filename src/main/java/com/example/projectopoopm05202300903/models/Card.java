package com.example.projectopoopm05202300903.models;

import com.example.projectopoopm05202300903.interfaces.ITarget;

public abstract class Card {

    private String name;
    private int manaCost;
    private String description;

    public Card(String name, int manaCost, String description) {
        this.name = name;
        this.manaCost = manaCost;
        this.description = description;
    }

    public abstract void applyEffect(ITarget target);

    public String getName() {
        return name;
    }

    public int getManaCost() {
        return manaCost;
    }

    public String getDescription() {
        return description;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setManaCost(int manaCost) {
        this.manaCost = manaCost;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return name + " [Mana: " + manaCost + "] - " + description;
    }
}